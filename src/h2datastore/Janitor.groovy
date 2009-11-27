package h2datastore

class Janitor {

  static def clean() {
    def entities = Entities.getInstance()
    def sql = entities.sql

    Index.allTables().each {
      sql.executeUpdate("drop table " + it) 
    } 
  }

}
