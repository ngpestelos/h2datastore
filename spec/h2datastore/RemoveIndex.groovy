import h2datastore.H2Utils
import h2datastore.Entities
import groovy.sql.Sql
import org.json.JSONObject

class RemoveIndex extends GroovyTestCase {

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

  void testRemoveMatchingEntity() {
    // given
    def id = entities.put(doc.toString())
    def index = entities.getIndex("name")

    // when
    entities.remove(id)

    // then
    assertNull index.find("Nesingwary 4000")
  }

  void testRemoveNothing() {
    // given
    def id = entities.put(doc.toString())
    def category_doc = new JSONObject()
    category_doc.put("category", "something")
    entities.put(category_doc.toString())
    def index = entities.getIndex("category")

    Thread.sleep(100)

    // when
    entities.remove(id)

    // then
    assertNotNull index.find("something")
  }
}
