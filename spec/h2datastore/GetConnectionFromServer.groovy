import h2datastore.H2Utils
import groovy.sql.Sql

class GetConnectionFromServer extends GroovyTestCase {

  // server up
  void testServerUp() {
    // when
    def s = H2Utils.startServer()

    // then
    assertNotNull s
    assertNotNull Sql.newInstance("jdbc:h2:tcp://localhost//tmp/foo", "sa", "")

    H2Utils.stopServer(s)
  }

  void testServerDown() {
    shouldFail { Sql.newInstance("jdbc:h2:tcp://localhost//tmp/foo", "sa", "") }
  }

}
