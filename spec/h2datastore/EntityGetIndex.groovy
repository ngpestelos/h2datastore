import h2datastore.*
import groovy.sql.Sql

import org.json.JSONObject

class EntityGetIndex extends GroovyTestCase {

  def entities

  void setUp() {
    def url = H2Utils.buildMemoryURL()
    def sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
  }

  void testEmpty() {
    // when
    def index = entities.getIndex("some_property")

    // then
    assertNull index.find("foo") 
  }

  void testNotEmpty() {
    // given
    def index = entities.getIndex("name")
    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("category", "abc")
    entities.put(doc)

    // when
    def res = index.find("foo")

    // then
    assertNotNull res
  }

  void testNoPropertiesMatching() {
    // given
    def index = entities.getIndex("supplier")
    def doc = new JSONObject()
    doc.put("name", "foo")
    doc.put("category", "abc")
    entities.put(doc)

    // when
    def res = index.find("bar")

    // then
    assertNull res
  }

}
