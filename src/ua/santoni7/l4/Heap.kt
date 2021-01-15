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

    // Індекс батьківської вершини
    private fun parent(pos: Int): Int {
        return pos / 2
    }

    // Індекс лівої дочірньої вершини
    private fun leftChild(pos: Int): Int {
        return 2 * pos
    }

    // Індекс правої дочірньої вершини
    private fun rightChild(pos: Int): Int {
        return 2 * pos + 1
    }

    // Перевірка чи є вершина листом (тобто не має дочірніх вершин)
    private fun isLeaf(pos: Int): Boolean {
        return pos > (size / 2) && pos <= size
    }

    // Перевірка чи існує вершина
    private fun exists(pos: Int): Boolean = pos <= size

    // Поміняти місцями вершини i та j
    private fun swap(i: Int, j: Int) {
        val tmp = arr[i]
        arr[i] = arr[j]
        arr[j] = tmp
    }

    // Нормалізація піраміди починаючи з вершини pos і нижче за структурою
    // У цій процедурі виконується перевірка дотримання умов піраміди (чи то мін. піраміда у якоі вершина завжди менша за
    // дочірні вершини, чи навпаки)
    private fun heapify(pos: Int) {
        if (!isLeaf(pos)) {
            if (exists(leftChild(pos)) && comparator.compare(arr[pos], arr[leftChild(pos)]) > 0 ||
                exists(rightChild(pos)) && comparator.compare(arr[pos], arr[rightChild(pos)]) > 0
            ) {
                if (!exists(rightChild(pos)) || comparator.compare(arr[leftChild(pos)], arr[rightChild(pos)]) < 0) {
                    swap(pos, leftChild(pos))
                    heapify(leftChild(pos))
                } else {
                    swap(pos, rightChild(pos))
                    heapify(rightChild(pos))
                }
            }
        }
    }

    // Вставка елементу у піраміду. Спочатку вона ставиться на останнє місце, після чого піднімається вгору допоки
    // не буде виконана умова піраміди
    fun insert(element: Int) {
        if (size >= maxsize) {
            return
        }
        arr[++size] = element
        var current = size

        while (comparator.compare(arr[current], arr[parent(current)]) < 0) {
            swap(current, parent(current))
            current = parent(current)
        }
    }

    // Вивід піраміди у консоль (використовується для відлагодження програми)
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

    // Видаляє і повертає вершину піраміди
    fun pop(): Int {
        val popped = arr[FRONT]
        arr[FRONT] = arr[size--]
        heapify(FRONT)
        return popped
    }

    // Повертає поточну вершину
    fun peek(): Int = arr[FRONT]


    fun asIterable(): Iterable<Int> = arr.copyOfRange(FRONT, size + 1).asIterable()

    companion object {
        private const val FRONT = 1
    }
}