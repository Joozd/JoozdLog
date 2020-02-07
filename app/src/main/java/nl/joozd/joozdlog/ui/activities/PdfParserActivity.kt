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

package nl.joozd.joozdlog.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_pdf_parser.*
import nl.joozd.joozdlog.R
import nl.joozd.joozdlog.comm.Comms
import nl.joozd.joozdlog.data.Flight
import nl.joozd.joozdlog.data.db.FlightDb
import nl.joozd.joozdlog.data.utils.airportsToIcao
import nl.joozd.joozdlog.extensions.toByteArray
import nl.joozd.joozdlog.ui.App
import nl.joozd.joozdlog.utils.startMainActivity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import java.io.FileNotFoundException

import java.util.concurrent.CountDownLatch

class PdfParserActivity : AppCompatActivity() {
    private val initialized = CountDownLatch(1)
    private val synchronized = CountDownLatch(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("PdfParserActivity", "started")
        setContentView(R.layout.activity_pdf_parser)

        lateinit var server: Comms

        doAsync{
            server= Comms()
            initialized.countDown()
        }
        val flightDb = FlightDb()
        val allFlights = (flightDb.requestAllFlights()).sortedBy { it.tOut }.asReversed()
        doAsync {
            initialized.await()
            runOnUiThread {
                connectedCheck.visibility = View.VISIBLE
            }
            if (server.sendUpdates(allFlights)) { // checks for a working connection to server, and updates flights. Returns false when no good connection.
                val updatedflights = (allFlights.filter { it.changed > 0 })
                if (updatedflights.isNotEmpty()) {
                    var flightsToSave = emptyList<Flight>()
                    updatedflights.forEach {
                        flightsToSave += it.copy(changed = 0)
                    }
                    flightDb.saveFlights(airportsToIcao(flightsToSave))
                }
                val newFlights = server.getUpdates()
                if (newFlights.isNotEmpty()) {
                    flightDb.saveFlights(airportsToIcao(newFlights))
                }
            } else { // no connection to server, maybe save roster for later use to do whole update thingy whenever phone comes online
                longToast("No connection, discarding")
                startMainActivity(App.instance)
                this@PdfParserActivity.finish()
            }
            synchronized.countDown()
            runOnUiThread {
                synchronizingCheck.visibility = View.VISIBLE
            }
        }
        intent?.let {
            Log.d("PdfParserActivity", intent.action ?: "No intent.action")
            Log.d("PdfParserActivity", intent.type ?: "No intent.type")
            (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
                val inputStream = try {
                    /*
                     * Get the content resolver instance for this context, and use it
                     * to get a ParcelFileDescriptor for the file.
                     */
                    contentResolver.openInputStream(it)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Log.e("PdfParserActivity", "File not found.")
                    return
                }
                doAsync {
                    synchronized.await()
                    // TODO make some loading twirly thing
                    if (server.sendPdfRoster(inputStream!!.toByteArray())) {
                        runOnUiThread {
                            sendingDataCheck.visibility=View.VISIBLE
                        }
                        Log.d("pdfParserActivity", "werkt")
                        val newFlights = server.getUpdates()
                        if (newFlights.isNotEmpty()) {
                            flightDb.saveFlights(airportsToIcao(newFlights))
                        }
                        runOnUiThread {
                            receivingDataCheck.visibility=View.VISIBLE
                        }
                        startMainActivity(App.instance)
                    } else {
                        Log.d(
                            "pdfParserActivity",
                            "werkt niet"
                        ) // TODO depending on reason either save the thing to be sent when internet is available or show an error for bad file
                        runOnUiThread {
                            longToast("Error parsing roster")
                        }
                    }
                    startMainActivity(App.instance)
                    finish()
                }

            }

        }
    }

}
