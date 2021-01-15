package ua.santoni7.l4

import java.util.*

fun main(){
    val sc = Scanner(System.`in`)
    println("Input N:")
    val n = sc.nextInt()
    val medianHeap = MedianHeap(n)
    val list = mutableListOf<Int>()
    for(i in 1..n){
        // Вводимо масив по 1 елементу і додаємо у структуру medianHeap.
        // Виводимо в консоль поточну медіану масиву що уже введено
        println("Input number #$i: ")
        val x = sc.nextInt()
        medianHeap.insert(x)
        list.add(x)
        println("Median for array ${list.joinToString(prefix = "[", postfix = "]") { it.toString() }} is ${medianHeap.median()}\n")
    }
}