import junit.framework.TestResult

class Suite extends groovy.util.GroovyTestSuite {

  def add(test) {
    addTestSuite(compile("${test}.groovy"))
  }

  def start() {
    println "Running tests..."

    def result = new TestResult()
    run(result)

    println "*** Tests Completed *** "
    println """T: ${result.runCount()} F: ${result.failureCount()} E: ${result.errorCount()}"""

    if (result.failureCount() > 0) {
      println "*** Failures ***"
      result.failures().each { println it }
    }

    if (result.errorCount() > 0) {
      println "*** Errors ***"
      result.errors().each { println it }
    } 
  }

  static void main(args) {
    def suite = new Suite()
    suite.add("test/IndexTest")
    suite.add("test/EntitiesTest")
    suite.add("test/IndexTablePopulateTest")
    suite.add("test/IndexEqualityTest")
    suite.add("test/EntitiesListenerTest")
    suite.start()
  }

}
