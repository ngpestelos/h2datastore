import h2datastore.*
import groovy.sql.Sql
import org.json.JSONObject

class EntityCopy extends GroovyTestCase {

  def sql
  def entities
  def json

  void setUp() {
    sql = Sql.newInstance(H2Utils.buildMemoryURL(), "sa", "")
    entities = Entities.newInstance(sql)
    json = new JSONObject()
    json.put("name", "Nesingwary 4000")
    entities.put(json)
  }

  void testCopy() {
    def destSql = Sql.newInstance(H2Utils.buildMemoryURL("dest"), "sa", "")
    Entities.initialize(destSql)

    // when
    entities.copy(destSql) 

    // then
    def res = destSql.rows("select * from entities")
    assertTrue (1 == res.size())
  }

}
