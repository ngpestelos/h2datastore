import h2datastore.Entities
import h2datastore.H2Utils
import groovy.sql.Sql
import org.json.JSONObject

class PutEntity extends GroovyTestCase {

  def url
  def sql
  def entities
  def added

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
    added = 0
    def listener = new Expando()
    listener.entityAdded = { added += 1 }
    entities.addListener(listener)
  }

  void testPutOne() {
    // given
    def doc = "some document"

    // when I put an Entity
    def id = entities.put(doc)

    // then I should be able to retrieve it
    def res = entities.get(id)
    assertTrue (id == res._id)
    assertTrue ("some document" == res.body)

    // ...and a listener should get an event
    assertTrue (1 == added)
  }

  void testPutTwo() {
    // given
    def first_doc = new JSONObject()
    first_doc.put("name", "Nesingwary 4000")
    def second_doc = new JSONObject()
    second_doc.put("name", "Fel Iron Musket")

    // when I put both Entities
    def first_id = entities.put(first_doc.toString())
    def second_id = entities.put(second_doc.toString())

    // then I should be able to retrieve both
    def first_res = entities.get(first_id)
    assertTrue (first_id == first_res._id)
    def second_res = entities.get(second_id)
    assertTrue (second_id == second_res._id)

    // ...and a listener should get two events
    assertTrue (2 == added)
  }

  void testPutNone() {
    // when I try to get using a bogus id
    def res = entities.get("blah")

    // then I should get an empty map
    assertTrue ([:] == res)
  }

}
