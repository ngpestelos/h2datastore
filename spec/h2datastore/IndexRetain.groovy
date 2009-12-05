import h2datastore.*
import groovy.sql.Sql

class IndexRetain extends GroovyTestCase {

  def entities

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
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
