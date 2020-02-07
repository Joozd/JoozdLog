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

package nl.joozd.joozdlog.comm.protocol

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nl.joozd.joozdlog.comm.utils.addCredentials
import nl.joozd.joozdlog.data.Aircraft
import nl.joozd.joozdlog.data.Airport
import nl.joozd.joozdlog.data.Flight
import nl.joozd.joozdlog.utils.toJson

class NotEncryptedComm { // will request an AES key upon initializiation and use that for further Comms
    companion object {
        const val PLEASE_SYNC = "PLEASE_SYNC"
        const val EOF = "EOF"
        const val INCREMENT = 500
    }
    private val client = Client()


/*    private val rsa: RSAHandler=RSAHandler()
    private val aes: AESHandler

    init {
        aes=AESHandler(requestKey(rsa))
    }

    private fun requestKey(rsa: RSAHandler): ByteArray{ // returns AES key as bytearray, or empty byteArray if shit dont work
        Log.d("requestKey:", "now requesting a key!")
        client.sendToServer( Packet(("REQUESTKEY".toByteArray(Charsets.UTF_8) + rsa.publicKey.encoded), OUTBOUND_PACKET )) //works
        val reply = client.readFromSocket() // doesnt work because socket is closed
        if (reply == null) { // that not supposed to happen
            Log.e(javaClass.simpleName, "Got null reply from readFromSocket()")
            return ByteArray(0)
        }
        return reply.message // unsafe for now!
    }
*/
    fun sendRequest(requestString: String, extraData: String?=null){ // for list of possible requests, see documentation
        var request = requestString
        extraData?.let { request += ":$it" }
        client.sendToServer(
            Packet(request.addCredentials().toByteArray(Charsets.UTF_8), OUTBOUND_PACKET)
        )
    }
    fun receiveFlights(): List<Flight> {
        Log.d("receiveFlights!" ,"Receiving Flights?")
        var flightsList: List<Flight> = emptyList()
        val listType = object : TypeToken<List<Flight>>() { }.type
        do {
            val jsonData = (client.readFromSocket()!!.message).toString(Charsets.UTF_8)
            Log.d("receiveFlights!", "# of flights: ${flightsList.size}")
            if (jsonData != EOF) {
                flightsList += Gson().fromJson<List<Flight>>(jsonData, listType)
                client.sendOK()
            }
        }
        while (jsonData != EOF)
        return flightsList
        }

    fun sendFlights(requestString: String, flights: List<Flight>): Boolean{
        Log.d("sendFlights!" ,"Sending Flights?")
        sendRequest(requestString)
        Log.d("sendFLights", "waiting for OK")
        client.receiveOK()
        Log.d("sendFLights", "received OK")
        var remainingFlights = flights
        while (remainingFlights.size > INCREMENT){
            var flightsToSend = remainingFlights.slice(0 until INCREMENT)
            remainingFlights = remainingFlights.drop(INCREMENT)
            Log.d("sendFlights", flightsToSend.toJson().slice(0 until 300))
            client.sendToServer(Packet(flightsToSend.toJson().toByteArray(Charsets.UTF_8), true))
            client.receiveOK()
        }
        if (remainingFlights.isNotEmpty()) {
            client.sendToServer(Packet(remainingFlights.toJson().toByteArray(Charsets.UTF_8), true))
            client.receiveOK()
        }
        client.sendToServer(Packet(EOF.toByteArray(Charsets.UTF_8), true))
        return client.receiveOK()
    }

    fun receiveAirports(): List<Airport> {
        Log.d("receiveAirports!" ,"Receiving Airports?")
        var airportsList: List<Airport> = emptyList()
        val listType = object : TypeToken<List<Airport>>() { }.type
        do {
            val jsonData = (client.readFromSocket()!!.message).toString(Charsets.UTF_8)
            Log.d("receiveAirports!", "# of airports: ${airportsList.size}")
            if (jsonData != EOF) {
                airportsList += Gson().fromJson<List<Airport>>(jsonData, listType)
                client.sendOK()
            }
        }
        while (jsonData != EOF)
        return airportsList
    }

    fun receiveAircraft(): List<Aircraft> {
        Log.d("receiveAircraft!" ,"Receiving Aircraft?")
        var aircraftList: List<Aircraft> = emptyList()
        val listType = object : TypeToken<List<Aircraft>>() { }.type
        do {
            val jsonData = (client.readFromSocket()!!.message).toString(Charsets.UTF_8)
            Log.d("receiveAircraft", "# of aircraft: ${aircraftList.size}")
            if (jsonData != EOF && jsonData != PLEASE_SYNC) {
                aircraftList += Gson().fromJson<List<Aircraft>>(jsonData, listType)
                client.sendOK()
            }
            else if (jsonData == PLEASE_SYNC){

            }
        }
        while (jsonData != EOF && jsonData != PLEASE_SYNC)
        return aircraftList
    }
    fun receiveString(): String? {
        return client.readFromSocket()?.message?.toString(Charsets.UTF_8)
    }

    fun receiveRosterUpdateReply(): Boolean {
        return client.receiveOK()
    }

    fun close(){
        client.close()
    }
}