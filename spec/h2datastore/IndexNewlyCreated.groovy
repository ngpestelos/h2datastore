import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexNewlyCreated extends GroovyTestCase {

  def entities

  void setUp() {
    def sql = Sql.newInstance(H2Utils.buildMemoryURL(), "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testShouldHaveZeroRows() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    entities.put(doc)

    // when
    def index = entities.getIndex("name")
    
    // then
    assertTrue (0 == index.size())
  }

}
