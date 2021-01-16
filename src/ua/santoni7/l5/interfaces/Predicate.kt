package ua.santoni7.l5.interfaces

interface Predicate<T> {
    fun apply(value: T): Boolean
}
