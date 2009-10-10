import groovy.sql.Sql

import h2datastore.Entities
import h2datastore.Index

class IndexTest extends GroovyTestCase {

  void testPut() {
    def sql = Sql.newInstance("jdbc:h2:mem:put")
    Entities.createTable(sql)
    def id = Entities.put(sql, "{\"name\" : \"Nesingwary 4000\"}")
    def index = new Index(sql, "name")
    index.createTable()
    
    assertTrue (1 == index.put("Nesingwary 4000", id))
  }

  void testFind() {
    def sql = Sql.newInstance("jdbc:h2:mem:find")
    Entities.createTable(sql)
    def id = Entities.put(sql, "{\"name\" : \"Nesingwary 4000\"}")
    def index = new Index(sql, "name")
    index.createTable()
    index.put("Nesingwary 4000", id)
    
    assertTrue (1 == index.find("Nesingwary 4000").size())

    Entities.put(sql, "Copper Ore")
    assertTrue (0 == index.find("Copper Ore").size())

    index.put("Nesingwary 4000XP", id)
    assertTrue (1 == index.find("Nesingwary 4000XP").size())
    assertTrue (0 == index.find("Nesingwary 4000").size())

    index.destroyTable()
  }

  static void main(args) {
    junit.textui.TestRunner.run(IndexTest.class)
  }

}
