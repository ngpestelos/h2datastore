package h2datastore

import java.util.EventListener

interface DatastoreListener extends EventListener {

    void entityUpdated(event)

    void entityDeleted(event)

}

