package h2datastore

import org.apache.commons.lang.builder.HashCodeBuilder
import org.json.JSONObject

import java.sql.Types

/**
 * Index tables are used to query the entities table for certain properties
 */
class Index implements DatastoreListener {

    def property
    def sql
    def dataType

    // acceptable types
    final def TYPES = [(Types.VARCHAR):'VARCHAR', (Types.TIMESTAMP):'TIMESTAMP']

    def Index(sql, property, dataType = Types.VARCHAR) {
        if (sql == null)
            throw new IllegalArgumentException("SQL cannot be null.")
        if (property == null)
            throw new IllegalArgumentException("Property cannot be null.")
        if (!(dataType in getAcceptableTypes()))
            throw new IllegalArgumentException("Keys can only be of varchar or timestamp types.")

        this.sql = sql
        this.property = property
        this.dataType = dataType
        createTable()
    }

    String toString() {
        "Index for ${property}"
    }

    def find(value) {
        def table = "index_${property}"
        sql.rows("select entities.* from ${table} join entities on ${table}.entity_id = entities._id where ${table}.${property} = ?", [value])
    }

    def put(value, entityID) {
        def table = "index_${property}"
        sql.executeUpdate("merge into ${table} (${property}, entity_id) key (entity_id) values (?, ?)", [value, entityID])
    }

    def remove(entityID) {
        sql.executeUpdate("delete from ${getTableName()} where entity_id = ?", [entityID])
    }

    def createTable() {
        def query = "create table if not exists index_" + property + " ( " + property + " " + getType() +
            " not null, entity_id uuid not null, primary key (" + property + ", entity_id) )";
        sql.executeUpdate(query)
        def res = sql.firstRow("select count(entity_id) as rows from index_" + property)
        if (res.rows == 0)
            populate()
    }

    def destroyTable() {
        def query = "drop table index_" + property + " if exists"
        sql.executeUpdate(query)
    }

    private def getType() {
        TYPES[dataType]
    }

    private def getAcceptableTypes() {
        TYPES.keySet()
    }

    private def getTableName() {
        "index_${property}"
    }

    private def populate() {
        Thread.start {
            sql.rows("select * from entities").each {
                def json = new JSONObject(it.body.characterStream.text)
                if (json.has(property))
                    put(json.get(property), it."_id")
            }
        }
    }

    boolean equals(obj) {
        return obj instanceof Index && this.hashCode() == obj.hashCode()
    }

    int hashCode() {
        return new HashCodeBuilder(15, 55).append(sql).append(property).toHashCode()
    }

    def count() {
        def res = sql.firstRow("select count(*) as numrows from " + getTableName())
        res ? res["numrows"] : 0
    }

    //// Callbacks

    void entityAdded(dsEvent) {
        Thread.start {
            def json = new JSONObject(dsEvent.body)
            if (json.has(property))
                put(json.get(property), dsEvent.id)
        }
    }

    void entityUpdated(dsEvent) {
        Thread.start {
            def json = new JSONObject(dsEvent.body)
            if (json.has(property))
                put(json.get(property), dsEvent.id)
        }
    }

    void entityRemoved(dsEvent) {
        Thread.start {
            def json = new JSONObject(dsEvent.body)
            if (json.has(property))
                remove(dsEvent.id)
        }
    }

    //// End Callbacks

    static def getIndices(sql) {
        def res = sql.rows("select table_name from information_schema.tables where table_name like 'INDEX%' order by table_name")
        def tables = res.findAll { it['TABLE_NAME'] =~ /^INDEX_/ }
        tables.collect { it['TABLE_NAME'].toLowerCase() }
    }
}