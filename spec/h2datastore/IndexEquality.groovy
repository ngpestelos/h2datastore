import h2datastore.*
import groovy.sql.Sql

class IndexEquality extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql) 
  }

  void testEqual() {
    // when
    def index_one = entities.getIndex("name")
    def index_two = entities.getIndex("name")

    // then
    assertTrue (index_one == index_two)
  }

  void testNotEqual() {
    // when
    def index_name = entities.getIndex("name")
    def index_category = entities.getIndex("category")

    // then
    assertFalse (index_name == index_category)
  }

}
