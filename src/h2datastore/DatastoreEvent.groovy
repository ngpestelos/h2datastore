package h2datastore

import java.util.EventObject

class DatastoreEvent extends EventObject {

    def entity

    def DatastoreEvent(source, entity) {
        super(source)
        this.entity = entity
    }

    String toString() {
        "Datastore Event: ${source}"
    }
	
}

