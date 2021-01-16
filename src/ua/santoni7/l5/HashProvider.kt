package ua.santoni7.l5

import kotlin.math.abs

/**
 * Інтерфейс що інкапсулює обчислення хеш функції і отримання індексу комірки всередині хеш таблиці
 */
interface HashProvider<T> {
    /**
     * Must return an integer hash-function value which is in [0; capacity) range
     */
    fun hashFor(value: T, capacity: Int): Int

    companion object {
        fun <R> create(provider: (value: R, capacity: Int) -> Int): HashProvider<R> = object :
            HashProvider<R> {
            override fun hashFor(value: R, capacity: Int) = provider.invoke(value, capacity)
        }

        fun <R> createDefault(): HashProvider<R> =
            create { value, capacity -> abs(value.hashCode()) and capacity - 1 }
    }
}

/**
 * Варіанти реалізації хеш функцій для цілих чисел
 */
object IntHashProviders {
    val ALL = listOf(Default, PseudoRandom, ModCapacity)

    /**
     * Використовує вбудовану у Kotlin/Java імплементацію Object::hashCode()
     */
    object Default :
        HashProvider<Int> by HashProvider.create(provider = { value, capacity -> abs(value.hashCode()) % capacity })

    /**
     * Використовує псевдо-випадкову функцію побудовану на операціях XOR і знакового/беззнакового побітового зсуву:
     */
    object PseudoRandom : HashProvider<Int> by HashProvider.create<Int>(provider = { value, capacity ->
        abs(pseudoRandomHashFunction(value)) and (capacity - 1)
    })

    /**
     * Обраховує хеш як остачу від ділення ключа на ємнітсть таблиці
     */
    object ModCapacity : HashProvider<Int> by HashProvider.create<Int>(provider = { value, capacity ->
        abs(value) % (capacity - 1)
    })

    // pseudo-random hash function
    private fun pseudoRandomHashFunction(value: Int): Int {
        var a = value
        a = a xor (a shl 13)
        a = a xor a.ushr(17)
        a = a xor (a shl 5)
        return a
    }
}