/*
 * JoozdLog Pilot's Logbook
 * Copyright (C) 2020 Joost Welle
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses
 */

package nl.joozd.joozdlog.utils

import android.util.Log
import nl.joozd.joozdlog.data.Aircraft
import nl.joozd.joozdlog.data.Airport
import nl.joozd.joozdlog.data.Flight
import nl.joozd.joozdlog.data.db.AircraftDb
import nl.joozd.joozdlog.data.db.AirportDb
import nl.joozd.joozdlog.data.db.FlightDb

class FlightSearcher(val flightDb: FlightDb, val airportDb: AirportDb, val aircraftDb: AircraftDb) {
    private var usedAirports: List<Airport> = emptyList()
    private var usedNames: List<String> = emptyList()
    private var usedAircraft: List<Aircraft> = emptyList()
    private val visitedAirportIdentifiers: MutableList<String> = mutableListOf()
    private val flownRegistrations: MutableList<String> = mutableListOf()
    private val foundNames: MutableList<String> = mutableListOf()
    private var allAirports: List<Airport> = emptyList()
    private var allAircraft: List<Aircraft> = emptyList()

    var flightsToSearch: List<Flight> = emptyList()
    private var oldFlights = flightsToSearch
    private var oldAirports = usedAirports
    private var oldAircraft = usedAircraft
    private var oldNames = usedNames


    class OnInitComplete(private val f: () -> Unit) {
        fun initComplete(){
            f()
        }
    }
    var onInitComplete: OnInitComplete? = null


    var initialized=false
    fun init(allFlights: List<Flight>){
        initialized=false
        flightsToSearch = allFlights
        usedAirports = buildAirportsList(allFlights)
        usedAircraft = buildAircraftlist(allFlights)
        usedNames = buildNamesList(allFlights)
        initialized = true
        onInitComplete?.initComplete()
    }

    private fun buildAirportsList(allFlights: List<Flight>): List <Airport> {
        for (flight in allFlights){
            if (flight.orig !in visitedAirportIdentifiers) visitedAirportIdentifiers.add(flight.orig)
            if (flight.dest !in visitedAirportIdentifiers) visitedAirportIdentifiers.add(flight.dest)
        }
        Log.d("buildAirportsList", "found ${visitedAirportIdentifiers.size} airports.")
        allAirports = airportDb.requestAllAirports()
        return allAirports.filter {it.ident in visitedAirportIdentifiers}
    }

    private fun buildAircraftlist(allFlights: List<Flight>): List<Aircraft> {
        for (flight in allFlights){
            if (flight.registration !in flownRegistrations) flownRegistrations.add(flight.registration)
        }
        allAircraft = aircraftDb.requestAllAircraft()
        return allAircraft.filter {it.registration in flownRegistrations}
    }

    private fun buildNamesList(allFlights: List<Flight>): List<String> {
        allFlights.forEach { flight ->
            flight.allNames.split(",").map { it.trim() }.forEach { name ->
                if (name !in foundNames) foundNames.add (name)
            }
        }
        return foundNames
    }

    fun search(name: String? = null, aircraft: String? = null, airport: String? = null): List<Flight>{
        if (!initialized) return emptyList()
        var foundFlights = flightsToSearch
        name?.let {n->
            foundFlights = foundFlights.filter{it.allNames.contains(n, ignoreCase = true)}
        }

        aircraft?.let {ac ->
            val foundAircraftRegs = usedAircraft.filter {it.registration.contains(ac, ignoreCase = true) || it.model.contains(ac, ignoreCase = true) || it.manufacturer.contains(ac, ignoreCase = true)}.map {it.registration}
            foundFlights = foundFlights.filter { it.registration in foundAircraftRegs}
        }

        airport?.let {ap ->
            val foundAirportIDs = usedAirports.filter { it.ident.contains(ap, ignoreCase = true) || it.name.contains(ap, ignoreCase = true) || it.municipality.contains(ap, ignoreCase = true) || it.iata_code.contains(ap, ignoreCase = true)}.map{it.ident}
            foundFlights = foundFlights.filter { it.orig in foundAirportIDs || it.dest in foundAirportIDs}
        }
        return foundFlights
    }

    fun addFlight(flight: Flight) {
        initialized = false

        //save old values for undoAdd()
        oldFlights = flightsToSearch
        oldAirports = usedAirports
        oldAircraft = usedAircraft
        oldNames = usedNames

        flightsToSearch = (flightsToSearch.filter{it.flightID != flight.flightID} + flight).sortedBy { it.tOut }.asReversed()
        if (flight.orig !in visitedAirportIdentifiers) {
            visitedAirportIdentifiers.add(flight.orig)
            usedAirports += listOfNotNull(allAirports.firstOrNull { it.ident == flight.orig })
        }
        if (flight.dest !in visitedAirportIdentifiers) {
            visitedAirportIdentifiers.add(flight.dest)
            usedAirports += listOfNotNull(allAirports.firstOrNull { it.ident == flight.dest })
        }
        if (flight.registration !in flownRegistrations) {
            flownRegistrations.add(flight.registration)
            usedAircraft += listOfNotNull(allAircraft.firstOrNull { it.registration == flight.registration })
        }
        flight.allNames.split(",").map { it.trim() }.forEach { name ->
            if (name !in foundNames) usedNames += name
        }
        initialized = true
    }

    fun undoAdd(){
        flightsToSearch=oldFlights
        usedAirports=oldAirports
        usedAircraft=oldAircraft
        usedNames=oldNames
    }
}