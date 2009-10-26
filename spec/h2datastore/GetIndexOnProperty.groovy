import h2datastore.Entities
import h2datastore.H2Utils
import groovy.sql.Sql
import org.json.JSONObject

class GetIndexOnProperty extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testEmpty() {
    // when
    def index = entities.getIndex("some_property")

    // then
    assertNull index.find("foo") 
  }

  void testNotEmpty() {
    // given
    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("category", "abc")
    entities.put(doc.toString())

    // when
    def index = entities.getIndex("name")

    // then
    def res = index.find("foo")
    assertNotNull res
  }

  void testNoPropertiesMatching() {
    // given
    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("category", "abc")

    // when
    def index = entities.getIndex("supplier")

    // then
    def res = index.find("bar")
    assertNull res
  } 

}
