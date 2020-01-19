package nl.joozd.logbooktest1.data.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import nl.joozd.logbooktest1.ui.App
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