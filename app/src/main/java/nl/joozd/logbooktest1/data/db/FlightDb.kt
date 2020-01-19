package nl.joozd.logbooktest1.data.db

import android.util.Log
import nl.joozd.logbooktest1.data.Flight
import nl.joozd.logbooktest1.extensions.clear

import nl.joozd.logbooktest1.extensions.parseList
import nl.joozd.logbooktest1.extensions.toVarargArray
import org.jetbrains.anko.db.*

class FlightDb(private val flightDbHelper: FlightDbHelper = FlightDbHelper.instance, private val dataMapper: DbDataMapper = DbDataMapper()) {
    companion object {
        var allFlights: List<Flight> = emptyList()
        var allFlightsUpToDate = false
    }
    val highestId: Int
        get() = flightDbHelper.use {select(FlightTable.TABLENAME, FlightTable.FLIGHTID).orderBy(FlightTable.FLIGHTID, SqlOrderDirection.DESC).limit(1).parseOpt(IntParser) ?: -1}

    fun requestAllFlights() = flightDbHelper.use {
        if (!allFlightsUpToDate) {
            Log.d("flightDb","updating allFlights!")
            allFlights = dataMapper.convertFlightsToDomain(select(FlightTable.TABLENAME).parseList { FlightData(HashMap(it)) })
            allFlightsUpToDate = true
        }
        allFlights
    }
    fun saveFlights(flights: List<Flight>) = flightDbHelper.use {
        val flightDataList = dataMapper.convertFlightsFromDomain(flights)
        flightDataList.forEach {
            with (it) {
                replace(FlightTable.TABLENAME, *map.toVarargArray())
            }
        }
        if (flights.isNotEmpty()) allFlightsUpToDate = false
    }

    fun searchFlights(name: String?  = null, aircraft: String? = null, airport: String? = null) = flightDbHelper.use {

        val flights = select(FlightTable.TABLENAME).whereArgs("(aircraft LIKE {aircraft} or registration LIKE {aircraft}) AND (name LIKE {name} OR name2 LIKE {name} OR name3 LIKE {name}) AND (dest LIKE {airport} OR orig LIKE {airport})",
            "aircraft" to if (aircraft == null ) "%" else ("%$aircraft%") ,
            "name" to if (name == null ) "%" else ("%$name%") ,
            "airport" to if (airport == null ) "%" else ("%$airport%")).parseList { FlightData(HashMap(it)) }
        dataMapper.convertFlightsToDomain(flights)
    }

    fun saveFlight(flight: Flight) {
        val flightToSave = if (flight.flightID == 0) flight.copy(flightID = highestId+1) else flight
        saveFlights(listOf(flightToSave))
        allFlightsUpToDate = false
    }
    fun getSumOfAllIds(): Int =
        flightDbHelper.use {select(FlightTable.TABLENAME, FlightTable.FLIGHTID).parseList(IntParser)}.fold(0) { total, next -> total + next }


    fun clearDB(){
        flightDbHelper.use {
            clear(FlightTable.TABLENAME)
        }
    }
    fun deleteFlight(flight: Flight){
        flightDbHelper.use{
            delete(FlightTable.TABLENAME, "${FlightTable.FLIGHTID}=${flight.flightID}")
        }
    }

    fun updateCachedFlights(flights: List<Flight>){ // updates cached flights and sets flag to updated.
        allFlights = flights
        allFlightsUpToDate = true
    }
}