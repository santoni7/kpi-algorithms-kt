package ua.santoni7.l2
import java.util.*
import kotlin.Comparator

/**
 * Допоміжна функція для вводу числа з консолі
 * @return Повертає зчитане число
 */
fun readNumber(prompt: String = "Input number: "): Int {
    print(prompt);
    val s = readLine()
    return s?.toInt() ?: throw IllegalStateException("Could not read a number")
}

/**
 * Допоміжна функція для вводу масиву чисел з консолі
 * @return Повертає зчитаний масив чисел
 */
fun readNumberArray(prompt: String = "Input number array (separate with commas): "): IntArray {
    print(prompt)
    val s = readLine()  ?: throw IllegalStateException("Could not read a line")
    return s.split(',').map { it.trim().toInt() }.toIntArray()
}

/**
 * Процедура злиття підмасивів left, right у масив arr
 *
 * @return Повертає кількість інверсій що була обчислена під час злиття
 */
fun merge(arr: IntArray, left: IntArray, right: IntArray): Long {
    var i = 0
    var j = 0
    var inversionCount = 0
    while (i < left.size || j < right.size) {
        if (i == left.size) { // якщо усі числа з лівого масиву уже опрацьовані
            arr[i + j] = right[j]
            j++
        } else if (j == right.size) { // якщо усі числа з правого масиву уже опрацьовані
            arr[i + j] = left[i]
            i++
        } else if (left[i] <= right[j]) { // найменший елемент знаходиться у лівому підмасиві. Просто додаємо його до результуючого масиву
            arr[i + j] = left[i]
            i++
        } else {
            // Найменший елемент знаходиться у правому підмасиві, в той час як в лівому все ще присутні елементи.
            // Це означає що існує ще стільки інверсій, скільки залишилось неопрацьованих елементів з лівого масиву
            // Тому ми додаємо це значення до загальної к-сті інверсій
            arr[i + j] = right[j]
            inversionCount += left.size - i
            j++
        }
    }
    return inversionCount.toLong()
}

/**
 * Процедура підрахунку кількості інверсій у заданому масиві методом сортування злиттям і підрахунку інверсій в процесі
 * цього сортування (див. процедуру [merge])
 *
 * @return Повертає кількість інверсій
 */
fun countInversions(arr: IntArray): Long {
    if (arr.size < 2) return 0

    // Розбиття масиву arr на два підмасиви:
    val m = (arr.size + 1) / 2
    val left = Arrays.copyOfRange(arr, 0, m)
    val right = Arrays.copyOfRange(arr, m, arr.size)

    // Результуюча кількість інверсій буде дорівнювати к-сті інверсій лівого підмасиву плюс кількість інверсій правого
    // підмасиву і плюс кількість інверсій що виявлена при злитті двох підмасивів:
    return countInversions(left) + countInversions(right) + merge(
        arr,
        left,
        right
    )
}

/**
 * Структура що представляє пару результатів обчислення інверсій між юзерами [userIndex] та X
 * @param inversions - обчислена кількість інверсій
 * @param userIndex - порядковий номер юзера якого ми порівнювали з юзером X
 */
data class Result(val inversions: Long, val userIndex: Int)
val resultComparator = Comparator<Result> { p0, p1 -> p0.inversions.compareTo(p1.inversions) }

fun main(){
    val u = readNumber("Input number of users (u): ")
    val m = readNumber("Input number of movies (m): ")
    val x = readNumber("Input index of user to compare with others (x): ")
    val D = mutableListOf<IntArray>()
    for (i in 0 until u){
        D.add(readNumberArray("Input D[$i] array: "))
    }
    val results = sortedSetOf<Result>(comparator = resultComparator)
    for (i in 0 until u){
        if (i == x) continue
        // Обчислимо "масив пріоритетів" на основі масивів D[x] i D[i], в якому і будем розраховувати інверсії
        val p = IntArray(m) { 0 }
        for (j in 0 until m){
            p[j] = D[x].indexOf(D[i][j])
        }
        val inversions = countInversions(p)
        results.add(Result(inversions, i))
    }
    println("\nResults:")
    println(results.joinToString(separator = "\n") { "(i = ${it.userIndex}; c = ${it.inversions})" })
}