import h2datastore.Entities 
import h2datastore.Index

import groovy.sql.Sql

class EntitiesListenerTest extends GroovyTestCase {

  def entities
  def sql

  void testAddListener() {
    sql = Sql.newInstance("jdbc:h2:mem:listen")
    entities = Entities.getInstance(sql)
    def index = new Index(sql, "name")
    entities.addListener(index)
    assertTrue (1 == entities.listeners.size())
    entities.addListener(index)
    assertTrue (1 == entities.listeners.size())
  }

  void testEntityAdded() {
    sql = Sql.newInstance("jdbc:h2:mem:add")
    entities = Entities.getInstance(sql)
    def index = new Expando()
    def added = 0
    index.entityAdded = { added += 1 }
    entities.addListener(index) 
    entities.put("{\"name\" : \"Nesingwary 4000\" }")
    assertTrue (1 == added)
  }

  void testEntityUpdated() {
    sql = Sql.newInstance("jdbc:h2:mem:update")
    entities = Entities.getInstance(sql)
    def index = new Expando()
    def updated = 0
    def added = 0
    index.entityAdded = { added += 1 }
    index.entityUpdated = { updated += 1 }
    entities.addListener(index)
    def id = entities.put("{\"name\" : \"Nesingwary 4000\" }")
    entities.put(id, "{\"name\" : \"Nesingwary 4000XP\" }")
    assertTrue (1 == added)
    assertTrue (1 == updated)
  }

  void testEntityRemoved() {
    sql = Sql.newInstance("jdbc:h2:mem:remove")
    entities = Entities.getInstance(sql)
    def index = new Expando()
    def added = 0
    def removed = 0
    index.entityAdded = { added += 1 }
    index.entityRemoved = { removed += 1 }
    entities.addListener(index)
    def id = entities.put("{\"name\" : \"Nesingwary 4000\" }")
    entities.remove(id)
    assertTrue (1 == added)
    assertTrue (1 == removed)
  }

  static void main(args) {
    junit.textui.TestRunner.run(EntitiesListenerTest.class)
  }  

}
