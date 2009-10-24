import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class AcceptsUUID extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testGetEntity() {
    // given
    def doc = new JSONObject()
    doc.put("name", "Nesingwary 4000")

    // when
    entities.put(doc)
    def res = sql.firstRow("select * from entities")

    // then
    assert res._id instanceof java.util.UUID
    assertNotNull entities.get(res._id)
  }

}
