package ua.santoni7.l5

import ua.santoni7.l5.interfaces.Iterator
import ua.santoni7.l5.interfaces.Predicate
import ua.santoni7.l5.interfaces.List

class LinkedList<T> : List<T> {

    private val head: Node
    private var size: Int = 0

    init {
        head = Node(null)
    }

    override fun size(): Int {
        return size
    }

    override fun add(value: T): Boolean {
        head.append(value)
        size++
        return true
    }

    override fun contains(value: T): Boolean {
        val it = iterator()
        while (it.hasNext()) {
            if (it.next() == value) return true
        }
        return false
    }

    override fun iterator(): Iterator<T> {

        return LinkedListIterator(head)
    }

    // Returns list of elements from current list, for which predicate is true
    override fun selectAll(predicate: Predicate<T>): List<T> {
        val res = LinkedList<T>()
        val it = iterator()

        while (it.hasNext()) {
            val value = it.next()
            if (predicate.apply(value))
                res.add(value)
        }
        return res
    }

    // A node of linked list
    internal inner class Node(var value: T?) {
        var next: Node? = null

        // Find the end of list and add new value
        fun append(value: T) {
            if (next == null) {
                next = Node(value)
            } else {
                val it = LinkedListIterator(this)
                while (it.hasNext())
                    it.next()
                it.currentNode!!.next = Node(value)
            }
        }
    }

    internal inner class LinkedListIterator(var currentNode: Node?) : Iterator<T> {

        override fun hasNext(): Boolean {
            return currentNode!!.next != null
        }

        override fun next(): T {
            currentNode = currentNode!!.next
            return currentNode!!.value!!
        }
    }
}
