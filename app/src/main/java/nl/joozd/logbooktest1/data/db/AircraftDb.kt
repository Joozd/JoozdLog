package nl.joozd.logbooktest1.data.db

import android.util.Log
import nl.joozd.logbooktest1.data.Aircraft
import nl.joozd.logbooktest1.extensions.parseList
import nl.joozd.logbooktest1.extensions.toVarargArray
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