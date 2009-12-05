package h2datastore

class Janitor {

    static def clean() {
        allTables().each {
            getSql().executeUpdate("drop table " + it)
        }
    }

    private static def allTables() {
        def filtered = getSql().rows("select TABLE_NAME from information_schema.tables").findAll { it["TABLE_NAME"] =~ /^INDEX_/ }
        filtered.collect { it["TABLE_NAME"] }
    }

    private static def getSql() {
        def entities = Entities.getInstance()
        entities.sql
    }
    
}
