package nl.joozd.logbooktest1.data.db

import android.database.sqlite.SQLiteException
import nl.joozd.logbooktest1.data.Airport
import nl.joozd.logbooktest1.extensions.parseList
import nl.joozd.logbooktest1.extensions.toVarargArray
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
