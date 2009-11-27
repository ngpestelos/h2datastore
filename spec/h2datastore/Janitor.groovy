import h2datastore.*
import groovy.sql.Sql

import org.json.JSONObject

class JanitorSpec extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)

    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("type", "something")
    entities.put(doc)
  }

  void testClean() {
    // given
    def index = entities.getIndex("name")
    assertTrue (1 == index.size())

    // when
    Janitor.clean()

    // then
    assertFalse (index.tableExists())
    shouldFail { index.size() }
  }

}
