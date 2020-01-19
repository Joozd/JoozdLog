package nl.joozd.logbooktest1.utils

import nl.joozd.logbooktest1.data.Flight
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun cleanPlannedFlights(f: List<Flight>) : List<Flight> =
    f.map { if (it.planned && it.tIn < LocalDateTime.ofInstant(Calendar.getInstance().toInstant(), ZoneId.of("UTC")).minusMinutes(10)) it.copy(DELETEFLAG = 1, changed = 1) else it}