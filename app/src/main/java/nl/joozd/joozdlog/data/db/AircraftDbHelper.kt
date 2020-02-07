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

class AircraftDbHelper : ManagedSQLiteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "aircraft.db"
        const val DB_VERSION = 4
        val instance by lazy { AircraftDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            AircraftTable.TABLENAME, true,
            AircraftTable.ID to INTEGER + PRIMARY_KEY,
            AircraftTable.REGISTRATION to TEXT,
            AircraftTable.MANUFACTURER to TEXT,
            AircraftTable.MODEL to TEXT,
            AircraftTable.MTOW to INTEGER,
            AircraftTable.SE to INTEGER,
            AircraftTable.ME to INTEGER,
            AircraftTable.ENGINE_TYPE to TEXT,
            AircraftTable.MULTIPILOT to INTEGER,
            AircraftTable.ISIFR to INTEGER

        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(AircraftTable.TABLENAME, true)
        Log.i("something", "Seems onUpgrade is running for AircraftDbHelper")
        onCreate(db)
    }
}