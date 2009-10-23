import junit.framework.TestResult

class Suite extends groovy.util.GroovyTestSuite {
  
  def add(test) {
    addTestSuite(compile("${test}.groovy"))
  }

  // TODO Machine-readable results
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
    suite.add("spec/h2datastore/StandaloneURL")
    suite.add("spec/h2datastore/MemoryURL")
    suite.add("spec/h2datastore/NetworkURL")
    suite.add("spec/h2datastore/GetConnectionFromServer")
    suite.add("spec/h2datastore/EmptyEntities")
    suite.add("spec/h2datastore/PutEntity")
    suite.add("spec/h2datastore/UpdateEntity")
    suite.add("spec/h2datastore/RemoveEntity")
    suite.add("spec/h2datastore/GetIndexOnProperty")
    suite.add("spec/h2datastore/PutIndex")
    suite.add("spec/h2datastore/RemoveIndex")
    suite.add("spec/h2datastore/UpdateIndex")
    suite.add("spec/h2datastore/GetTimestampIndex")
    suite.add("spec/h2datastore/AcceptsJSON")
    suite.start()
  }
}
