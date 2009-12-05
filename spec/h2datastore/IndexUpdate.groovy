import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexUpdate extends GroovyTestCase {

  def entities
  def doc

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
  }

  void testUpdateName() {
    // given
    def id = entities.put(doc)
    def index = entities.getIndex("name")

    // when
    doc.put("name", "Fel Iron Musket")
    entities.update(id, doc.toString())

    // then
    assertNull index.find("Nesingwary 4000")
    assertNotNull index.find("Fel Iron Musket")
  }

  // Note: See also IndexRemove.testRemoveNothing
  void testUpdateNothing() {
    // given
    def id = entities.put(doc)
    def other_index = entities.getIndex("category")
    def other_doc = new JSONObject()
    other_doc.put("category", "def")
    entities.put(other_doc)

    // when
    doc.put("name", "Fel Iron Musket")
    entities.update(id, doc)

    // then
    assertNotNull other_index.find("def")
  }

  void testRemoveThisProperty() {
    // given
    doc.put("category", "Guns")
    def index_name = entities.getIndex("name")
    def id = entities.put(doc)

    // when
    doc.remove("name")
    entities.update(id, doc)
    
    // then
    assertNull index_name.find("Nesingwary 4000")
  }
}
