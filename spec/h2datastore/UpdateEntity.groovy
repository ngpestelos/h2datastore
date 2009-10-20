import h2datastore.Entities
import h2datastore.H2Utils
import groovy.sql.Sql

class UpdateEntity extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
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
  }

  void testUpdateNone() {
    // when
    def res = entities.update("abcde", "foo")

    // then
    assertTrue ([:] == res)
  }

}
