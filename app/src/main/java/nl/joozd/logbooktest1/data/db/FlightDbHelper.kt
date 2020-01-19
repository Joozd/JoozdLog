package nl.joozd.logbooktest1.data.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import nl.joozd.logbooktest1.ui.App
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
