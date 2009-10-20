import groovy.sql.Sql
import h2datastore.Entities
import h2datastore.H2Utils

class EmptyEntities extends GroovyTestCase {

  // initial database, no rows in entries
  void testNoRows() {
    // given
    def url = H2Utils.buildMemoryURL()

    // when
    def sql = Sql.newInstance(url, "sa", "")
    def ent = Entities.getInstance(sql)

    // then
    def rows = sql.rows("select * from information_schema.tables where table_name = ?", ["ENTITIES"])
    assertTrue (1 == rows.size())

    def data = sql.rows("select * from entities") 
    assertTrue (0 == data.size())
  }

}
