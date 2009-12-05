import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class EntityFind extends GroovyTestCase {

  def entities

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testFindOne() {
    // given
    def index = entities.getIndex("name")  
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    def id = entities.put(doc)
 
    // when
    def res = index.find("Nesingwary 4000")

    // then
    assertTrue id == res._id
    assertNotNull res.body
  }

  void testFindAll() {
    // given
    def index = entities.getIndex("category")
    def first = new JSONObject()
    first.put("name", "Nesingwary 4000")
    first.put("category", "Guns")
    def second = new JSONObject()
    second.put("name", "Fel Iron Musket")
    second.put("category", "Guns")
    entities.put(first)
    entities.put(second)

    // when
    def res = index.findAll("Guns")

    // then
    assertTrue (2 == res.size())
  }

}
