package nl.joozd.logbooktest1.data.db

import nl.joozd.logbooktest1.data.Aircraft
import nl.joozd.logbooktest1.data.Airport
import nl.joozd.logbooktest1.data.BalanceForward
import nl.joozd.logbooktest1.data.Flight

class DbDataMapper {

    fun convertFlightsToDomain(flightData: List<FlightData>) = with(flightData) {
        var flights :List<Flight> = emptyList()
        flightData.forEach{
            with(it) {
                flights += Flight(flightID, orig, dest, timeOut, timeIn, correctedTotalTime, nightTime, ifrTime, simTime, aircraft, registration, name, name2, takeOffDay, takeOffNight, landingDay, landingNight, autoLand,
                    flightNumber, remarks, isPIC, isPICUS, isCoPilot, isDual, isInstructor, isSim, isPF, isPlanned, changed, autoFill, augmentedCrew, DELETEFLAG)
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
                airports += Airport(id, ident, type, name, latitude_deg, longitude_deg, elevation_ft, continent, iso_country, iso_region, municipality, scheduled_service, gps_code, iata_code, local_code, home_link, wikipedia_link, keywords)
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
                aircraft.add(Aircraft(id, registration, manufacturer, model, engine_type, mtow, se, me, multipilot, isIfr))
            }
        }
        aircraft.toList()
    }
    fun convertBalanceForwardToDomain(balanceData: List<BalanceForwardData>) = with (balanceData) {
        val balanceForwardList: MutableList<BalanceForward> = mutableListOf()
        balanceData.forEach {
            with (it) {
                balanceForwardList.add(BalanceForward(logbookName, totalTime, simTime, takeOffDay, takeOffNight, landingDay, landingNight,
                nightTime, ifrTime, picTime, copilotTime, dualTime, instructortime, id))
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
