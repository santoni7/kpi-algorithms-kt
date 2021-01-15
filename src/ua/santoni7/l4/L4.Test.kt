package ua.santoni7.l4

sealed class HeapType(
    val comparator: Comparator<Int>,
    val defaultValue: Int,
    val name: String
) {
    object Min : HeapType(Comparator.naturalOrder(), Int.MIN_VALUE, "MinHeap")
    object Max : HeapType(Comparator.reverseOrder(), Int.MAX_VALUE, "MaxHeap")
}

class Heap(private val maxsize: Int, private val heapType: HeapType) {
    private val arr: IntArray
    var size: Int = 0
        private set
    private val comparator: Comparator<Int> get() = heapType.comparator

    init {
        this.size = 0
        arr = IntArray(this.maxsize + 1)
        arr[0] = heapType.defaultValue
    }

    // Function to return the position of
    // the parent for the node currently
    // at pos
    private fun parent(pos: Int): Int {
        return pos / 2
    }

    // Function to return the position of the
    // left child for the node currently at pos
    private fun leftChild(pos: Int): Int {
        return 2 * pos
    }

    // Function to return the position of
    // the right child for the node currently
    // at pos
    private fun rightChild(pos: Int): Int {
        return 2 * pos + 1
    }

    // Function that returns true if the passed
    // node is a leaf node
    private fun isLeaf(pos: Int): Boolean {
        return pos > (size / 2) && pos <= size
    }

    private fun exists(pos: Int): Boolean = pos <= size

    // Function to swap two nodes of the heap
    private fun swap(i: Int, j: Int) {
        val tmp = arr[i]
        arr[i] = arr[j]
        arr[j] = tmp
    }

    // Function to heapify the node at pos
    private fun minHeapify(pos: Int) {

        // If the node is a non-leaf node and greater
        // than any of its child
        if (!isLeaf(pos)) {
            if (exists(leftChild(pos)) && comparator.compare(arr[pos], arr[leftChild(pos)]) > 0 ||
                exists(rightChild(pos)) && comparator.compare(arr[pos], arr[rightChild(pos)]) > 0
            ) {
//            if (arr[pos] > arr[leftChild(pos)] || arr[pos] > arr[rightChild(pos)]) {

                // Swap with the left child and heapify
                // the left child
                if (!exists(rightChild(pos)) || comparator.compare(arr[leftChild(pos)], arr[rightChild(pos)]) < 0) {
//                if (arr[leftChild(pos)] < arr[rightChild(pos)]) {
                    swap(pos, leftChild(pos))
                    minHeapify(leftChild(pos))
                } else {//if(exists(rightChild(pos))) {
                    swap(pos, rightChild(pos))
                    minHeapify(rightChild(pos))
                } //else {
                //println("*** ELSE BRANCH ***")// Swap with the right child and heapify
                //}
                // the right child
            }
        }
    }

    // Function to insert a node into the heap
    fun insert(element: Int) {
        if (size >= maxsize) {
            return
        }
        arr[++size] = element
        var current = size

        while (comparator.compare(arr[current], arr[parent(current)]) < 0) {
//        while (arr[current] < arr[parent(current)]) {
            swap(current, parent(current))
            current = parent(current)
        }
    }

    // Function to print the contents of the heap
    fun print() {
        println("========= ${heapType.name} BEGIN ========= ")
        println("Size=$size")
        if (size == 1) println("SINGLE NODE: ${arr[1]}")
        else for (i in 1..size / 2) {
            print(
                " PARENT : " + arr[i]
                        + " LEFT CHILD : " + (if (exists(leftChild(i))) arr[leftChild(i)] else "NULL")
                        + " RIGHT CHILD :" + (if (exists(rightChild(i))) arr[rightChild(i)] else "NULL")
//                " PARENT : " + arr[i]
//                        + " LEFT CHILD : " + arr[2 * i]
//                        + " RIGHT CHILD :" + arr[2 * i + 1]
            )
            println()
        }
        println("========= ${heapType.name} END ========= ")
    }

    // Function to build the min heap using
    // the minHeapify
    fun minHeap() {
        for (pos in (size / 2) downTo 1) {
            minHeapify(pos)
        }
    }

    // Function to remove and return the minimum
    // element from the heap
    fun pop(): Int {
        val popped = arr[FRONT]
        arr[FRONT] = arr[size--]
        minHeapify(FRONT)
        return popped
    }

    fun peek(): Int = arr[FRONT]


    fun asIterable(): Iterable<Int> = arr.copyOfRange(FRONT, size + 1).asIterable()

    companion object {

        private const val FRONT = 1
    }
}

fun main() {
    val min = Heap(1000, HeapType.Min)
    val max = Heap(1000, HeapType.Max)

    for (i in 1..5) {
        min.insert(i)
        max.insert(i)

//        min.print()
        max.print()
    }
    println("\n\nStarting to remove:\n\n")
    for (i in 1..5) {
        val minRemoved = min.pop()
        val maxRemoved = max.pop()

//        println("Removed $minRemoved as top of min heap")
//        min.print()
        println("Removed $maxRemoved as top of min heap")
        max.print()
    }
}