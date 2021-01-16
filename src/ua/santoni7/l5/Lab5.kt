package ua.santoni7.l5

import kotlin.random.Random

/**
 * Пошук двох чисел з масиву [A] що у сумі дають число [s]
 */
fun findTwoSum(A: IntArray, s: Int, hashMapFactory: ()-> Map<Int, Int>){
    val hashMap = hashMapFactory.invoke()

    for (i in 0 until A.size){
        hashMap[A[i]] = i
    }

    for (i in 0 until A.size){
        val x = A[i]
        val y = s - x
        val j = hashMap[y]?.value
        if(j != null && i != j){
            println("Знайдено пару чисел з масиву А що дають у сумі S: A[$i]+A[$j]=${A[i]}+${A[j]}=$s")
        }
    }
}

fun main(){
    val maps = listOf<Map<Int, Int>>(ListHashMap(), OpenAddressHashMap())

    while(true) {
        val A = generateArray(10, 0, 10)
        println("A: " + A.joinToString { it.toString() })
        findTwoSum(A, 6) { ListHashMap() }
        readLine()
    }
}

/**
 * Генерація випадкового масиву
 */
val random = Random(System.currentTimeMillis())

fun generateArray(size: Int, min: Int, max: Int): IntArray = IntArray(size) { random.nextInt(min, max) }
