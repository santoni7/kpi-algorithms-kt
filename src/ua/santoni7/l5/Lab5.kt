package ua.santoni7.l5

import kotlin.random.Random

/**
 * Пошук двох чисел з масиву [A] що у сумі дають число [s]
 */
fun findTwoSum(A: IntArray, s: Int, hashMap: Map<Int, Int>): Boolean {
    for (i in 0 until A.size) {
        hashMap[A[i]] = i
    }

    for (i in 0 until A.size) {
        val x = A[i]
        val y = s - x
        val j = hashMap[y]?.value
        if (j != null && i != j) {
            println("Знайдено пару чисел з масиву А що дають у сумі S: A[$i]+A[$j]=${A[i]}+${A[j]}=$s")
            return true
        }
    }
    return false
}

fun main() {
    val mapFactories = MapFactory.ALL // усі наявні імплементації інтерфейсу Map
    val hashProviders = IntHashProviders.ALL // усі наявні імплементації інтерфейсу HashProvider

    val results = mutableMapOf<MapFactory, MutableMap<HashProvider<Int>, Int>>() // структура для збереженні к-сті колізії для кожного варіанту

    val A = generateArray(1023, 0, 100000000)
    val s = 1000

    mapFactories.forEach { factory ->
        val resultsMap = mutableMapOf<HashProvider<Int>, Int>()//ChainingHashMap<HashProvider<Int>, Int>()
        hashProviders.forEach { hashProvider ->
            val hashMap = factory.createMap(hashProvider)
            findTwoSum(A, s, hashMap)
            resultsMap[hashProvider] = hashMap.countCollisions()
        }
        results[factory] = resultsMap
    }

    results.forEach { it ->
        val mapType = it.key.name
        println("Results for $mapType")
        println(it.value.entries.joinToString(separator = "\n") { "\t${it.key::class.simpleName} => ${it.value}" })
    }
}

/**
 * Генерація випадкового масиву
 */
val random = Random(System.currentTimeMillis())

fun generateArray(size: Int, min: Int, max: Int): IntArray = IntArray(size) { random.nextInt(min, max) }

fun readNumber(prompt: String = "Input number: "): Int {
    print(prompt);
    val s = readLine()
    return s?.toInt() ?: throw IllegalStateException("Could not read a number")
}

class MapFactory(
    val name: String,
    private val factory: (HashProvider<Int>) -> Map<Int, Int>
) {
    fun createMap(hashProvider: HashProvider<Int>): Map<Int, Int> = factory.invoke(hashProvider)
    override fun equals(other: Any?): Boolean {
        return (other as? MapFactory)?.name?.equals(name) ?: false
    }

    override fun hashCode() = name.hashCode()
    override fun toString(): String = name

    companion object {
        val CHAINING = MapFactory("Chaining") { ChainingHashMap(it) }
        val OPEN_ADDRESS = MapFactory("OpenAddress") { OpenAddressHashMap(it) }

        val ALL = listOf(CHAINING, OPEN_ADDRESS)
    }
}