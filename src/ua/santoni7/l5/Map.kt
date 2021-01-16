package ua.santoni7.l5

import kotlin.collections.List

/**
 * Інтерфейс що інкапсулює хеш-таблицю. Підтримує вставку пари ключ-значення і пошук значення по ключу
 *
 * Див. реалізації [ListHashMap] та [OpenAddressHashMap]
 */
interface Map<K, V> {

    fun getEntries(): List<KeyValuePair<K, V>>?
    operator fun set(key: K, value: V): Boolean

    operator fun get(key: K): KeyValuePair<K, V>?

    fun size(): Int
}