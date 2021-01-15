package ua.santoni7.l4

import kotlin.math.abs

/**
 * Структура що дозволяє обчислити медіану в будь який момент за O(1).
 * Всередині містить дві простих піраміди - [minHeap] та [maxHeap]
 * [maxHeap] - Зберігає усі елементи менші за поточну медіану у максимальну піраміду (тобто корінь структури зберігає найбільше значення)
 * [minHeap] - Зберігає усі елементи більші за поточну медіану у мінімальну піраміду (тобто корінь структури зберігає найменше значення)
 */
class MedianHeap(val capacity: Int = 1000) {
    class Median(val a: Int, val b: Int? = null) {
        override fun toString(): String {
            return if (b != null) "($a; $b)"
            else "($a)"
        }
        // Представити медіану у вигляді десяткового числа. Якщо медіана складається з 2х елементів - повертає їх середнє
        // арифметичше значення
        fun asDouble(): Double = (b?.toDouble()?.plus(a)?.div(2.0)) ?: a.toDouble()
    }

    // Зберігає усі елементи менші за поточну медіану у максимальну піраміду (тобто корінь структури зберігає найбільше значення)
    private val maxHeap = Heap(capacity, HeapType.Max)

    // Зберігає усі елементи більші за поточну медіану у мінімальну піраміду (тобто корінь структури зберігає найменше значення)
    private val minHeap = Heap(capacity, HeapType.Min)

    private val isEmpty: Boolean
        get() = maxHeap.size == 0 && minHeap.size == 0


    /**
     * Вставка числа n у структуру даних. Асимптотична складність зумовлена складністю процедур вставки у просту піраміду
     * [Heap.insert] а також процедури [rebalanceIfNeeded] а тому дорівнює O(log(N)) + O(log(N)) = O(log(N))
     */
    fun insert(n: Int) {
        if (isEmpty) { // Якщо елементів до цього часу не було, додаємо перший елемент у мінімальну піраміду
            minHeap.insert(n)
        } else {
            // Якщо n менше чи рівне поточній медіані, додаємо до максимальної піраміди
            // В іншому разі - до мінімальної
            if (n.toDouble().compareTo(median().asDouble()) <= 0) {
                maxHeap.insert(n)
            } else {
                minHeap.insert(n)
            }
        }
        // Перевірка на збалансованість структури у випадку якщо розмір мінімальної і максимальної пірамід відрізняються
        // більш ніж на одиницю
        rebalanceIfNeeded()
    }

    /**
     * Перевірка на збалансованість структури:
     * у випадку якщо розмір мінімальної і максимальної пірамід відрізняються більш ніж на одиницю, видаляємо найперший елемент
     * з більшої піраміди і додаємо його до меншої. В результаті розмір пірамід буде відрізнятись не більше ніж на 1
     *
     * Асимптотична складність зумовлена складністю операцій вставки і видалення простої піраміди [Heap.insert] та [Heap.pop]
     * і дорівнює O(log(N))+O(log(N)) = O(log(N))
     */
    private fun rebalanceIfNeeded() {
        if (abs(maxHeap.size - minHeap.size) > 1) {
            if (maxHeap.size > minHeap.size) {
                minHeap.insert(maxHeap.pop())
            } else {
                maxHeap.insert(minHeap.pop())
            }
        }
    }

    /**
     * Знаходить поточну медіану. Якщо мін. і макс. піраміди однакового розміру, медіана - це пара чисел (вершини обох пірамід)
     * Якщо одна з пірамід має на один елемент більше ніж інша, медіаною буде вершина цієї піраміди
     *
     * Цей метод не містить циклічних операцій тому його складність O(1)
     */
    fun median(): Median {
        return when {
            maxHeap.size == minHeap.size -> Median(maxHeap.peek(), minHeap.peek())
            maxHeap.size > minHeap.size -> Median(maxHeap.peek())
            else -> Median(minHeap.peek())
        }
    }

    /**
     * Представити структуру у вигляді рядка
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("\n Median for the numbers : ")
        for (i in maxHeap.asIterable()) {
            sb.append(" $i")
        }
        for (i in minHeap.asIterable()) {
            sb.append(" $i")
        }
        sb.append(" is " + median() + "\n")
        return sb.toString()
    }
}