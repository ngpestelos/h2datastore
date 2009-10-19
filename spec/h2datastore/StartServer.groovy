import h2datastore.H2Utils
import org.h2.tools.Server

class StartServer extends GroovyTestCase {
  
  // starting for the first time
  void testStartFirstTime() {
    // before
    def started = 0
    def server = new Expando()
    server.start = { -> started += 1 }
    def created = 0
    Server.metaClass.'static'.createTcpServer = { created += 1; server }

    // when
    def s = H2Utils.startServer()

    // then
    assertNotNull s
    assertTrue (1 == started)
  }

}
