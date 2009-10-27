package h2datastore

import org.json.JSONObject
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.log4j.Logger

/**
 * Index tables are used to query the entities table for certain properties
 *
 * Assumes Entity bodies are JSON objects
 */
class Index implements DatastoreListener {

    def property
    def logger
    def entities
    def sql

    protected def Index(property, Boolean timestamp = false) {
        this.logger = Logger.getLogger(Index.class)
        this.property = property
        this.entities = Entities.getInstance()
        this.sql = entities.sql
        def tableExists = sql.firstRow("select TABLE_NAME from information_schema.tables where table_name = ?",
            ["INDEX_" + property.toUpperCase()])
        if (!tableExists) {
            createTable(timestamp)
            populateTable()
        }
    }

    String toString() {
        return "${property}"
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

    /**
     * Search entities having property matching value. Returns a list of matching Entities
     *
     * @see Index.find
     */
    List findAll(value) {
        def table = getTableName()
        def res = sql.rows("select entity_id from ${table} where ${property} = ?", [value])
        if (!res)
            return []

        res.collect {
            entities.get(it["ENTITY_ID"].toString())
        }
    }

    def put(value, eid) {
        def table = getTableName()
        logger.debug("put ${table} ${value} ${eid}")
        sql.executeUpdate("merge into ${table} (${property}, entity_id) key (entity_id) values (?, ?)", [value, eid])
    }

    private def createTable(Boolean timestamp) {
        logger.debug("creating index table for ${property}")
        def dataType = timestamp ? "bigint" : "varchar(768)"
        def query = "create table if not exists index_" + property + " ( " + property + " " + dataType +
            " not null, entity_id uuid not null, primary key (" + property + ", entity_id) )";
        sql.executeUpdate(query)
    }

    private def populateTable() {
        logger.debug("populating index table for ${property}")
        sql.rows("select * from entities").each {
            def json = new JSONObject(it.body.characterStream.text)
            if (json.has(property))
                put(json.get(property), it."_id")
        }
    }

    def remove(entityID) {
        sql.executeUpdate("delete from ${getTableName()} where entity_id = ?", [entityID])
    }

    private def getTableName() {
        "index_${property}"
    }

    def size() {
        def res = sql.firstRow("select count(*) as numrows from " + getTableName())
        res ? res["numrows"] : 0
    }

    //// Callbacks

    void entityAdded(dsEvent) {
        def json = new JSONObject(dsEvent.body)
        logger.debug("entity added ${json}")
        if (json.has(property))
            put(json.get(property), dsEvent.id)
    }

    void entityUpdated(dsEvent) {
        def json = new JSONObject(dsEvent.body)
        logger.debug("entity updated ${json}")
        if (json.has(property))
            put(json.get(property), dsEvent.id)
    }

    void entityRemoved(dsEvent) {
        def table = getTableName()
        logger.debug("entity removed ${table}")
        sql.executeUpdate("delete from ${table} where entity_id = ?", [dsEvent.id])
    }

    void cleanup(dsEvent) {
        //populate()
    }

    //// End Callbacks


    boolean equals(obj) {
        return obj instanceof Index && this.hashCode() == obj.hashCode()
    }

    int hashCode() {
        return new HashCodeBuilder(15, 55).append(sql).append(property).toHashCode()
    }
}