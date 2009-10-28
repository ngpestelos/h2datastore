import h2datastore.*

class TestConnection extends GroovyTestCase {

  void testNormalURL() {
    // given
    def url = H2Utils.buildStandaloneURL("foo")
    
    // when
    def res = H2Utils.testConnection(url, "sa", "")

    // then
    assertTrue res
  }

  void testNullURL() {
    // when
    def res = H2Utils.testConnection(null, "sa", "")

    // then
    assertFalse res
  }

  void testEmptyURL() {
    // when
    def res = H2Utils.testConnection("", "sa", "")

    // then
    assertFalse res
  }

  void testOfflineURL() {
    // given
    def url = H2Utils.buildNetworkURL("foo")

    // when
    def res = H2Utils.testConnection(url, "sa", "")

    // then
    assertFalse res
  }
}
