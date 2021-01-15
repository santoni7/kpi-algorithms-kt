package ua.santoni7.l3

import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun readNumber(prompt: String = "Input number: "): Int {
    print(prompt);
    val s = readLine()
    return s?.toInt() ?: throw IllegalStateException("Could not read a number")
}

/**
 * Клас, що інкапсулює обрахування опорного елементу під час процедури [partition] у алгоритмі [quickSort]
 *
 * Всередині наведені досліджувані реалізації обрахування опорного елементу:
 * [Left] - завжди лівий елемент
 * [Right] - завжди правий
 * [Middle] - елемент, індекс якого це середнє арифметичне лівого і правого індексів
 */
sealed class PivotProvider(val name: String) {
    abstract fun getPivot(array: IntArray, leftIndex: Int, rightIndex: Int): Int

    object Left : PivotProvider("LEFT") {
        override fun getPivot(array: IntArray, leftIndex: Int, rightIndex: Int) = array[leftIndex]
    }

    object Right : PivotProvider("RIGHT") {
        override fun getPivot(array: IntArray, leftIndex: Int, rightIndex: Int) = array[rightIndex]
    }

    object Middle : PivotProvider("MIDDLE") {
        override fun getPivot(array: IntArray, leftIndex: Int, rightIndex: Int) = array[(leftIndex + rightIndex) / 2]
    }
}

/**
 * Генерація випадкового масиву
 */
val random = Random(System.currentTimeMillis())

fun generateArray(size: Int, min: Int, max: Int): IntArray = IntArray(size) { random.nextInt(min, max) }

fun IntArray.swapElements(i: Int, j: Int) {
    val tmp = this[j]
    this[j] = this[i]
    this[i] = tmp
}

/**
 * Допоміжний метод для обрахунку кількості порівнянь
 */
inline fun <T> runAndIncrement(counter: AtomicLong, action: () -> T): T {
    counter.incrementAndGet()
    return action.invoke()
}

fun quickSort(array: IntArray, left: Int, right: Int, pivotProvider: PivotProvider, comparsionCounter: AtomicLong) {
    if (left >= right) return
    val index = partition(array, left, right, pivotProvider, comparsionCounter)
    if (left < index - 1 && index - 1 != right) {
        quickSort(array, left, index - 1, pivotProvider, comparsionCounter)
    }
    if (index < right && index != left) {
        quickSort(array, index, right, pivotProvider, comparsionCounter)
    }
}

fun partition(array: IntArray, l: Int, r: Int, pivotProvider: PivotProvider, comparsionCounter: AtomicLong): Int {
    var left = l
    var right = r
    val pivot = pivotProvider.getPivot(array, l, r)
    while (left <= right) {
        while (runAndIncrement(comparsionCounter) { array[left] < pivot }) {
            left++
        }

        while (runAndIncrement(comparsionCounter) { array[right] > pivot }) {
            right--
        }

        if (left <= right) {
            array.swapElements(left, right)
            left++
            right--
        }
    }
    return min(r, max(l, left))
}

/**
 * Допоміжний клас що обраховує кількість порівнянь елементів масиву у окремому потоці
 */
class Worker(
    val pivotProvider: PivotProvider,
    val array: IntArray
) : Thread(pivotProvider.name) {
    val counter = AtomicLong(0)

    override fun run() {
        quickSort(array, 0, array.size - 1, pivotProvider, counter)
    }

    fun joinAndGetResults(): Long {
        join()
        return counter.get()
    }
}

/**
 * Точка входу програми
 */
fun main(args: Array<String>) {
    val results = mutableMapOf(PivotProvider.Left to 0L, PivotProvider.Right to 0L, PivotProvider.Middle to 0L, PivotProvider.Average to 0L)
    val iterationCount = readNumber("Input iteration count: ")
    val arraySize = readNumber("Input array size: ")
    val maxElement = readNumber("Input max element: ")
    for (i in 1..iterationCount) {
        val array = generateArray(arraySize, 0, maxElement)

        val w1 = Worker(PivotProvider.Left, array.copyOf())
        val w2 = Worker(PivotProvider.Middle, array.copyOf())
        val w3 = Worker(PivotProvider.Right, array.copyOf())

        val workers = listOf(w1, w2, w3)
        workers.forEach { it.start() }
        workers.forEach {
            results[it.pivotProvider] = results[it.pivotProvider]!! + it.joinAndGetResults()
        }
        println("Iteration $i finished\n---------")
    }

    val resultsString = results.toList().joinToString(separator = "\n") {
        "${it.first.name} -> ${it.second * 1.0 / iterationCount} comparsions on average"
    }
    println("Results on running $iterationCount iterations with array size of $arraySize:\n$resultsString")
}


