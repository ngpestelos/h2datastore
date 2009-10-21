import h2datastore.H2Utils
import h2datastore.Entities
import groovy.sql.Sql
import org.json.JSONObject
import java.sql.Types

class CreateIndexTable extends GroovyTestCase {

  def url
  def sql
  def entities

  void setUp() {
    url = H2Utils.buildMemoryURL()
    sql = Sql.newInstance(url, "sa", "")
    entities = Entities.getInstance(sql)
  }

  void testCreateIndex() {
    // when
    def first_index = entities.createIndex("name", Types.VARCHAR)
    def second_index = entities.createIndex("name", Types.VARCHAR)   
    def third_index = entities.createIndex("document_date", Types.TIMESTAMP)
    def fourth_index = entities.createIndex("document_date", Types.TIMESTAMP)
 
    // then
    assertTrue (first_index == second_index)
    assertTrue (third_index == fourth_index) 
  }

}
