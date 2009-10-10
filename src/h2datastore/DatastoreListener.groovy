package h2datastore

import java.util.EventListener

interface DatastoreListener extends EventListener {

    void entityAdded(event)

    void entityUpdated(event)

    void entityRemoved(event)

}

