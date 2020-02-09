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

package nl.joozd.joozdlog.data.db

import nl.joozd.joozdlog.shared.Aircraft
import nl.joozd.joozdlog.shared.Airport
import nl.joozd.joozdlog.shared.BalanceForward
import nl.joozd.joozdlog.data.Flight

class DbDataMapper {

    fun convertFlightsToDomain(flightData: List<FlightData>) = with(flightData) {
        var flights :List<Flight> = emptyList()
        flightData.forEach{
            with(it) {
                flights += Flight(
                    flightID,
                    orig,
                    dest,
                    timeOut,
                    timeIn,
                    correctedTotalTime,
                    nightTime,
                    ifrTime,
                    simTime,
                    aircraft,
                    registration,
                    name,
                    name2,
                    takeOffDay,
                    takeOffNight,
                    landingDay,
                    landingNight,
                    autoLand,
                    flightNumber,
                    remarks,
                    isPIC,
                    isPICUS,
                    isCoPilot,
                    isDual,
                    isInstructor,
                    isSim,
                    isPF,
                    isPlanned,
                    changed,
                    autoFill,
                    augmentedCrew,
                    DELETEFLAG
                )
            }
        }
        flights
    }

    fun convertFlightsFromDomain(flights: List<Flight>) = with(flights) {
        var flightDatas: List<FlightData> = emptyList()
        flights.forEach {
            with (it){
                flightDatas += (FlightData(flightID, orig, dest, timeOut, timeIn, correctedTotalTime, nightTime, ifrTime, simTime, aircraft, registration, name, name2, takeOffDay, takeOffNight, landingDay, landingNight, autoLand, flightNumber, remarks, isPIC, isPICUS, isCoPilot, isDual, isInstructor, isSim, isPF, isPlanned, changed, autoFill, augmentedCrew, DELETEFLAG) )
            }
        }
        flightDatas
    }

    fun convertAirportsToDomain(airportData: List<AirportData>) = with (airportData){
        var airports : List<Airport> = emptyList()
        airportData.forEach {
            with(it) {
                airports += Airport(
                    id,
                    ident,
                    type,
                    name,
                    latitude_deg,
                    longitude_deg,
                    elevation_ft,
                    continent,
                    iso_country,
                    iso_region,
                    municipality,
                    scheduled_service,
                    gps_code,
                    iata_code,
                    local_code,
                    home_link,
                    wikipedia_link,
                    keywords
                )
            }
        }
        airports
    }

    fun convertAirportsFromDomain(airports: List<Airport>) = with(airports) {
        var airportDatas: List<AirportData> = emptyList()
        airports.forEach {
            with (it){
                airportDatas += (AirportData(id, ident, type, name, latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link, wikipedia_link, keywords) )
            }
        }
        airportDatas
    }

    fun convertAircraftFromDomain(aircraft: List<Aircraft>) = with(aircraft) {
        var aircraftDatas : List<AircraftData> = emptyList()
        aircraft.forEach {
            with (it){
                aircraftDatas += (AircraftData(id, registration, manufacturer, model, engine_type, mtow, se, me, multipilot, isIfr))
            }
        }
        aircraftDatas
    }
    fun convertAircraftToDomain(aircraftData: List<AircraftData>) = with (aircraftData){
        val aircraft : MutableList<Aircraft>  = mutableListOf()
        aircraftData.forEach {
            with(it) {
                aircraft.add(
                    Aircraft(
                        id,
                        registration,
                        manufacturer,
                        model,
                        engine_type,
                        mtow,
                        se,
                        me,
                        multipilot,
                        isIfr
                    )
                )
            }
        }
        aircraft.toList()
    }
    fun convertBalanceForwardToDomain(balanceData: List<BalanceForwardData>) = with (balanceData) {
        val balanceForwardList: MutableList<BalanceForward> = mutableListOf()
        balanceData.forEach {
            with (it) {
                balanceForwardList.add(
                    BalanceForward(
                        logbookName,
                        totalTime,
                        simTime,
                        takeOffDay,
                        takeOffNight,
                        landingDay,
                        landingNight,
                        nightTime,
                        ifrTime,
                        picTime,
                        copilotTime,
                        dualTime,
                        instructortime,
                        id
                    )
                )
            }
        }
        balanceForwardList.toList()
    }
    fun convertBalanceForwardFromDomain(balanceData: List<BalanceForward>) = with (balanceData){
        val balanceForwardData: MutableList<BalanceForwardData> = mutableListOf()
        balanceData.forEach{
            with (it){
                balanceForwardData.add(
                    BalanceForwardData(logbookName, aircraftTime, simTime, takeOffDay, takeOffNight, landingDay, landingNight,
                        nightTime, ifrTime, picTime, copilotTime, dualTime, instructortime, id))
            }
        }
        balanceForwardData.toList()
    }

    fun convertBalanceForwardFromDomain(balanceData: BalanceForward) = with (balanceData){
                    BalanceForwardData(logbookName, aircraftTime, simTime, takeOffDay, takeOffNight, landingDay, landingNight,
                        nightTime, ifrTime, picTime, copilotTime, dualTime, instructortime, id)

    }

}
