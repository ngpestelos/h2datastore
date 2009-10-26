import h2datastore.*
import groovy.sql.Sql

class UpdateEntity extends GroovyTestCase {

  def url
  def sql
  def entities
  def updated

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.newInstance(sql)
    updated = 0
    def listener = new Expando()
    listener.entityAdded = { }
    listener.entityUpdated = { DatastoreEvent e -> updated += 1 }
    entities.addListener(listener)
  }

  void testUpdateOne() {
    // given
    def id = entities.put("some document")
    def doc = entities.get(id)

    // when
    def res = entities.update(id, "updated document")

    // then
    assertTrue (id == res._id)
    assertTrue (res.updated_at > doc.updated_at)

    // ...and a listener should get an event
    assertTrue (1 == updated)
  }

  /*
  void testUpdateNone() {
    // when
    def res = entities.update("abcde", "foo")

    // then
    assertTrue ([:] == res)

    // ...and no events have been sent
    assertTrue (0 == updated)
  }*/

}
