package nl.joozd.logbooktest1.data.utils

import android.util.Log
import nl.joozd.logbooktest1.data.Flight

class NamesWorker (){
    class InitializationListener(private val f:() -> Unit ) {
        fun initialized(){
            f()
        }
    }
    private var flightList: List<Flight> = emptyList()
    var initializationListener: InitializationListener? = null
    var nameList: List<String> = emptyList()
    var isInitialized = false

    fun initialize(newFlights: List<Flight>){
        flightList=newFlights
        Log.d ("NamesWorker", "Got ${nameList.size} names!")
        Log.d("populateNames", "got ${flightList.size} flights")
        nameList = buildNamesList(flightList)
        Log.d("populateNames", "got ${nameList.size} names")
        isInitialized = true
        initializationListener?.initialized()
    }

    fun queryName(query: String): List<String> = nameList.filter {it.contains(query, ignoreCase = true)}
    fun getOneName (query: String): String? = nameList.firstOrNull {it.toUpperCase().contains(query.toUpperCase())}


    // TODO: WIP
/*
    private fun unpackNames(vararg nameStrings: String): List<String>? {
        val names: MutableList<String> = mutableListOf()
        for (nameString in nameStrings) {
            if (',' in nameString) {
                var text = nameString
                while (',' in text) {
                    val name = text.slice(0 until text.indexOf(','))
                    text = nameString.slice(text.indexOf(',') until text.length).trim(',', ' ')
                    if (name.isNotEmpty() && name != "SELF") names.add(name)
                }
                if (text.isNotEmpty()) names.add(text)
            } else if (nameString.isNotEmpty() && nameString != "SELF") names.add(nameString)
        }
        return if (names.isEmpty()) null else names
    }
*/
    private fun buildNamesList(allFlights: List<Flight>): List<String> {
        val foundNames: MutableList<String> = mutableListOf()
        allFlights.forEach { flight ->
            flight.allNames.split(",").map { it.trim() }.forEach { name ->
                if (name !in foundNames) foundNames.add(name)
            }
        }
        return foundNames
    }


    fun addFlight(flight: Flight){
        nameList = (nameList + buildNamesList(listOf(flight))).distinct()
    }
}