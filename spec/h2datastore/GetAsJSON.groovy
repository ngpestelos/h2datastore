import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class GetAsJSON extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testGet() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    def id = entities.put(doc)

    // when
    def res = entities.getAsJSON(id)

    // then
    assertNotNull res
    def body = res.get("body")
    assertTrue ("Nesingwary 4000" == body.get("name"))
  }

}
