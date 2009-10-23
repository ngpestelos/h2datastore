package h2datastore

import org.apache.commons.lang.builder.HashCodeBuilder
import org.json.JSONObject
import java.sql.Types

/**
 * Index tables are used to query the entities table for certain properties
 *
 * Assumes Entity bodies are JSON objects
 */
class Index implements DatastoreListener {

    def property
    def sql
    def entities

    protected def Index(sql, property, entities, Boolean timestamp = false) {
        if (sql == null)
            throw new IllegalArgumentException("SQL cannot be null.")
        if (property == null)
            throw new IllegalArgumentException("Property cannot be null.")

        this.sql = sql
        this.property = property
        this.entities = entities
        createTable(timestamp)
        populateTable()
    }

    String toString() {
        "Index for ${property}"
    }

    /**
     * Search entities having property matching value. Returns the first one found.
     *
     * Examples:
     *  def types = entities.getIndex("type")
     *  def item = types.find("item") // returns the first entity found having type = "item"
     *
     * @see Entities.get
     */
    def find(value) {
        def table = getTableName()
        def res = sql.firstRow("select entity_id from ${table} where ${property} = ?", [value])
        if (!res)
            return null

        entities.get(res["ENTITY_ID"].toString())
    }

    def put(value, eid) {
        def table = getTableName()
        sql.executeUpdate("merge into ${table} (${property}, entity_id) key (entity_id) values (?, ?)", [value, eid])
    }

    private def createTable(Boolean timestamp) {
        def dataType = timestamp ? "bigint" : "varchar(768)"
        def query = "create table if not exists index_" + property + " ( " + property + " " + dataType +
            " not null, entity_id uuid not null, primary key (" + property + ", entity_id) )";
        sql.executeUpdate(query)
    }

    private def populateTable() {
        if (size() == 0) {
            sql.rows("select * from entities").each {
                def json = new JSONObject(it.body.characterStream.text)
                if (json.has(property))
                    put(json.get(property), it."_id")   
            }
        }
    }

    def remove(entityID) {
        sql.executeUpdate("delete from ${getTableName()} where entity_id = ?", [entityID])
    }

    /*
    def destroyTable() {
        def query = "drop table index_" + property + " if exists"
        sql.executeUpdate(query)
    }*/

    /*
    private def getType() {
        TYPES[dataType]
    }*/

    /*
    private def getAcceptableTypes() {
        TYPES.keySet()
    }*/

    private def getTableName() {
        "index_${property}"
    }

    /*
    boolean equals(obj) {
        return obj instanceof Index && this.hashCode() == obj.hashCode()
    }

    int hashCode() {
        return new HashCodeBuilder(15, 55).append(sql).append(property).toHashCode()
    }*/

    def size() {
        def res = sql.firstRow("select count(*) as numrows from " + getTableName())
        res ? res["numrows"] : 0
    }

    //// Callbacks

    void entityAdded(dsEvent) {
        def json = new JSONObject(dsEvent.body)
        if (json.has(property))
            put(json.get(property), dsEvent.id)
    }

    void entityUpdated(dsEvent) {
        def json = new JSONObject(dsEvent.body)
        if (json.has(property))
            put(json.get(property), dsEvent.id)
    }

    void entityRemoved(dsEvent) {
        def table = getTableName()
        sql.executeUpdate("delete from ${table} where entity_id = ?", [dsEvent.id])
    }

    void cleanup(dsEvent) {
        //populate()
    }

    //// End Callbacks
}
