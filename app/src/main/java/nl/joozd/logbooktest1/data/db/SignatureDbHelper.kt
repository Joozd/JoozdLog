package nl.joozd.logbooktest1.data.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import nl.joozd.logbooktest1.ui.App
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