import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexSize extends GroovyTestCase {

  def entities
  def index
  def doc

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    index = entities.getIndex("name")
  }

  void testZero() {
    // then
    assertTrue (0 == index.size())
  }

  void testNotZero() {
    // given
    entities.put(doc)

    // then
    assertTrue (1 == index.size())    
  }

}
