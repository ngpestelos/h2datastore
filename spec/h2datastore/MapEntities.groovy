import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class MapEntities extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testMapIDs() {
    // given
    def first = new JSONObject()
    first.put("name", "Nesingwary 4000")
    entities.put(first)

    def second = new JSONObject()
    second.put("name", "Fel Iron Musket")
    entities.put(second)

    // when
    def ids = sql.rows("select * from entities").collect { it._id }

    // then
    assertTrue (2 == ids.size())
    Entities.map(ids, { assertNotNull entities.get(it) })
  }

}
