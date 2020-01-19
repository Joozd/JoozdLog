package nl.joozd.logbooktest1.data.utils

import nl.joozd.logbooktest1.data.Flight
import nl.joozd.logbooktest1.data.db.AirportDb
import nl.joozd.logbooktest1.extensions.reversedMap

fun airportsToIcao(flights: List<Flight>): List<Flight> {
    val airportDb=AirportDb()
    val airportsMap= airportDb.makeIcaoIataPairs().associate{it}.reversedMap()
    val correctFlights: MutableList<Flight> = mutableListOf()
    flights.forEach {flight ->
        correctFlights.add(flight.copy(orig = airportsMap[flight.orig] ?: flight.orig, dest = airportsMap[flight.dest] ?: flight.dest))
    }
    return correctFlights.toList()
}

fun flightAirportsToIata(flights: List<Flight>, pairs: List<Pair<String, String>>): List<Flight> {
    var fixedFlights: List <Flight> = emptyList()
    val airportsMap = pairs.associate { it }
    flights.forEach {
        fixedFlights += it.copy(orig = airportsMap[it.orig] ?: it.orig, dest = airportsMap[it.dest] ?: it.dest)
    }
    return fixedFlights
}

fun flightAirportToIata(flight: Flight, airportsMap: Map<String, String>): Flight {
    return flight.copy(orig = airportsMap[flight.orig] ?: flight.orig, dest = airportsMap[flight.dest] ?: flight.dest)
}

fun flightAirportToIcao(flight: Flight): Flight {
    val db = AirportDb()
    val currentOrig = db.searchAirport(flight.orig)
    val currentDest = db.searchAirport(flight.dest)
    return if (currentOrig != null && currentDest != null) flight.copy(orig = currentOrig.ident, dest = currentDest.ident)
    else flight
}
