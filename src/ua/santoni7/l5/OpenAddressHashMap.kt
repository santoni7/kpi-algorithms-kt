package ua.santoni7.l5


import ua.santoni7.l5.interfaces.HashProvider
import ua.santoni7.l5.interfaces.List
import ua.santoni7.l5.interfaces.Map


class OpenAddressHashMap<K, V>(
    private val hashProvider: HashProvider<K> = HashProvider.createDefault(),
    initCapacity: Int = 512
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
            oldEntries[i]?.let { put(it.keyValuePair) }
        }
    }

    /**
     * Calculate index in Entries array based on item's hash provided by [hashProvider]
     */
    private fun indexFor(item: K, capacity: Int): Int {
        return hashProvider.hashFor(item, capacity)
    }

    override fun put(key: K, value: V): Boolean {
        return put(KeyValuePair(key, value))
    }

    fun put(pair: KeyValuePair<K, V>): Boolean {
        if (thresholdSize())
            enlarge()

        var index = indexFor(pair.key, capacity)

        while(mEntries[index] != null && mEntries[index]?.isDeleted != true && mEntries[index]?.key != pair.key)
            index = (index + 1) % capacity
        mEntries[index] = Entry(pair)
        return true
    }

    override fun get(key: K): KeyValuePair<K, V>? {
        var index = indexFor(key, capacity)
        var c = 0
        while(mEntries[index]?.key != key && c < capacity){
            index = (index + 1) % capacity
            c++
        }
        return if(mEntries[index]?.key == key) mEntries[index]?.keyValuePair else null
    }

    override fun size(): Int {
        return size
    }

    override fun getEntries(): List<KeyValuePair<K, V>> {
        val list = LinkedList<KeyValuePair<K, V>>()
        for (i in 0 until capacity) {
            mEntries[i]?.keyValuePair?.let { list.add(it) }
        }
        return list
    }

    // Indicates whether capacity should be enlarged
    private fun thresholdSize(): Boolean {
        return size >= capacity * loadFactor
    }


    internal inner class Entry(val keyValuePair: KeyValuePair<K, V>) {
        val key get() = keyValuePair.key
        val value get() = keyValuePair.value

        var isDeleted = false
    }

    companion object {
        private val loadFactor = 0.75f
    }

}