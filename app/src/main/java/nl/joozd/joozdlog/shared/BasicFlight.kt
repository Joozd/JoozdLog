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

package nl.joozd.joozdlog.shared

data class BasicFlight(
val flightID: Int,
val orig: String ,
val dest: String ,
val timeOut: Long,              // timeOut and timeIn are seconds since epoch
val timeIn: Long,               // timeOut and timeIn are seconds since epoch
val correctedTotalTime: Int,
val nightTime: Int,
val ifrTime:Int,
val simTime: Int,
val aircraft: String,
val registration: String,
val name: String,
val name2: String,
val takeOffDay: Int,
val takeOffNight: Int,
val landingDay: Int,
val landingNight: Int,
val autoLand: Int,
val flightNumber: String,
val remarks: String,
val isPIC: Int,
val isPICUS: Int,
val isCoPilot: Int,
val isDual: Int,
val isInstructor: Int,
val isSim: Int,
val isPF: Int,
val isPlanned: Int,
val changed: Int,
val autoFill: Int,
val augmentedCrew: Int,
val DELETEFLAG: Int,
val signed: Boolean
)