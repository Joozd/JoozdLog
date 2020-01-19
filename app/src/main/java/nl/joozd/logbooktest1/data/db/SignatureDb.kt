package nl.joozd.logbooktest1.data.db

import android.util.Log
import org.jetbrains.anko.db.*

class SignatureDb(private val signatureDbHelper: SignatureDbHelper = SignatureDbHelper.instance){

    companion object{
        const val TAG = "SignatureDb"
    }

    private val pairParser = rowParser {id: Int, signautre: String -> id to signautre }

    fun getAllSignedIDs() = signatureDbHelper.use {
        select(SignaturesTable.TABLENAME, SignaturesTable.FLIGHTID).parseList(IntParser)
    }

    fun getAllSignatures() = signatureDbHelper.use {
        select(SignaturesTable.TABLENAME).parseList(pairParser)
    }

    fun getSignature(flightId: Int): String = signatureDbHelper.use {
        select(SignaturesTable.TABLENAME, SignaturesTable.SIGNATURE)
            .whereArgs("(${SignaturesTable.FLIGHTID} = $flightId)")
            .parseOpt(StringParser) ?: ""
    }


    fun setSignature(flightId: Int, signature: String) = signatureDbHelper.use {
        Log.d(TAG, "inserting signature for flight #$flightId")
        replace(SignaturesTable.TABLENAME, SignaturesTable.FLIGHTID to flightId, SignaturesTable.SIGNATURE to signature )
    }
}