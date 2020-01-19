package nl.joozd.logbooktest1.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_pdf_parser.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.comm.Comms
import nl.joozd.logbooktest1.data.Flight
import nl.joozd.logbooktest1.data.db.FlightDb
import nl.joozd.logbooktest1.data.utils.airportsToIcao
import nl.joozd.logbooktest1.extensions.toByteArray
import nl.joozd.logbooktest1.ui.App
import nl.joozd.logbooktest1.utils.startMainActivity
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
