import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexPut extends GroovyTestCase {

  def entities
  def doc

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
  }

  void testPropertyPresent() {
      // given
      def index = entities.getIndex("name")

      // when
      entities.put(doc)

      // then
      def res = index.find("Nesingwary 4000")
      assertNotNull res
  }

  void testPropertyNotPresent() {
      // given
      def index = entities.getIndex("some_property")

      // when
      entities.put(doc)

      // then
      assertNull index.find("foo")
  }

}
