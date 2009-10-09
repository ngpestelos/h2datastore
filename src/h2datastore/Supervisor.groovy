package h2datastore

import org.h2.tools.Server

// Starts/stops the tcp server @ port 9092
class Supervisor {

    def server

    static def instance

    private def Supervisor() {
        server = Server.createTcpServer(null) // TODO options
    }

    static def getInstance() {
        if (!instance)
            instance = new Supervisor()

        instance
    }

    def start() {
        server.start()
    }

    def stop() {
        server.stop()
    }

    def getStatus() {
        server.status
    }

    def isAlive() {
        server.isRunning(false)
    }

    def isAddressInUse() {
        def inUse = false
        try {
            start()
        } catch (e) {
            if (e.getMessage() =~ /port may be in use/)
                inUse = true
        }
        return inUse
    }

    static void main(args) {
        def supervisor = Supervisor.getInstance()
        println supervisor.isAlive()
        //supervisor.start()
        println supervisor.isAddressInUse()
        println supervisor.isAlive()
    }
	
}

