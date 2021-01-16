package ua.santoni7.l5.interfaces

interface Iterator<T> {
    operator fun hasNext(): Boolean
    operator fun next(): T
}
