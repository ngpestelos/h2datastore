import h2datastore.*
import groovy.sql.Sql

class EmptyEntities extends GroovyTestCase {

  def sql

  void setUp() {
    sql = Sql.newInstance(H2Utils.buildMemoryURL(), "sa", "")
  }

  void testFailOnGetInstance() {
    shouldFail { Entities.getInstance() }   
  }

  void testWipeInstance() {
    def e1 = Entities.newInstance(sql)
    def e2 = Entities.newInstance(sql)

    assertTrue (e1 != e2)
  }

  void testGetInstance() {
    def e = Entities.newInstance(sql)
    def f = Entities.getInstance()

    assertTrue (e == f)
  }

  void testNoRows() {
    // given
    def e = Entities.newInstance(sql)

    // when
    def res = sql.firstRow("select * from information_schema.tables where table_name = ?", ["ENTITIES"])

    // then
    assertNotNull res
  }

}
