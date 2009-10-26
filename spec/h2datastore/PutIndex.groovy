import h2datastore.H2Utils
import h2datastore.Entities
import groovy.sql.Sql
import org.json.JSONObject

class PutIndex extends GroovyTestCase {

  def url
  def sql
  def entities
  def doc

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
  }

  void testPropertyPresent() {
      // given
      def index = entities.getIndex("name")

      // when
      entities.put(doc.toString())

      // then
      def res = index.find("Nesingwary 4000")
      assertNotNull res
  }

  void testPropertyNotPresent() {
      // given
      def index = entities.getIndex("some_property")

      // when
      entities.put(doc.toString())

      // then
      assertNull index.find("foo")
  }

}
