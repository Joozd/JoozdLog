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

package nl.joozd.joozdlog.comm

import android.util.Base64
import com.google.gson.Gson
import nl.joozd.joozdlog.comm.protocol.NotEncryptedComm
import nl.joozd.joozdlog.data.Aircraft
import nl.joozd.joozdlog.data.Airport
import nl.joozd.joozdlog.data.Flight
import nl.joozd.joozdlog.data.db.AircraftDb


class Comms{
    private val handler: NotEncryptedComm = NotEncryptedComm()
    companion object{
        const val REQUEST_ALL       = "___REQ_ALL"
        const val SEND_FLIGHTS      = "_SEND_FLTS" // second arg List<Flight> with all new flights.
        const val SEND_AIRCRAFT     = "SNDAIRCRFT"
        const val REQUEST_MISSING   = "REQMISSING" // second arg will be list of already known flights
        const val REQUEST_UPDATED   = "REQUPDATED" // check for flights that are changed on server side
        const val CHECK_AIRPORTS    = "CHKAIRPRTS" // check if airport database still correct
        const val CHECK_AIRCRAFT    = "CHKAIRCRFT" // check if aircraft database still uptodate
        const val CHECK_SYNCHED     = "CHKSYNCHED" // check is fum of all id's is the same here as there
        const val SEND_ROSTER       = "__SEND_PDF" // send a PDF file. Make the server turn it into flights. Or not.
    }


    fun rebuildFromServer(): List<Flight> {
        synchronized(this) {
            handler.sendRequest(REQUEST_ALL)
            return handler.receiveFlights()
        }
    }

    fun checkAirports(highestID: Int): List<Airport>? {
        synchronized(this) {
            handler.sendRequest(CHECK_AIRPORTS, highestID.toString())
            val airports = handler.receiveAirports()
            return if (airports.isEmpty()) null else airports
        }
    }

    fun checkAircraft(highestID: Int): List<Aircraft>? {
        handler.sendRequest(CHECK_AIRCRAFT, highestID.toString())
        val aircrafts = handler.receiveAircraft()
        return if (aircrafts.isEmpty()) null else aircrafts
    }
    fun sendAircraftFromDb() {
        synchronized(this) {
            val aircraftDb = AircraftDb()
            val gson = Gson()
            handler.sendRequest(SEND_AIRCRAFT, gson.toJson(aircraftDb.requestAllAircraft()))

        }
    }


    fun getUpdates(): List<Flight> {
        synchronized(this) {
            handler.sendRequest(REQUEST_UPDATED)
            var newFlights: List<Flight> = emptyList()
            handler.receiveFlights().forEach {
                newFlights += it.copy(changed = 0)
            }
            return newFlights
        }
    }

    fun sendUpdates(allFlights: List<Flight>): Boolean {
        synchronized(this) {
            val updatedflights = allFlights.filter { it.changed > 0 }
            if (updatedflights.isEmpty()) return true
            var greatSuccess: Boolean
            try {
                greatSuccess = handler.sendFlights(SEND_FLIGHTS, updatedflights)
            } catch (e: Throwable) {
                return false
            }
            return greatSuccess
        }
    }

    fun checkifsynched(checksum: Int): Boolean {
        synchronized(this) {
            handler.sendRequest(CHECK_SYNCHED)
            return handler.receiveString()?.toInt() ?: -1 == checksum
        }
    }

    fun sendPdfRoster(roster: ByteArray): Boolean {
        synchronized(this) {
            handler.sendRequest(SEND_ROSTER, Base64.encodeToString(roster, Base64.DEFAULT))
            return handler.receiveRosterUpdateReply()
        }
    }


    fun close() {
        synchronized(this) {
            handler.close()
        }
    }
}