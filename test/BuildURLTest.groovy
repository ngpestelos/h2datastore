import h2datastore.ConnectionMode
import h2datastore.H2Utils

class BuildURLTest extends GroovyTestCase {

  void testBuildInvalidPath() {
    shouldFail { H2Utils.buildURL(null, ConnectionMode.NETWORK) }
    shouldFail { H2Utils.buildURL("", ConnectionMode.MEMORY) }
  }

  void testBuildInvalidMode() {
    shouldFail { H2Utils.buildURL("e:/data/foo", null) }
    shouldFail { H2Utils.buildURL("e:/data/foo", ConnectionMode.SOMETHING) }
  }

  void testBuildNetworkPath() {
    assertTrue  "jdbc:h2:tcp://localhost/e:/data/foo" == H2Utils.buildURL("e:/data/foo", ConnectionMode.NETWORK)
    assertTrue "jdbc:h2:tcp://localhost/e:/data/foo;IFEXISTS=TRUE" == H2Utils.buildURL("e:/data/foo", ConnectionMode.NETWORK, true)
  }

  void testBuildStandalonePath() {
    assertTrue "jdbc:h2:e:/data/foo" == H2Utils.buildURL("e:/data/foo", ConnectionMode.STANDALONE)
    assertTrue "jdbc:h2:e:/data/foo;IFEXISTS=TRUE" == H2Utils.buildURL("e:/data/foo", ConnectionMode.STANDALONE, true)
  }

  void testBuildMemoryPath() {
    assertTrue "jdbc:h2:mem:foo" == H2Utils.buildURL("foo", ConnectionMode.MEMORY)
    assertTrue "jdbc:h2:mem:foo" == H2Utils.buildURL("foo", ConnectionMode.MEMORY, true)
  }

  static void main(args) {
    junit.textui.TestRunner.run(BuildURLTest.class)
  }

}
