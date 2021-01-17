package ua.santoni7.l5

import kotlin.collections.List

/**
 * Інтерфейс що інкапсулює хеш-таблицю. Підтримує вставку пари ключ-значення і пошук значення по ключу
 *
 * Див. реалізації [ChainingHashMap] та [OpenAddressHashMap]
 */
interface Map<K, V> {

    fun getEntries(): List<KeyValuePair<K, V>>

    operator fun set(key: K, value: V): Boolean

    operator fun get(key: K): KeyValuePair<K, V>?

    fun size(): Int

    fun countCollisions(): Int
}

class KeyValuePair<K, V>(var key: K, var value: V) {
    override fun toString() = "{ $key = > $value }"
}