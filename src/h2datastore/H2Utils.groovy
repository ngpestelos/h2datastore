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

    /**
     * @param url
     * @param username
     * @param password
     */
    static Boolean testConnection(String url, String username, String password) {
        def sql

        try {
            sql = Sql.newInstance(url, username, password)
        } catch (e) {
            return false
        }
        
        return (sql != null)
    }
}