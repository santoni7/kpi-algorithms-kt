package ua.santoni7.l4

import java.util.*


class MedianHeap(val capacity: Int = 1000) {

    //stores all the numbers less than the current median in a maxheap, i.e median is the maximum, at the root
    private val maxheap = Heap(capacity, HeapType.Max)
    //stores all the numbers greater than the current median in a minheap, i.e median is the minimum, at the root
    private val minheap = Heap(capacity, HeapType.Min)

    /**
     * Returns empty if no median i.e, no input
     * @return
     */
    private val isEmpty: Boolean
        get() = maxheap.size == 0 && minheap.size == 0


    /**
     * Inserts into MedianHeap to update the median accordingly
     * @param n
     */
    fun insert(n: Int) {
        // initialize if empty
        if (isEmpty) {
            minheap.insert(n)
        } else {
            //add to the appropriate heap
            // if n is less than or equal to current median, add to maxheap
            // TODO: CLARIFY HERE
            if (n.toDouble().compareTo(median()) <= 0) {
                maxheap.insert(n)
            } else {
                minheap.insert(n)
            }// if n is greater than current median, add to min heap
        }
        // fix the chaos, if any imbalance occurs in the heap sizes
        //i.e, absolute difference of sizes is greater than one.
        rebalanceIfNeeded()
    }

    /**
     * Re-balances the heap sizes
     */
    private fun rebalanceIfNeeded() {
        //if sizes of heaps differ by 2, then it's a chaos, since median must be the middle element
        if (Math.abs(maxheap.size - minheap.size) > 1) {
            //check which one is the culprit and take action by kicking out the root from culprit into victim
            if (maxheap.size > minheap.size) {
                minheap.insert(maxheap.pop())
            } else {
                maxheap.insert(minheap.pop())
            }
        }
    }

    /**
     * returns the median of the numbers encountered so far
     * @return
     */
    fun median(): Double {
        //if total size(no. of elements entered) is even, then median iss the average of the 2 middle elements
        //i.e, average of the root's of the heaps.
        return if (maxheap.size == minheap.size) {
            (maxheap.peek().toDouble() + minheap.peek().toDouble()) / 2
        } else if (maxheap.size > minheap.size) {
            maxheap.peek().toDouble()
        } else {
            minheap.peek().toDouble()
        }//else median is middle element, i.e, root of the heap with one element more

    }

    /**
     * String representation of the numbers and median
     * @return
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

    /**
     * Adds all the array elements and returns the median.
     * @param array
     * @return
     */
    fun addArray(array: IntArray): Double {
        for (i in array.indices) {
            insert(array[i])
        }
        return median()
    }

    /**
     * Just a test
     * @param N
     */
    fun test(N: Int) {
        val array = InputGenerator.randomArray(N)
        println("Input array: \n" + Arrays.toString(array))

        for(i in 1..N){
            println("**************** ITERATION $i ****************")
            val subArray = array.copyOfRange(0, i)
            val x = array[i-1]
            insert(x)

            println("Inserted $x into heap. Computed Median is :" + median())
            minheap.print()
            maxheap.print()
            Arrays.sort(subArray)
            println("Sorted (sub)array: " + Arrays.toString(subArray) )
            println("Calculated median is ${sortAndGetMedianForTest(subArray.size, subArray)}")

        }
//        addArray(array)
        println("------------------")
        println("Computed Median is :" + median())
        Arrays.sort(array)
        println("Sorted array: \n" + Arrays.toString(array))
        println("Calculated Median is : ${sortAndGetMedianForTest(N, array)}")
    }

    private fun sortAndGetMedianForTest(N: Int, array: IntArray): Double =
        if (N % 2 == 0) {
            (array[N / 2] + array[N / 2 - 1]) / 2.0
//            println("Calculated Median is :" + (array[N / 2] + array[N / 2 - 1]) / 2.0)
        } else {
            array[N / 2].toDouble()
//            println("Calculated Median is :" + array[N / 2] + "\n")
        }


    /**
     * Another testing utility
     */
    fun printInternal() {
        println("Less than median, max heap:$maxheap")
        println("Greater than median, min heap:$minheap")
    }

    //Inner class to generate input for basic testing
    private object InputGenerator {

        fun orderedArray(N: Int): IntArray {
            val array = IntArray(N)
            for (i in 0 until N) {
                array[i] = i
            }
            return array
        }

        fun randomArray(N: Int): IntArray {
            val array = IntArray(N)
            for (i in 0 until N) {
                array[i] = (Math.random() * N.toDouble() * N.toDouble()).toInt()
            }
            return array
        }

        fun readInt(s: String): Int {
            println(s)
            val sc = Scanner(System.`in`)
            return sc.nextInt()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            while (true) {
                val testObj = MedianHeap()
                testObj.test(InputGenerator.readInt("Enter size of the array:"))
                println(testObj)
            }
        }
    }
}