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

import nl.joozd.joozdlog.data.Airport
import nl.joozd.joozdlog.extensions.parseList
import nl.joozd.joozdlog.extensions.toVarargArray
import org.jetbrains.anko.db.*

class AirportDb(private val airportDbHelper: AirportDbHelper = AirportDbHelper.instance, private val dataMapper: DbDataMapper = DbDataMapper()) {
    companion object {
        var icaoIataPairs: List<Pair<String, String>>? = null
        var icaoIataMap: Map<String, String>? = null
        var allAirports: List<Airport> = emptyList()
        var allAirportsUpToDate = false
    }

    val highestId: Int
        get() =  airportDbHelper.use {
            select(AirportTable.TABLENAME, AirportTable.ID).orderBy(AirportTable.ID, SqlOrderDirection.DESC).limit(1)
                .parseOpt(IntParser) ?: 0
            }



    fun requestAllAirports() = airportDbHelper.use {
        if (!allAirportsUpToDate)      {
            allAirports = dataMapper.convertAirportsToDomain(select(AirportTable.TABLENAME).parseList { AirportData(HashMap(it))})
            allAirportsUpToDate = true
        }
        allAirports
    }

    fun saveAirports(airports: List<Airport>) = airportDbHelper.use {
        allAirportsUpToDate = false
        val airportDataList = dataMapper.convertAirportsFromDomain(airports)
        airportDataList.forEach {
            with (it){
                replace(AirportTable.TABLENAME, *map.toVarargArray())
            }
        }
    }

    // Will return one airport. Searches for IATA then ICAO then municipality then name. Returns null if nothing found.
    fun searchAirport(term: String): Airport? = airportDbHelper.use {
        var airportsList = select(AirportTable.TABLENAME).whereArgs("(${AirportTable.IATACODE} LIKE {term})", "term" to "%$term%").parseList { AirportData(HashMap(it))}
        if (airportsList.isEmpty()) airportsList = select(AirportTable.TABLENAME).whereArgs("(${AirportTable.IDENT} LIKE {term})", "term" to "%$term%").parseList { AirportData(HashMap(it))}
        if (airportsList.isEmpty()) airportsList = select(AirportTable.TABLENAME).whereArgs("(${AirportTable.MUNICIPALITY} LIKE {term})", "term" to "%$term%").parseList { AirportData(HashMap(it))}
        if (airportsList.isEmpty()) airportsList = select(AirportTable.TABLENAME).whereArgs("(${AirportTable.NAME} LIKE {term})", "term" to "%$term%").parseList { AirportData(HashMap(it))}
        if (airportsList.isEmpty()) null else dataMapper.convertAirportsToDomain(airportsList)[0]
    }
    fun searchAirports(term: String): List<Airport> = airportDbHelper.use {
        var airportsList = select(AirportTable.TABLENAME).whereArgs("(${AirportTable.IATACODE} LIKE {term}) OR (${AirportTable.NAME} LIKE {term}) OR (${AirportTable.MUNICIPALITY} LIKE {term}) OR (${AirportTable.IDENT} LIKE {term})", "term" to "%$term%").parseList { AirportData(HashMap(it))}
        if (airportsList.isEmpty()) emptyList() else dataMapper.convertAirportsToDomain(airportsList)
    }
    fun searchAirportsToIcao(term: String): List<String> = airportDbHelper.use {
        select(AirportTable.TABLENAME, AirportTable.IDENT)
            .whereArgs("(${AirportTable.IATACODE} LIKE {term}) OR (${AirportTable.NAME} LIKE {term}) OR (${AirportTable.MUNICIPALITY} LIKE {term}) OR (${AirportTable.IDENT} LIKE {term})", "term" to "%$term%")
            .parseList(StringParser)
    }


    fun makeIcaoIataPairs() : List<Pair<String, String>> = airportDbHelper.use {
        if (icaoIataPairs == null) {
            val parser = rowParser { icao: String, iata: String -> Pair(icao, iata) }
            icaoIataPairs = select(
                AirportTable.TABLENAME,
                AirportTable.IDENT,
                AirportTable.IATACODE
            ).whereArgs("(${AirportTable.IATACODE} <> '')").parseList(parser)
        }
        icaoIataPairs!!
    }

    fun makeIcaoIataMap(): Map <String, String> = airportDbHelper.use {
        if (icaoIataPairs == null) {
            val parser = rowParser { icao: String, iata: String -> Pair(icao, iata) }
            icaoIataPairs = select(
                AirportTable.TABLENAME,
                AirportTable.IDENT,
                AirportTable.IATACODE
            ).whereArgs("(${AirportTable.IATACODE} <> '')").parseList(parser)
        }
        if (icaoIataMap == null) icaoIataMap = icaoIataPairs!!.associate { it }
        icaoIataMap!!
    }
}
