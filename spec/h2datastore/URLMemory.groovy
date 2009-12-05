import h2datastore.H2Utils

class URLMemory extends GroovyTestCase {

  void testBuildURL_1() {
    // given
    def path = "foo"

    // when
    def url = H2Utils.buildMemoryURL(path)

    // then
    assertTrue ("jdbc:h2:mem:foo" == url)
  }

  void testBuildURL_2() {
    // when
    def url = H2Utils.buildMemoryURL()

    // then
    assertTrue ("jdbc:h2:mem:" == url)
  }

}
