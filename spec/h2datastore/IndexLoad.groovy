import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class IndexLoad extends GroovyTestCase {

  def entities
  def doc

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
  }

  void testShouldBeOne() {
    // given
    entities.put(doc)
    def index = entities.getIndex("name")
    assert (0 == index.size())

    // when
    index.load()

    // then
    assert (1 == index.size())
  }

}
