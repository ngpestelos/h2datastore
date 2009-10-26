import h2datastore.*
import groovy.sql.Sql

class IndexCount extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
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
