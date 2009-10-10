package h2datastore

import org.json.JSONObject

/**
 * Index tables are used to query the entities table for certain properties
 */
class Index {

    // TODO support for multiple properties (I still need to figure out how a multi-key index works)
    // TODO support for secondary index (search within an index)
    def property
    def sql

    def Index(sql, property) {
        if (sql == null)
            throw new IllegalArgumentException("SQL cannot be null.")
        if (property == null)
            throw new IllegalArgumentException("Property cannot be null.")

        this.sql = sql
        this.property = property
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

    def createTable() {
        // TODO allow for other data types (e.g. by date)
        def query = "create table if not exists index_" + property + " ( " + property + " varchar(254)" +
            " not null, entity_id uuid not null, primary key (" + property + ", entity_id) )";
        sql.executeUpdate(query)
        def res = sql.firstRow("select count(entity_id) as rows from index_" + property)
        if (res.rows == 0)
            populateTable()
    }

    def destroyTable() {
        def query = "drop table index_" + property + " if exists"
        sql.executeUpdate(query)
    }

    private def populateTable() {
        sql.rows("select * from entities").each {
            def json = new JSONObject(it.body.characterStream.text)
            if (json.has(property))
                put(json.get(property), it."_id")
        }
    }

    // TODO How do we populate the index?
    // 1. listen to an event (what is the protocol?) (cleaner)
    // 2. spawn a thread to populate the index right after creating the index table
    

}