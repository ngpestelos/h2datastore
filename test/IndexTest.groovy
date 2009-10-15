import groovy.sql.Sql

import h2datastore.Entities
import h2datastore.Index

import java.sql.Types

class IndexTest extends GroovyTestCase {

  def sql
  def ent

  void setUp() {
    sql = Sql.newInstance("jdbc:h2:mem:")
    ent = Entities.getInstance(sql)
  }
  
  void testPut() {
    def id = ent.put("{\"name\" : \"Nesingwary 4000\"}")
    def index = new Index(sql, "name")
    assertTrue (1 == index.put("Nesingwary 4000", id))
  }

  void testFind() {
    def id = ent.put("{\"name\" : \"Nesingwary 4000\"}")
    def index = new Index(sql, "name")
    index.put("Nesingwary 4000", id)
    
    assertTrue (1 == index.find("Nesingwary 4000").size())

    ent.put("{\"name\" : \"Copper Ore\"}")
    assertTrue (1 == index.find("Copper Ore").size())

    index.put("Nesingwary 4000XP", id)
    assertTrue (1 == index.find("Nesingwary 4000XP").size())
    assertTrue (0 == index.find("Nesingwary 4000").size())
  }

  void testGetIndexes() {
    def id = ent.put("{\"name\" : \"Nesingwary 4000\", \"category\" : \"Guns\"}")
    def nameIndex = new Index(sql, "name")
    def categoryIndex = new Index(sql, "category")

    assertTrue (2 == Index.getIndices(sql).size())
    assertTrue ("index_name" in Index.getIndices(sql))
    assertTrue ("index_category" in Index.getIndices(sql))
  }

  void testNonExistentProperty() {
    def id = ent.put("{\"name\" : \"Nesingwary 4000\"}")
    def fooIndex = new Index(sql, "foo")
    assertTrue (0 == fooIndex.count())
  }

  void testInvalidDataType() {
    shouldFail { new Index(sql, "document_date", Types.INTEGER) }
  }

  void testValidDataType() {
    assertNotNull (new Index(sql, "document_date", Types.TIMESTAMP))
    assertNotNull (new Index(sql, "name", Types.VARCHAR))
  }

  static void main(args) {
    junit.textui.TestRunner.run(IndexTest.class)
  }

}
