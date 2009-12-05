import h2datastore.*
import groovy.sql.Sql

class EntityListenerCount extends GroovyTestCase {

  def entities

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testHowManyIndices() {
    // given
    entities.getIndex("name")
    entities.getIndex("category")
    entities.getIndex("name")

    // when
    def res = entities.listenerCount()

    // then
    assertTrue (2 == res)
  }

}
