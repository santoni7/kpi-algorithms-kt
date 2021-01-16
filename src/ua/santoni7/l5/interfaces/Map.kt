package ua.santoni7.l5.interfaces

import ua.santoni7.l5.KeyValuePair

interface Map<K, V> {

    fun getEntries(): List<KeyValuePair<K, V>>?
    fun put(key: K, value: V): Boolean

    operator fun get(key: K): KeyValuePair<K, V>?

    fun size(): Int
}
