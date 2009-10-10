package h2datastore

/**
 * Index tables are used to query the entities table for certain properties
 */
class Index {

    def property

    def Index(property) {
        this.property = property
    }

    String toString() {
        "Table: ${table}, Property: ${property}"
    }

    def find(sql, value) {
        def table = "index_${property}"
        sql.rows("select entities.* from ${table} join entities on ${table}.entity_id = entities._id where ${table}.${property} = ?", [value])
    }

    def put(sql, value, entityID) {
        def table = "index_${property}"
        sql.executeUpdate("merge into ${table} (${property}, entity_id) key (entity_id) values (?, ?)", [value, entityID])
    }

    def createTable(sql) {
        // TODO allow for other data types
        def query = "create table if not exists index_" + property + " ( " + property + " varchar(254)" +
            " not null, entity_id uuid not null, primary key (" + property + ", entity_id) )";
        sql.executeUpdate(query)
    }

    def destroyTable(sql) {
        def query = "drop table index_" + property + " if exists"
        sql.executeUpdate(query)
    }

    // TODO How do we populate the index?
    // 1. listen to an event (what is the protocol?) (cleaner)
    // 2. spawn a thread to populate the index right after creating the index table
    

}

