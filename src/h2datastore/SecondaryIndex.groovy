package h2datastore

// An index for the index
class SecondaryIndex {

    /**
     * @param sql      - Which database?
     * @param index    - Parent index
     * @param property - Which property?
     * @param dataType
     * @param closure  - A closure operating on each entity returned by the index; should insert an entry to the secondary index table
     */
    def SecondaryIndex(sql, index, property, dataType, closure) {

    }
	
}

