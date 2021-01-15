package ua.santoni7.l4

import kotlin.math.abs

class MedianHeap(val capacity: Int = 1000) {
    class Median(val a: Int, val b: Int? = null) {
        override fun toString(): String {
            return if (b != null) "($a; $b)"
            else "($a)"
        }

        fun asDouble(): Double = (b?.toDouble()?.plus(a)?.div(2.0)) ?: a.toDouble()
    }

    // Зберігає усі елементи менші за поточну медіану у максимальну піраміду (тобто корінь структури зберігає найбільше значення)
    private val maxheap = Heap(capacity, HeapType.Max)

    // Зберігає усі елементи більші за поточну медіану у мінімальну піраміду (тобто корінь структури зберігає найменше значення)
    private val minheap = Heap(capacity, HeapType.Min)

    private val isEmpty: Boolean
        get() = maxheap.size == 0 && minheap.size == 0


    /**
     * Вставка числа n у структуру даних.
     */
    fun insert(n: Int) {
        if (isEmpty) { // Якщо елементів до цього часу не було, додаємо перший елемент у мінімальну піраміду
            minheap.insert(n)
        } else {
            // Якщо n менше чи рівне поточній медіані, додаємо до максимальної піраміди
            // В іншому разі - до мінімальної
            if (n.toDouble().compareTo(median().asDouble()) <= 0) {
                maxheap.insert(n)
            } else {
                minheap.insert(n)
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
     */
    private fun rebalanceIfNeeded() {
        if (abs(maxheap.size - minheap.size) > 1) {
            if (maxheap.size > minheap.size) {
                minheap.insert(maxheap.pop())
            } else {
                maxheap.insert(minheap.pop())
            }
        }
    }

    /**
     * Знаходить поточну медіану. Якщо мін. і макс. піраміди однакового розміру, медіана - пара чисел (вершини обох пірамід)
     * Якщо одна з пірамід має на один елемент більше ніж інша, медіаною буде вершина цієї піраміди
     */
    fun median(): Median {
        return when {
            maxheap.size == minheap.size -> Median(maxheap.peek(), minheap.peek())
            maxheap.size > minheap.size -> Median(maxheap.peek())
            else -> Median(minheap.peek())
        }
    }

    /**
     * Представити структуру у вигляді рядка
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("\n Median for the numbers : ")
        for (i in maxheap.asIterable()) {
            sb.append(" $i")
        }
        for (i in minheap.asIterable()) {
            sb.append(" $i")
        }
        sb.append(" is " + median() + "\n")
        return sb.toString()
    }
}