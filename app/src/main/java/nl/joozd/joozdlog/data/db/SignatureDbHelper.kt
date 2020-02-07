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

class SignatureDbHelper: ManagedSQLiteOpenHelper(
    App.instance,
    SignatureDbHelper.DB_NAME, null,
    SignatureDbHelper.DB_VERSION
) {
    companion object {
        const val DB_NAME = "signatures.db"
        const val DB_VERSION = 1
        val instance by lazy { SignatureDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            SignaturesTable.TABLENAME, true,
            SignaturesTable.FLIGHTID to INTEGER + PRIMARY_KEY,
            SignaturesTable.SIGNATURE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(SignaturesTable.TABLENAME, true)
        Log.i("something", "Seems onUpgrade is running for SignatureDbHelper")
        onCreate(db)
    }
}