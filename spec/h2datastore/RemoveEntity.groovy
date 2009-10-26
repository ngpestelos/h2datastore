import h2datastore.Entities
import h2datastore.H2Utils
import groovy.sql.Sql

class RemoveEntity extends GroovyTestCase {

  def url
  def sql
  def entities
  def removed

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    removed = 0
    def listener = new Expando()
    listener.entityAdded = { }
    listener.entityRemoved = { removed += 1 }
    entities.addListener(listener)
  }

  void testRemoveOne() {
    // given
    def id = entities.put("some document") 

    // when
    def res = entities.remove(id)

    // then
    assertTrue (1 == res)
    assertTrue ([:] == entities.get(id))

    // ...and a listener should get an event
    assertTrue (1 == removed)
  }

  void testRemoveNone() {
    // when
    def res = entities.remove("foo")

    // then
    assertTrue (0 == res)

    // ...and no events were received
    assertTrue (0 == removed)
  }

}
