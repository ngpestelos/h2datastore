import groovy.sql.Sql
import org.json.JSONArray
import org.json.JSONObject
import h2datastore.Entities

class EntitiesTest extends GroovyTestCase {

  void testSimplePut() {
    def sql = Sql.newInstance("jdbc:h2:mem:entput")
    def entities = Entities.getInstance(sql)
    entities.put("{\"abc\" : \"def\"}")
    def body = sql.rows("select * from entities")[0].body.characterStream.text
    assertTrue "{\"abc\" : \"def\"}" == body
  }

  void testJsonPut() {
    def sql = Sql.newInstance("jdbc:h2:mem:jsonput")
    def entities = Entities.getInstance(sql)
    def json = new JSONObject()
    json.put("lastname", "Gilmore")
    json.put("firstname", "Happy")
    json.put("sports", ["Hockey", "Golf"])
    def id = entities.put(json.toString())
    def body = sql.rows("select * from entities")[0].body.characterStream.text
    
    def newJson = new JSONObject(body)
    assertTrue "Gilmore" == newJson.get("lastname")
    assertTrue "Happy" == newJson.get("firstname")
    assertTrue (newJson.get("sports") instanceof org.json.JSONArray)
    assertTrue "Hockey" == newJson.get("sports").get(0)
    assertTrue "Golf" == newJson.get("sports").get(1)

    assertTrue id instanceof String
  }

  void testSimpleGet() {
    def sql = Sql.newInstance("jdbc:h2:mem:get")
    assertTrue ([:] == Entities.getInstance(sql).get("1234567890"))
  }

  void testJsonGet() {
    def sql = Sql.newInstance("jdbc:h2:mem:jsonget")
    def json = new JSONObject()
    json.put("type", "item")
    json.put("name", "Nesingwary 4000")
    def ent = Entities.getInstance(sql)
    def id = ent.put(json.toString())
    def res = ent.get(id)
    assertTrue 1 == res.added_id
    assertTrue res._id instanceof java.util.UUID
    assertTrue id == res._id.toString()
    assertTrue res.updated_at instanceof java.sql.Timestamp
    res.body.getClass() instanceof org.h2.jdbc.JdbcClob
    def newJson = new JSONObject(res.body.characterStream.text)
    assertTrue "item" == newJson.get("type")
    assertTrue "Nesingwary 4000" == newJson.get("name")
  }

  void testSimpleRemove() {
    def sql = Sql.newInstance("jdbc:h2:mem:remove")
    assertTrue (0 == Entities.getInstance(sql).remove("abc"))
  }

  void testJsonRemove() {
    def json = new JSONObject()
    json.put("buying", new BigDecimal("100"))
    json.put("selling", new BigDecimal("10000000000.98")) // megabucks
    def sql = Sql.newInstance("jdbc:h2:mem:jsonremove")
    def ent = Entities.getInstance(sql)
    def id = ent.put(json.toString())
    assertTrue (1 == ent.remove(id))
    assertTrue ([:] == ent.get(id))
  }

  void testJsonUpdate() {
    def sql = Sql.newInstance("jdbc:h2:mem:jsonupdate")
    def json = new JSONObject()
    json.put("type", "item")
    json.put("name", "Nesingwary 4000")
    def ent = Entities.getInstance(sql)
    def id = ent.put(json.toString())
    def res = ent.get(id)
    def newJson = new JSONObject(res.body.characterStream.text)
    newJson.put("buying", new BigDecimal("100.00"))
    
    def updated = ent.put(id, newJson.toString())
    assertTrue (id == updated._id)
    assertTrue (res.updated_at < updated.updated_at)
    
    def updatedRes = ent.get(id)
    def updatedJson = new JSONObject(updatedRes.body.characterStream.text)
    assertTrue (100 == updatedJson.get("buying"))
  }

  static void main(args) {
    junit.textui.TestRunner.run(EntitiesTest.class)
  }

}
