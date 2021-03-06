package h2datastore

import org.apache.log4j.Logger
import org.json.JSONObject
import java.util.UUID

import groovy.sql.Sql

class Entities {

    def sql
    def listeners
    def logger
    
    private static def instance

    private def Entities(sql) {
        this.logger = Logger.getLogger(Entities.class)
        this.sql = sql
        this.listeners = []
        Entities.initialize(sql)
    }

    /**
     * Create a pristine Entities object.
     *
     * This is a wipe.
     */
    static def newInstance(sql) {
        instance = new Entities(sql)
        return instance
    }

    /**
     * Return the current Entities instance
     */
    static def getInstance() {
        if (!instance)
            throw new IllegalStateException("Call Entities.newInstance(sql) first.")
            
        return instance
    }

    def addListener(listener) {
        if (!(listener in listeners)) {
            logger.debug("add listener ${listener}")
            listeners << listener
        }
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

    def getIndex(String property) {
        logger.debug("getIndex ${property}")
        def index = new Index(property)
        addListener(index)
        index
    }

    def listenerCount() {
        listeners.size()
    }

    def entityCount() {
        def res = sql.firstRow("select count(_id) as num_entities from entities")
        res["num_entities"]
    }

    def clearListeners() {
        listeners.clear()
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
        logger.debug("entity removed ${_id}")
        listeners.each { it.entityRemoved(new DatastoreEvent(this, _id)) }
    }

    static def initialize(sql) {
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

    // TODO I'm still trying to figure out how to drop the instance
    static def reset() {
        instance = null
    }
	
}

