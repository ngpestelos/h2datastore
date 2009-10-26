package h2datastore

import org.apache.log4j.Logger
import org.json.JSONObject
import java.util.UUID

class Entities {

    def sql
    def listeners
    def logger
    
    private static def instance

    private def Entities(sql) {
        this.logger = Logger.getLogger(Entities.class)
        this.sql = sql
        createTable()
        this.listeners = []
    }

    static def getInstance(sql) {
        if (!instance)
            instance = new Entities(sql)
            
        instance
    }

    def addListener(listener) {
        logger.debug("add listener ${listener}")
        if (!(listener in listeners))
            listeners << listener
    }

    private def createTable() {
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

    def put(JSONObject json) {
        put(json.toString())
    }

    def put(String body) {
        def _id = UUID.randomUUID().toString()
        def updated = new java.sql.Timestamp(new Date().getTime())
        sql.dataSet("entities").add("_id" : _id, "body": body, "updated_at": updated)
        entityAdded(_id, body)
        return _id
    }

    def update(String _id, JSONObject json) {
        update(_id, json.toString())
    }

    def update(String _id, String body) {
        def updated = new java.sql.Timestamp(new Date().getTime())
        def rows_affected = sql.executeUpdate("update entities set body = ?, updated_at = ? where _id = ?", [body, updated, _id])
        if (rows_affected == 0)
            return [:]

        entityUpdated(_id, body)
        return ["_id" : _id, "updated_at" : updated]
    }

    def getAsJSON(UUID _ID) {
        getAsJSON(_id.toString())
    }

    // @see Entities.get
    def getAsJSON(String _id) {
        def map = get(_id)
        if (!map)
            return null

        def json = new JSONObject()
        json.put("_id", map["_id"])
        json.put("body", new JSONObject(map["body"]))
        json.put("updated_at", map["updated_at"])
        return json
    }

    def get(UUID _id) {
        get(_id.toString())
    }

    /**
     * Fetches an Entity for the specified identifier
     *
     * @return Map having these attributes: _id, body, and updated_at
     * 
     */
    def get(String _id) {
        def map = [:]
        sql.firstRow("select * from entities where _id = ?", [_id]).each { k, v ->
            if (k == "BODY")
                map[k.toLowerCase()] = v.characterStream.text
            else if (k == "_ID")
                map[k.toLowerCase()] = v.toString()
            else
                map[k.toLowerCase()] = v
        }
        map
    }

    def remove(String _id) {
        def rows_affected = sql.executeUpdate("delete from entities where _id = ?", [_id])
        if (rows_affected == 0)
            return rows_affected

        entityRemoved(_id)
        return rows_affected
    }

    def getIndex(String property, Boolean timestamp = false) {
        def index = new Index(sql, property, this, timestamp)
        addListener(index)
        index
    }

    def listenerCount() {
        listeners.size()
    }

    // @param list
    // @param closure (accepts one element)
    // @return another list
    static def map(list, closure) {
        list.collect { closure(it) }
    }

    private def entityAdded(_id, body) {
        logger.debug("entity added ${_id} ${body} ${listeners}")
        listeners.each { it.entityAdded(new DatastoreEvent(this, _id, body)) }
    }

    private def entityUpdated(_id, body) {
        logger.debug("entity updated ${_id} ${body} ${listeners}")
        listeners.each { it.entityUpdated(new DatastoreEvent(this, _id, body)) }
    }

    private def entityRemoved(_id) {
        listeners.each { it.entityRemoved(new DatastoreEvent(this, _id)) }
    }
	
}

