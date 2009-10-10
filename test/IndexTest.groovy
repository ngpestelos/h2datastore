import groovy.sql.Sql

import h2datastore.Entities
import h2datastore.Index

class IndexTest extends GroovyTestCase {

  void testPut() {
    def sql = Sql.newInstance("jdbc:h2:mem:put")
    Entities.createTable(sql)
    def id = Entities.put(sql, "Nesingwary 4000")
    def index = new Index("name")
    index.createTable(sql)
    
    assertTrue (1 == index.put(sql, "Nesingwary 4000", id))
  }

  void testFind() {
    def sql = Sql.newInstance("jdbc:h2:mem:find")
    Entities.createTable(sql)
    def id = Entities.put(sql, "Nesingwary 4000")
    def index = new Index("name")
    index.createTable(sql)
    index.put(sql, "Nesingwary 4000", id)
    
    assertTrue (1 == index.find(sql, "Nesingwary 4000").size())

    Entities.put(sql, "Copper Ore")
    assertTrue (0 == index.find(sql, "Copper Ore").size())

    index.put(sql, "Nesingwary 4000XP", id)
    assertTrue (1 == index.find(sql, "Nesingwary 4000XP").size())
    assertTrue (0 == index.find(sql, "Nesingwary 4000").size())

    index.destroyTable(sql)
  }

  static void main(args) {
    junit.textui.TestRunner.run(IndexTest.class)
  }

}
