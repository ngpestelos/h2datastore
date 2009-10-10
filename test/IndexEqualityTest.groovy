import h2datastore.Entities
import h2datastore.Index

import groovy.sql.Sql

class IndexEqualityTest extends GroovyTestCase {

  def sql
  def entities

  void testEquals() {
    def sql = Sql.newInstance("jdbc:h2:mem:indexeq")
    def entities = Entities.getInstance(sql) // will create entities table
    def one = new Index(sql, "foo")
    def two = new Index(sql, "bar")
    assertTrue (sql == sql)
    assertTrue (one == one)
    assertFalse (one == two)
  }

  static void main(args) {
    junit.textui.TestRunner.run(IndexEqualityTest.class)
  }

}
