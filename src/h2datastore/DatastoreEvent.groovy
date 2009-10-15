package h2datastore

import java.util.EventObject

class DatastoreEvent extends EventObject {

    def id
    def body

    def DatastoreEvent(source, id, body) {
        super(source)
        this.id = id
        this.body = body
    }

    def DatastoreEvent(source, id) {
        super(source)
        this.id = id
        this.body = null
    }

    def DatastoreEvent(source) {
        super(source)
    }

    String toString() {
        "Datastore Event: ${source}"
    }
	
}

