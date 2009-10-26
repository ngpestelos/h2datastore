import h2datastore.*
import groovy.sql.Sql

class RetainIndex extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testKeepTheseIndices() {
    // given
    entities.getIndex("name")
    entities.getIndex("category")

    assert (2 == entities.listenerCount())

    // when
    entities = Entities.getInstance()

    // then
    assertTrue (2 == entities.listenerCount()) 
  }

}
