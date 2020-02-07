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

object FlightTable {
    const val TABLENAME = "Flights"
    const val FLIGHTID = "flightID"
    const val ORIG = "orig"
    const val DEST = "dest"
    const val TIMEOUT = "timeOut"
    const val TIMEIN = "timeIn"
    const val CORRECTED_TOTAL_TIME = "correctedTotalTime"
    const val AIRCRAFT  = "aircraft"
    const val NAME = "name"
    const val NAME2 = "name2"
    const val TAKEOFFDAY = "takeOffDay"
    const val TAKEOFFNIGHT = "takeOffNight"
    const val LANDINGDAY = "landingDay"
    const val NIGHTTIME = "nightTime"
    const val IFRTIME = "ifrTime"
    const val ISPIC = "isPIC"
    const val SIMTIME = "simTime"
    const val REGISTRATION = "registration"
    const val LANDINGNIGHT = "landingNight"
    const val AUTOLAND = "autoLand"
    const val FLIGHTNR = "flightNumber"
    const val REMARKS = "remarks"
    const val ISPICUS = "isPICUS"
    const val ISCOPILOT = "isCoPilot"
    const val ISDUAL = "isDual"
    const val ISINSTRUCTOR = "isInstructor"
    const val ISSIM = "isSim"
    const val ISPF = "isPF"
    const val ISPLANNED = "isPlanned"
    const val CHANGED = "changed"
    const val AUTOFILL = "autoFill"
    const val AUGMENTEDCREW = "augmentedCrew"
    const val DELETEFLAG = "DELETEFLAG"
}
object AirportTable {
    const val TABLENAME = "Airports"
    const val ID = "id"
    const val IDENT = "ident"
    const val TYPE = "type"
    const val NAME = "name"
    const val LATITUDE = "latitude_deg"
    const val LONGITUDE = "longitude_deg"
    const val ELEVATION = "elevation_ft"
    const val CONTINENT = "continent"
    const val COUNTRY = "iso_country"
    const val REGION = "iso_region"
    const val MUNICIPALITY = "municipality"
    const val SCHEDULED_SERVICE = "scheduled_service"
    const val GPSCODE = "gps_code"
    const val IATACODE = "iata_code"
    const val LOCALCODE = "local_code"
    const val WEBSITE = "home_link"
    const val WIKIPEDIA = "wikipedia_link"
    const val KEYWORDS = "keywords"
}

object AircraftTable {
    const val TABLENAME = "Aircraft"
    const val REGISTRATION = "registration"
    const val ID = "id"
    const val MANUFACTURER = "manufacturer"
    const val MODEL = "model"
    const val MTOW = "mtow"
    const val SE = "se"
    const val ME = "me"
    const val ENGINE_TYPE= "engine_type"
    const val MULTIPILOT = "multipilot"
    const val ISIFR = "isIfr"
}

object BalanceForwardTable {
    const val TABLENAME = "BalanceForward"
    const val ID = "id"
    const val LOGBOOKNAME = "logbookName"
    const val TOTALTIME = "aircraftTime"
    const val SIMTIME = "simTime"
    const val TAKEOFFDAY = "takeOffDay"
    const val TAKEOFFNIGHT = "takeOffNight"
    const val LANDINGDAY = "landingDay"
    const val LANDINGNIGHT = "landingNight"
    const val NIGHTTIME = "nightTime"
    const val IFRTIME = "ifrTime"
    const val PICTIME = "picTime"
    const val COPILOTTIME = "copilotTime"
    const val DUALTIME = "dualTime"
    const val INSTRUCTORTIME = "instructortime"
}

object SignaturesTable {
    const val TABLENAME = "Signatures"
    const val FLIGHTID = "flightID"
    const val SIGNATURE = "Sign"
}