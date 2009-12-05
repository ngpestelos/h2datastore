import h2datastore.*
import groovy.sql.Sql

class IndexToString extends GroovyTestCase {

  def entities

  void setUp() {
    def sql = Sql.newInstance(H2Utils.buildMemoryURL(), "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testToString() {
    // given
    def index = entities.getIndex("name")

    assertTrue ("name" == index.toString())
  }

}
