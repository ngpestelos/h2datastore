package h2datastore

import org.h2.tools.Server
import groovy.sql.Sql

/**
 * Database helper functions
 *
 */
class H2Utils {

    static String buildMemoryURL(path = "") {
        return "jdbc:h2:mem:${path}"
    }

    static String buildStandaloneURL(path, Boolean isExisting = false) {
        if (!path)
            throw new IllegalArgumentException("Please specify a path")

        def url = "jdbc:h2:${path}"

        isExisting ? "${url};IFEXISTS=TRUE" : url
    }

    static String buildNetworkURL(path, Boolean isExisting = false) {
        if (!path)
            throw new IllegalArgumentException("Please specify a path")

        def url = "jdbc:h2:tcp://localhost/${path}"

        isExisting ? "${url};IFEXISTS=TRUE" : url
    }

    // Creates a H2 Server instance and tries to start it
    // See H2 documentation
    // @return server
    static def startServer(options) {
        def server = Server.createTcpServer(options)
        server.start()
        server
    }

    // Stops a H2 Server
    // @param server
    static def stopServer(server) {
        server.stop()
    }

}