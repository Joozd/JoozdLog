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

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import nl.joozd.joozdlog.ui.App
import org.jetbrains.anko.db.*

class FlightDbHelper : ManagedSQLiteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "flights.db"
        const val DB_VERSION = 16
        val instance by lazy { FlightDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            FlightTable.TABLENAME, true,
            FlightTable.FLIGHTID to INTEGER + PRIMARY_KEY,
            FlightTable.ORIG to TEXT,
            FlightTable.DEST to TEXT,
            FlightTable.TIMEOUT to INTEGER,
            FlightTable.TIMEIN to INTEGER,
            FlightTable.CORRECTED_TOTAL_TIME to INTEGER,
            FlightTable.AIRCRAFT to TEXT,
            FlightTable.NAME to TEXT,
            FlightTable.NAME2 to TEXT,
            FlightTable.TAKEOFFDAY to INTEGER,
            FlightTable.TAKEOFFNIGHT to INTEGER,
            FlightTable.LANDINGDAY to INTEGER,
            FlightTable.LANDINGNIGHT to INTEGER,
            FlightTable.AUTOLAND to INTEGER,
            FlightTable.FLIGHTNR to TEXT,
            FlightTable.NIGHTTIME to INTEGER,
            FlightTable.IFRTIME to INTEGER,
            FlightTable.SIMTIME to INTEGER,
            FlightTable.REGISTRATION to TEXT,
            FlightTable.REMARKS to TEXT,
            FlightTable.ISPIC to INTEGER,
            FlightTable.ISPICUS to INTEGER,
            FlightTable.ISCOPILOT to INTEGER,
            FlightTable.ISDUAL to INTEGER,
            FlightTable.ISINSTRUCTOR to INTEGER,
            FlightTable.ISSIM to INTEGER,
            FlightTable.ISPF to INTEGER,
            FlightTable.ISPLANNED to INTEGER,
            FlightTable.CHANGED to INTEGER,
            FlightTable.AUTOFILL to INTEGER,
            FlightTable.AUGMENTEDCREW to INTEGER,
            FlightTable.DELETEFLAG to INTEGER
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FlightTable.TABLENAME, true)
        Log.i("FlightDbHelper", "DB Updated, dropping whole table!")
        onCreate(db)
    }
}
