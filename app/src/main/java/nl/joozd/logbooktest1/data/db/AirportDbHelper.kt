package nl.joozd.logbooktest1.data.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import nl.joozd.logbooktest1.ui.App
import org.jetbrains.anko.db.*

class AirportDbHelper : ManagedSQLiteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "airports.db"
        const val DB_VERSION = 4
        val instance by lazy { AirportDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            AirportTable.TABLENAME, true,
            AirportTable.ID to INTEGER + PRIMARY_KEY,
                    AirportTable.IDENT to TEXT,
                    AirportTable.TYPE to TEXT,
                    AirportTable.NAME to TEXT,
                    AirportTable.LATITUDE to REAL,
                    AirportTable.LONGITUDE to REAL,
                    AirportTable.ELEVATION to INTEGER,
                    AirportTable.CONTINENT to TEXT,
                    AirportTable.COUNTRY to TEXT,
                    AirportTable.REGION to TEXT,
                    AirportTable.MUNICIPALITY to TEXT,
                    AirportTable.SCHEDULED_SERVICE to TEXT,
                    AirportTable.GPSCODE to TEXT,
                    AirportTable.IATACODE to TEXT,
                    AirportTable.LOCALCODE to TEXT,
                    AirportTable.WEBSITE to TEXT,
                    AirportTable.WIKIPEDIA to TEXT,
                    AirportTable.KEYWORDS to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(AirportTable.TABLENAME, true)
        Log.i("something", "Seems onUpgrade is running for AirportDbHelper")
        onCreate(db)
    }
}
