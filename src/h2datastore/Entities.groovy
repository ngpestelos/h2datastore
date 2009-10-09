package h2datastore

import java.util.UUID

class Entities {

    static def createTable(sql) {
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

    static def put(sql, String body) {
        def _id = UUID.randomUUID().toString()
        def updated = new java.sql.Timestamp(new Date().getTime())
        sql.dataSet("entities").add("_id" : _id, "body": body, "updated_at": updated)
        return _id
    }

    static def put(sql, String _id, String body) {
        def updated = new java.sql.Timestamp(new Date().getTime())
        sql.executeUpdate("update entities set body = ?, updated_at = ? where _id = ?", [body, updated, _id])
        return ["_id" : _id, "updated_at" : updated]
    }

    // Returns a Map or null
    static def get(sql, String _id) {
        def map = [:]
        sql.firstRow("select * from entities where _id = ?", [_id]).each {k, v ->
            map[k.toLowerCase()] = v
        }
        map
    }

    static def remove(sql, String _id) {
        sql.executeUpdate("delete from entities where _id = ?", [_id])
    }
	
}

