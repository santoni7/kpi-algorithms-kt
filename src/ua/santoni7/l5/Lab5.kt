package ua.santoni7.l5

import ua.santoni7.l5.interfaces.Map

fun main(){
    val maps = listOf<Map<Int, Int>>(ListHashMap(), OpenAddressHashMap())

    for(i in 0..10){
        maps.forEach { it.put(i, i*10) }
    }

    maps.forEach {
        println("Entries for ${it.javaClass.name}: " + it.getEntries()?.joinToString { it.toString() })
    }
}