import h2datastore.H2Utils
import h2datastore.Entities
import groovy.sql.Sql
import org.json.JSONObject

class UpdateIndex extends GroovyTestCase {

  def url
  def sql
  def entities
  def doc

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
  }

  void testUpdateName() {
    // given
    def id = entities.put(doc.toString())
    def index = entities.getIndex("name")

    // when
    def ndoc = new JSONObject(doc)
    ndoc.put("name", "Fel Iron Musket")
    entities.update(id, ndoc.toString())

    // then
    assertNull index.find("Nesingwary 4000")
    assertNotNull index.find("Fel Iron Musket")
  }

  void testUpdateNothing() {
    // given
    def id = entities.put(doc.toString())
    def category_doc = new JSONObject()
    category_doc.put("category", "def")
    entities.put(category_doc.toString())
    def index = entities.getIndex("category")

    // when
    def ndoc = new JSONObject()
    ndoc.put("name", "Fel Iron Musket")
    entities.update(id, ndoc.toString())

    // then
    assertNotNull index.find("def")
  }
}
