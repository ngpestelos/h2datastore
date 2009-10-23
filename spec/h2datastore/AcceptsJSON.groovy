import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class AcceptsJSON extends GroovyTestCase {

  def url
  def entities
  def sql

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testPut() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")

    // when
    def id = entities.put(doc)

    // then
    assertNotNull id
  }

}
