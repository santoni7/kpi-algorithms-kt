package ua.santoni7.l5


import ua.santoni7.l5.interfaces.List
import ua.santoni7.l5.interfaces.Map


class ListHashMap<K, V>(
    initCapacity: Int = 256
) : Map<K, V> {
    private var capacity = initCapacity
    private var size = 0

    //Each element is a head of linked list of Entries
    private var mEntries: Array<Entry?>

    init {
        mEntries = Array(capacity) { null }
    }

    // Double capacity and reinsert all entries at new positions
    private fun enlarge() {
        val oldEntries = mEntries
        size = 0
        capacity *= 2

        mEntries = Array(capacity) { null }
        for (i in 0 until capacity / 2) {
            var e: Entry? = oldEntries[i]
            while (e != null) {
                put(e.keyValuePair)
                e = e.next()
            }
        }
    }

    // index in Entries array
    private fun indexFor(hash: Int, capacity: Int): Int {
        return hash and capacity - 1
    }

    override fun put(key: K, value: V): Boolean {
        return put(KeyValuePair(key, value))
    }

    fun put(pair: KeyValuePair<K, V>): Boolean {
        if (thresholdSize())
            enlarge()

        val index = indexFor(pair.key.hashCode(), capacity)
        val entry = mEntries[index]
        if (entry == null) {
            mEntries[index] = Entry(pair)
        } else {
            val kvp = entry.find(pair.key)
            if (kvp == null)
            //Key is not in the table
                entry.append(pair)
            else
            // Key already exists, so change value
                kvp.value = pair.value
        }
        return true
    }

    override fun get(key: K): KeyValuePair<K, V>? {
        val index = indexFor(key.hashCode(), capacity)
        return mEntries[index]?.find(key)
    }

    override fun size(): Int {
        return size
    }

    override fun getEntries(): List<KeyValuePair<K, V>> {
        val list = LinkedList<KeyValuePair<K, V>>()
        for (i in 0 until capacity) {
            var e: Entry? = mEntries[i]
            while (e != null) {
                list.add(e.keyValuePair)
                e = e.next()
            }
        }
        return list
    }

    // Indicates whether capacity should be enlarged
    private fun thresholdSize(): Boolean {
        return size >= capacity * loadFactor
    }

    internal inner class Entry(var keyValuePair: KeyValuePair<K, V>) {
        var nextEntry: Entry? = null

        operator fun next(): Entry? {
            return nextEntry
        }

        // Append to list
        fun append(pair: KeyValuePair<K, V>) {
            if (nextEntry == null) {
                nextEntry = Entry(pair)
            } else {
                nextEntry!!.append(pair)
            }
        }

        // Recursively find key in list
        fun find(key: K): KeyValuePair<K, V>? {
            if (keyValuePair.key?.equals(key) == true)
                return keyValuePair
            return if (next() != null) next()!!.find(key) else null
        }
    }

    companion object {
        private val loadFactor = 0.75f
    }

}