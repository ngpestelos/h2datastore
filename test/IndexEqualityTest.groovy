import h2datastore.Entities
import h2datastore.Index

import groovy.sql.Sql

class IndexEqualityTest extends GroovyTestCase {

  def sql
  def entities

  void setUp() {
    sql = Sql.newInstance("jdbc:h2:mem:eq")
    Entities.createTable()
  }

  void testEquals() {
    def one = new Index(sql, "foo")
    def two = new Index(sql, "bar")
    assertTrue (sql == sql)
  }

  static void main(args) {
    junit.textui.TestRunner.run(IndexEqualityTest.class)
  }

}
