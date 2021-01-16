package ua.santoni7.l5.interfaces

interface HashProvider<T> {
    /**
     * Must return an integer hash-function value which is in [0; capacity) range
     */
    fun hashFor(value: T, capacity: Int): Int

    companion object {
        fun <R> create(provider: (R, Int)->Int): HashProvider<R>  = object : HashProvider<R> {
            override fun hashFor(value: R, capacity: Int) = provider.invoke(value, capacity)
        }
        fun <R> createDefault() : HashProvider<R> = create { value, capacity -> value.hashCode() and capacity - 1 }
    }
}