import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexEvents extends GroovyTestCase {

  def url
  def sql
  def entities
  def index_name

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
    index_name = entities.getIndex("name")
  }

  void testIndexEvents() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")

    // when
    def id = entities.put(doc)

    // then
    assertNotNull index_name.find("Nesingwary 4000")

    // when
    doc.put("name", "Fel Iron Ore")
    entities.update(id, doc)

    // then
    assertNull index_name.find("Nesingwary 4000")
    assertNotNull index_name.find("Fel Iron Ore")

    // when
    entities.remove(id)

    // then
    assertNull index_name.find("Fel Iron Ore")
  }

}
