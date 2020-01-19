package nl.joozd.logbooktest1.data.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import nl.joozd.logbooktest1.ui.App
import org.jetbrains.anko.db.*

class BalanceForwardDbHelper : ManagedSQLiteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "balanceforward.db"
        const val DB_VERSION = 1
        val instance by lazy { BalanceForwardDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(
            BalanceForwardTable.TABLENAME, true,
            BalanceForwardTable.ID to INTEGER + PRIMARY_KEY,
            BalanceForwardTable.LOGBOOKNAME to TEXT,
            BalanceForwardTable.TOTALTIME to INTEGER,
            BalanceForwardTable.SIMTIME to INTEGER,
            BalanceForwardTable.TAKEOFFDAY to INTEGER,
            BalanceForwardTable.TAKEOFFNIGHT to INTEGER,
            BalanceForwardTable.LANDINGDAY to INTEGER,
            BalanceForwardTable.LANDINGNIGHT to INTEGER,
            BalanceForwardTable.NIGHTTIME to INTEGER,
            BalanceForwardTable.IFRTIME to INTEGER,
            BalanceForwardTable.PICTIME to INTEGER,
            BalanceForwardTable.COPILOTTIME to INTEGER,
            BalanceForwardTable.DUALTIME to INTEGER,
            BalanceForwardTable.INSTRUCTORTIME to INTEGER
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(AircraftTable.TABLENAME, true)
        Log.i("something", "Seems onUpgrade is running for BalanceForwardDbHelper")
        onCreate(db)
    }
}