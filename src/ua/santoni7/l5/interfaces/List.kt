package ua.santoni7.l5.interfaces

interface List<T> : Collection<T>, Iterable<T> {
    // Select all elements, to which predicate apply
    fun selectAll(predicate: Predicate<T>): List<T>
}
