import h2datastore.H2Utils

class NetworkURL extends GroovyTestCase {

  void testBuildURL_1() {
    // given
    def path = "/tmp/foodb"
    def exists = true

    // when
    def url = H2Utils.buildNetworkURL(path, exists)

    // then
    assertTrue ("jdbc:h2:tcp://localhost//tmp/foodb;IFEXISTS=TRUE" == url)
  }

  void testBuildURL_2() {
    // given
    def path = "/tmp/bardb"
    def exists = false

    // when
    def url = H2Utils.buildNetworkURL(path, exists)

    // then
    assertTrue ("jdbc:h2:tcp://localhost//tmp/bardb" == url)
  }

}
