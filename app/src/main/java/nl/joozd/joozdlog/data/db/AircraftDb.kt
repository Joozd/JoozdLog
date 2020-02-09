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

import android.util.Log
import nl.joozd.joozdlog.shared.Aircraft
import nl.joozd.joozdlog.extensions.parseList
import nl.joozd.joozdlog.extensions.toVarargArray
import org.jetbrains.anko.db.IntParser
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.replace
import org.jetbrains.anko.db.select

class AircraftDb(private val aircraftDbHelper: AircraftDbHelper = AircraftDbHelper.instance, private val dataMapper: DbDataMapper = DbDataMapper()) {
    val highestId: Int
        get() = aircraftDbHelper.use {
            select(AircraftTable.TABLENAME, AircraftTable.ID).orderBy(AircraftTable.ID, SqlOrderDirection.DESC).limit(1)
                .parseOpt(IntParser) ?: 0
        }


    fun requestAllAircraft() = aircraftDbHelper.use {
        val aircraft = select(AircraftTable.TABLENAME).parseList { AircraftData(HashMap(it)) }
        dataMapper.convertAircraftToDomain(aircraft)
    }

    fun saveAircraft(aircraft: List<Aircraft>) = aircraftDbHelper.use {
        val aircraftDataList = dataMapper.convertAircraftFromDomain(aircraft)
        aircraftDataList.forEach {
            with(it) {
                replace(AircraftTable.TABLENAME, *map.toVarargArray())
                Log.d("AircraftDb", "Replaced or inserted $aircraft")
            }
        }
    }
    fun saveAircraft(aircraft: Aircraft) = saveAircraft(listOf(aircraft))

    fun searchRegAndType(reg: String = "", type: String = ""): List<Aircraft> = aircraftDbHelper.use {
        dataMapper.convertAircraftToDomain(select(AircraftTable.TABLENAME).whereArgs(
            "(${AircraftTable.REGISTRATION} LIKE {reg} AND ${AircraftTable.MODEL} LIKE {type})",
            "reg" to if (reg == "" ) "%" else ("%$reg%"),
            "type" to if (type == "" ) "%" else ("%$type%")).parseList { AircraftData(HashMap(it))})
    }
}