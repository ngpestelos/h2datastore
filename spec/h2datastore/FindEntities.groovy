import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class FindEntities extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testFindOne() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    def id = entities.put(doc)
    def index = entities.getIndex("name")  
  
    // when
    def res = index.find("Nesingwary 4000")

    // then
    assertTrue id == res._id
    assertNotNull res.body
  }

  void testFindAll() {
    // given
    def first = new JSONObject()
    first.put("name", "Nesingwary 4000")
    first.put("category", "Guns")
    def second = new JSONObject()
    second.put("name", "Fel Iron Musket")
    second.put("category", "Guns")
    entities.put(first)
    entities.put(second)
    def index = entities.getIndex("category")

    // when
    def res = index.findAll("Guns")

    // then
    assertTrue (2 == res.size())
  }

}
