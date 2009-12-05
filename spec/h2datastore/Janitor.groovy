import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class JanitorSpec extends GroovyTestCase {

  def entities
  def index_name

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)

    index_name = entities.getIndex("name")

    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("type", "something")
    entities.put(doc)
  }

  void testClean() {
    // given
    assertTrue (1 == index_name.size())

    // when
    Janitor.clean()

    // then
    assertFalse (index_name.tableExists())
    //shouldFail { index_name.size() }
  }

}
