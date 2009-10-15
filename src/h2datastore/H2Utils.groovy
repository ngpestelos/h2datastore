package h2datastore

import org.h2.tools.Server
import groovy.sql.Sql

/**
 * Database helper functions
 *
 */
class H2Utils {

    /**
     * Assembles a URL based on connection mode
     *
     * Supports the following modes: network, standalone, and memory
     *
     * Note: Network mode assumes localhost
     *
     * @param path (e.g. e:/data/somedb)
     * @param mode (see ConnectionMode)
     * @param isExisting (defaults to false => H2 creates a new database if it is not found in the path)
     *
     * @return String the connection URL
     */
    static String buildURL(path, mode, Boolean isExisting = false) {
        if (!path)
            throw new IllegalArgumentException("Please specify a path")
        if (!(mode in ConnectionMode))
            throw new IllegalArgumentException("Please specify a connection mode. See h2datastore.ConnectionMode.")

        def host = "localhost"
        def url = ""

        if (mode == ConnectionMode.NETWORK)
            url = "jdbc:h2:tcp://localhost/${path}"
        else if (mode == ConnectionMode.STANDALONE)
            url = "jdbc:h2:${path}"
        else if (mode == ConnectionMode.MEMORY)
            url = "jdbc:h2:mem:${path}"
        else
            throw new IllegalArgumentException("Please see h2datastore.ConnectionMode for valid connection modes.")

        if (isExisting && (mode != ConnectionMode.MEMORY))
            url += ";IFEXISTS=TRUE"

        return url
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