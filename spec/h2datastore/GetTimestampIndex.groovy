import h2datastore.H2Utils
import h2datastore.Entities
import groovy.sql.Sql
import org.json.JSONObject

class GetTimestampIndex extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testEmpty() {
    // when
    def index = entities.getIndex("some_property", true)
    Thread.sleep(100)

    // then
    def now = new Date()
    assertNull index.find(now.getTime())
  }

  void testNotEmpty() {
    // given
    def doc_date = new Date("February 12, 2009")
    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("document_date", doc_date.time)
    entities.put(doc.toString())

    // when
    def index = entities.getIndex("document_date", true)
    Thread.sleep(100)

    // then
    def res = index.find(doc_date.time)
    assertNotNull res
  }

}
