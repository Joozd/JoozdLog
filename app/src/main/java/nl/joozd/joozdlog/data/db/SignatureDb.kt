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


    fun setSignature(flightId: Int, signature: String): Unit = signatureDbHelper.use {
        if (signature.isNotEmpty()) {
            Log.d(TAG, "inserting signature for flight #$flightId")
            Log.d(TAG, "signatureString:  $signature")
            replace(
                SignaturesTable.TABLENAME,
                SignaturesTable.FLIGHTID to flightId,
                SignaturesTable.SIGNATURE to signature
            )
        }
        else {
            delete(SignaturesTable.TABLENAME, "${SignaturesTable.FLIGHTID}=$flightId")
            Log.d(TAG, "deleting signature for flight #$flightId")
        }
    }
}