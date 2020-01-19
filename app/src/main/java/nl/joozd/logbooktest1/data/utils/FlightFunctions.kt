package nl.joozd.logbooktest1.data.utils

import nl.joozd.logbooktest1.data.Flight
import java.time.Instant
import java.time.LocalDateTime


// will return most recent flight that is not isPlanned
fun mostRecentCompleteFlight(flights: List<Flight>): Flight = flights.maxBy { if (it.isPlanned == 0 && it.DELETEFLAG == 0) it.tOut else LocalDateTime.of(1980, 11, 27, 10, 0) } ?: flights[0]

// returns a flight that is the return flight of the flight given (ie dest and orig swapped, flightnr plus one)
fun reverseFlight(flight: Flight, newID: Int): Flight{
    var flightnumber= flight.flightNumber
    var flightnumberDigits = ""
    while (flightnumber.last().isDigit()){
        flightnumberDigits = flightnumber.last() + flightnumberDigits
        flightnumber = flightnumber.dropLast(1)
    }
    flightnumber = if (flightnumberDigits.isEmpty()) flightnumber else flightnumber+(flightnumberDigits.toLong() + 1).toString()
    return flight.copy(flightID = newID, orig=flight.dest, dest=flight.orig, flightNumber = flightnumber, timeOut = Instant.now().epochSecond -60, timeIn = Instant.now().epochSecond, remarks = "", correctedTotalTime = 0, ifrTime = 0, nightTime = 0) // nighttime etc wont be correct but times need to be edited anyway
}


