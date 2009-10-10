import h2datastore.Entities
import h2datastore.Index

import org.json.JSONObject

import groovy.sql.Sql

class IndexTablePopulateTest extends GroovyTestCase {

  /*
  void setUp() {
    sql = Sql.newInstance("jdbc:h2:mem:populate")
    Entities.createTable(sql)
    def one = new JSONObject()
    one.put("name", "Nesingwary 4000")
    Entities.put(sql, one.toString())
    def two = new JSONObject()
    two.put("name", "Fel Iron Musket")
    Entities.put(sql, two.toString())
  }*/

  void testPopulateIndex() {
    def sql = Sql.newInstance("jdbc:h2:mem:populate")
    def ent = Entities.getInstance(sql)

    def one = new JSONObject()
    one.put("name", "Nesingwary 4000")
    ent.put(one.toString())

    def two = new JSONObject()
    two.put("name", "Fel Iron Musket")
    ent.put(two.toString())

    def index = new Index(sql, "name")
    assertTrue (1 == index.find("Nesingwary 4000").size())
    assertTrue (1 == index.find("Fel Iron Musket").size())
    assertTrue (0 == index.find("Magic Carpet").size())
  }

  static void main(args) {
    junit.textui.TestRunner.run(IndexTablePopulateTest.class)
  }

}
