import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class EntityAcceptsJSON extends GroovyTestCase {

  def entities
  def sql

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
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

  void testUpdate() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")
    def id = entities.put(doc)

    // when
    doc.put("category", "Guns")
    doc.put("level", 80)
    def res = entities.update(id, doc)

    // then
    assertNotNull res
  }

}
