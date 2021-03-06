import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class EntityAcceptsUUID extends GroovyTestCase {

  def entities
  def sql

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
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
