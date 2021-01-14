package ua.santoni7.l1

fun readNumber(prompt: String = "Input number: "): Int {
    print(prompt);
    val s = readLine()
    return s?.toInt() ?: throw IllegalStateException("Could not read a number")
}

fun readNumberArray(prompt: String = "Input number array (separate with commas): "): List<Int> {
    print(prompt)
    val s = readLine()  ?: throw IllegalStateException("Could not read a line")
    return s.split(',').map { it.trim().toInt() }
}

fun insertSort(arr: List<Int>): List<Int>{
    val a = arr.toIntArray()
    for(i in 1 until a.size){
        val current = a[i]
        var j = i - 1
        while(j>=0 && (
                    current % 2 == 0 && a[j] % 2 == 0 && a[j] > current || // Парні в порядку зростання
                            current % 2 == 1 && a[j] % 2 == 1 && a[j] < current || // Непарні в порядку спадання
                            current % 2 == 0 && a[j] % 2 == 1 // Парні лівіше від непарних
                    )){
            a[j+1] = a[j]
            j--
        }
        a[j+1] = current
    }
    return a.toList()
}

fun main() {
    val A = readNumberArray()
    val result = insertSort(A)
    println(result.joinToString { it.toString() })
}