package h2datastore

import org.h2.tools.Server
import groovy.sql.Sql

class H2Utils {

    // createDatabase (name, mode, location)

    // destroyDatabase (sql)

    // getSql(name, mode, location)
   

    // see H2 javadoc
    static def startServer(options) {
        def server = Server.createTcpServer(options)
        server.start()
        server
    }

    static def stopServer(server) {
        server.stop()
    }

    static def createDatabase(params) {
        def sql = Sql.newInstance("jdbc:h2:tcp://127.0.0.1:9092//Users/ngpestelos/temp/foo/foodb", "sa", "")
        assert (sql != null)
        sql.executeUpdate("create table entities ( id varchar (254) )")
    }

    static void main(args) {
        def server = startServer()
        println server.status
        //print server.status
        //def sql = Sql.newInstance("jdbc:h2:/Users/ngpestelos/temp/foo/foodb", "sa", "")
        //print sql.rows("select * from information_schema.tables")
        createDatabase([name : "foo", location : "/Users/ngpestelos/temp"])
        //stopServer(server)
        //println server.status
    }

}

