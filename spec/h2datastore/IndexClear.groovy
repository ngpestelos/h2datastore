import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexClear extends GroovyTestCase {

  def entities
  def index_name
  def doc

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    index_name = entities.getIndex("name")
  }

  void testShouldBeZero() {
    // given
    entities.put(doc)
    assert (1 == index_name.size())

    // when
    index_name.clear()

    // then
    assert (0 == index_name.size()) 
  }

}
