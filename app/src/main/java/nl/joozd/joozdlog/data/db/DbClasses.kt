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

class FlightData(val map: MutableMap<String, Any?>) {
    var flightID: Int by map
    var orig: String by map
    var dest: String by map
    var timeOut: Long by map
    var timeIn: Long by map
    var correctedTotalTime: Int by map
    var aircraft: String by map
    var name: String by map
    var name2: String by map
    var takeOffDay: Int by map
    var takeOffNight: Int by map
    var landingDay: Int by map
    var landingNight: Int by map
    var autoLand: Int by map
    var flightNumber: String by map
    var nightTime: Int by map
    var ifrTime: Int by map
    var simTime: Int by map
    var registration: String by map
    var remarks: String by map
    var isPIC: Int by map
    var isCoPilot: Int by map
    var isDual: Int by map
    var isInstructor: Int by map
    var isSim: Int by map
    var isPICUS: Int by map
    var isPF: Int by map
    var isPlanned: Int by map
    var changed: Int by map
    var autoFill: Int by map
    var augmentedCrew: Int by map
    var DELETEFLAG: Int by map


    constructor(flightID: Int, orig: String, dest: String, timeOut: Long, timeIn: Long, correctedTotalTime: Int, nightTime: Int, ifrTime: Int, simTime: Int, aircraft: String, registration: String, name: String, name2: String,
                takeOffDay: Int, takeOffNight: Int, landingDay: Int, landingNight: Int, autoLand: Int, flightNumber: String, remarks: String, isPIC: Int, isPICUS: Int, isCoPilot: Int, isDual: Int, isInstructor: Int, isSim: Int,
                isPF: Int, isPlanned: Int, changed: Int, autoFill: Int, augmentedCrew: Int, DELETEFLAG: Int) : this(HashMap()) {
        this.orig = orig
        this.dest = dest
        this.timeOut = timeOut
        this.timeIn = timeIn
        this.correctedTotalTime = correctedTotalTime
        this.aircraft = aircraft
        this.name = name
        this.name2 = name2
        this.takeOffDay = takeOffDay
        this.takeOffNight = takeOffNight
        this.landingDay = landingDay
        this.landingNight = landingNight
        this.autoLand = autoLand
        this.flightNumber = flightNumber
        this.nightTime = nightTime
        this.ifrTime = ifrTime
        this.simTime = simTime
        this.registration = registration
        this.remarks = remarks
        this.isPIC = isPIC
        this.isCoPilot = isCoPilot
        this.isDual = isDual
        this.isInstructor = isInstructor
        this.isSim = isSim
        this.isPICUS = isPICUS
        this.flightID = flightID
        this.isPF = isPF
        this.isPlanned = isPlanned
        this.changed = changed
        this.autoFill = autoFill
        this.augmentedCrew = augmentedCrew
        this.DELETEFLAG = DELETEFLAG
    }
}

class AirportData(val map: MutableMap<String, Any?>) {
    var id: Int by map
    var ident: String by map
    var type: String by map
    var name: String by map
    var latitude_deg: Double by map
    var longitude_deg: Double by map
    var elevation_ft: Int by map
    var continent: String by map
    var iso_country: String by map
    var iso_region: String by map
    var municipality: String by map
    var scheduled_service: String by map
    var gps_code: String by map
    var iata_code: String by map
    var local_code: String by map
    var home_link: String by map
    var wikipedia_link: String by map
    var keywords: String by map


    constructor(id: Int, ident: String, type: String, name: String, latitude_deg: Double, longitude_deg: Double, elevation_ft: Int,
                continent: String, iso_country: String, iso_region: String, municipality: String, scheduled_service: String,
                gps_code: String, iata_code: String, local_code: String, home_link: String, wikipedia_link: String, keywords: String) : this(HashMap()){
        this.id=id
        this.ident=ident
        this.type=type
        this.name=name
        this.latitude_deg=latitude_deg
        this.longitude_deg=longitude_deg
        this.elevation_ft=elevation_ft
        this.continent=continent
        this.iso_country=iso_country
        this.iso_region=iso_region
        this.municipality=municipality
        this.scheduled_service=scheduled_service
        this.gps_code=gps_code
        this.iata_code=iata_code
        this.local_code=local_code
        this.home_link=home_link
        this.wikipedia_link=wikipedia_link
        this.keywords=keywords
    }
}

class AircraftData(val map:MutableMap<String, Any?>){
    var id: Int by map
    var registration: String by map
    var manufacturer: String by map
    var model: String by map
    var engine_type: String by map
    var mtow: Int by map
    var se: Int by map
    var me: Int by map
    var multipilot: Int  by map
    var isIfr: Int by map

    constructor(id: Int, registration: String, manufacturer: String, model: String, engine_type: String, mtow: Int, se: Int, me: Int, multipilot: Int, isIfr: Int) : this(HashMap()){
        this.id = id
        this.registration = registration
        this.manufacturer = manufacturer
        this.model = model
        this.engine_type = engine_type
        this.mtow = mtow
        this.se = se
        this.me = me
        this.multipilot = multipilot
        this.isIfr = isIfr
    }
}

class BalanceForwardData(val map:MutableMap<String, Any?>){
    var logbookName: String by map
    var totalTime: Int by map
    var simTime: Int by map
    var takeOffDay: Int by map
    var takeOffNight: Int by map
    var landingDay: Int by map
    var landingNight: Int by map
    var nightTime: Int by map
    var ifrTime: Int by map
    var picTime: Int by map
    var copilotTime: Int by map
    var dualTime: Int by map
    var instructortime: Int by map
    var id: Int by map

    constructor(logbookName: String, totalTime: Int, simTime: Int, takeOffDay: Int, takeOffNight: Int,
                landingDay: Int, landingNight: Int, nightTime: Int, ifrTime: Int, picTime: Int, copilotTime: Int, dualTime: Int, instructortime: Int, id: Int) : this(HashMap()){
        this.logbookName = logbookName
        this.totalTime = totalTime
        this.simTime = simTime
        this.takeOffDay = takeOffDay
        this.takeOffNight = takeOffNight
        this.landingDay = landingDay
        this.landingNight = landingNight
        this.nightTime = nightTime
        this.ifrTime = ifrTime
        this.picTime = picTime
        this.copilotTime = copilotTime
        this.dualTime = dualTime
        this.instructortime = instructortime
        this.id = id
    }
}
