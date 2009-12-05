import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexRemove extends GroovyTestCase {

  def entities
  def doc

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
  }

  void testRemoveMatchingEntity() {
    // given
    def id = entities.put(doc)
    def index = entities.getIndex("name")

    // when
    entities.remove(id)

    // then
    assertNull index.find("Nesingwary 4000")
  }

  void testRemoveNothing() {
    // given
    def other_index = entities.getIndex("category")
    def other_doc = new JSONObject()
    other_doc.put("category", "something")
    entities.put(other_doc.toString())

    // when
    def id = entities.put(doc.toString())
    entities.remove(id)

    // then
    assertNotNull other_index.find("something")
  }
}
