package h2datastore

import java.util.UUID

class Entities {

    static def createTable(sql) {
        def table = "create table if not exists entities (" +
            "added_id identity auto_increment primary key," +
            "id uuid not null," +
            "body clob," +
            "updated_at timestamp not null);"
        sql.executeUpdate(table)

        def index_1 = "create index if not exists ix_entities_id on entities(id);";
        sql.executeUpdate(index_1)

        def index_2 = "create index if not exists ix_entities_timestamp on entities(updated_at);";
        sql.executeUpdate(index_2)
    }

    static def put(sql, String body) {
        def id = UUID.randomUUID().toString()
        def updated = new java.sql.Timestamp(new Date().getTime())
        sql.dataSet("entities").add("id" : id, "body": body, "updated_at": updated)
        return id
    }

    // Returns a Map or null
    static def get(sql, String id) {
        sql.firstRow("select * from entities where id = ?", [id])
    }

    static def remove(sql, String id) {
        sql.executeUpdate("delete from entities where id = ?", [id])
    }
	
}

