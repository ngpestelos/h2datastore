package h2datastore

import java.util.UUID

class Entities {

    def sql
    def listeners
    
    private static def instance

    private def Entities() { }

    static def getInstance(sql) {
        if (!instance)
            instance = new Entities()

        instance.sql = sql
        instance.createTable()
        instance.listeners = []
        instance
    }

    def addListener(listener) {
        if (!(listener in listeners))
            listeners << listener
    }

    def createTable() {
        def table = "create table if not exists entities (" +
            "added_id identity auto_increment primary key," +
            "_id uuid not null," +
            "body clob," +
            "updated_at timestamp not null);"
        sql.executeUpdate(table)

        def index_1 = "create index if not exists ix_entities_id on entities(_id);";
        sql.executeUpdate(index_1)

        def index_2 = "create index if not exists ix_entities_timestamp on entities(updated_at);";
        sql.executeUpdate(index_2)
    }

    def put(String body) {
        def _id = UUID.randomUUID().toString()
        def updated = new java.sql.Timestamp(new Date().getTime())
        sql.dataSet("entities").add("_id" : _id, "body": body, "updated_at": updated)
        entityAdded(_id, body)
        return _id
    }

    def put(String _id, String body) {
        def updated = new java.sql.Timestamp(new Date().getTime())
        sql.executeUpdate("update entities set body = ?, updated_at = ? where _id = ?", [body, updated, _id])
        entityUpdated(_id, body)
        return ["_id" : _id, "updated_at" : updated]
    }

    // Returns a Map or null
    def get(String _id) {
        def map = [:]
        sql.firstRow("select * from entities where _id = ?", [_id]).each {k, v ->
            if (k == "body")
                map[k.toLowerCase()] = v.characterStream.text
            else
                map[k.toLowerCase()] = v
        }
        map
    }

    def remove(String _id) {
        def res = sql.executeUpdate("delete from entities where _id = ?", [_id])
        entityRemoved(_id)
        res
    }

    private def entityAdded(_id, body) {
        listeners.each { it.entityAdded(new DatastoreEvent(this, _id, body)) }
    }

    private def entityUpdated(_id, body) {
        listeners.each { it.entityUpdated(new DatastoreEvent(this, _id, body)) }
    }

    private def entityRemoved(_id) {
        listeners.each { it.entityRemoved(new DatastoreEvent(this, _id)) }
    }
	
}

