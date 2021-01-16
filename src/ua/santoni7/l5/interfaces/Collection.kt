package ua.santoni7.l5.interfaces

interface Collection<T> {
    fun size(): Int
    fun add(value: T): Boolean
    operator fun contains(value: T): Boolean
}
