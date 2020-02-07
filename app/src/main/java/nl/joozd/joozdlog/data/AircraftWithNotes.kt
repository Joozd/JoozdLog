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

package nl.joozd.joozdlog.data

data class AircraftWithNotes(
    val id: Int,
    val registration: String,
    val manufacturer: String,
    val model: String,
    val engine_type: String,
    val mtow: Int,
    val se: Int,
    val me: Int,
    val multipilot: Int,
    val isIfr: Int,
    val isKnown: Boolean){
    constructor(a: Aircraft, isKnown: Boolean): this(a.id, a.registration, a.manufacturer,a.model,a.engine_type,a.mtow,a.se,a.me,a.multipilot,a.isIfr, isKnown)
    fun toAircraft() = Aircraft (id, registration, manufacturer, model, engine_type, mtow, se, me, multipilot, isIfr)
}