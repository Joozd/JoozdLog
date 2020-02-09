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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main_new.*

import nl.joozd.joozdlog.R

import nl.joozd.joozdlog.comm.Comms

import nl.joozd.joozdlog.shared.Aircraft
import nl.joozd.joozdlog.shared.BalanceForward
import nl.joozd.joozdlog.data.Flight
import nl.joozd.joozdlog.data.db.*

import nl.joozd.joozdlog.ui.adapters.FlightsAdapter

import nl.joozd.joozdlog.data.enumclasses.ThingsToSync
import nl.joozd.joozdlog.data.utils.*
import nl.joozd.joozdlog.extensions.*
import nl.joozd.joozdlog.shared.utils.mostRecentCompleteFlight
import nl.joozd.joozdlog.shared.utils.reverseFlight
import nl.joozd.joozdlog.ui.fragments.EditFlightNew

import nl.joozd.joozdlog.ui.utils.CustomSnackbar
import nl.joozd.joozdlog.utils.FlightSearcher

import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {
    class ShowMenuListener(private val f: () -> Unit){
        fun go(){
            f()
        }
    }
    private val deleteListener: (Flight) -> Unit = { f ->
        if (f.planned) {
            val deletedFlight = f.copy(DELETEFLAG = 1, changed = 1)
            flightDb.saveFlight(deletedFlight)
            allFlights =
                allFlights.filter { flt -> flt.flightID != f.flightID } + listOf(deletedFlight)
            flightsAdapter.flights = allFlights

            // show UNDO snackbar
            val snackBar = CustomSnackbar.make(flightsList)
            snackBar.setOnActionBarShown { addButton.hide() }
            snackBar.setOnActionBarGone { addButton.show() }
            snackBar.setOnAction {
                flightDb.saveFlight(f)
                allFlights =
                    (allFlights.filter { flt -> flt.flightID != f.flightID } + listOf(f)).sortedBy { it.tOut }
                        .asReversed()
                flightsAdapter.flights = allFlights
                snackBar.dismiss()
            }
            snackBar.show()
        }
        else {
            alert( "Do you want to delete COMPLETED flight? This might be illegal.", "WARNING"){
                yesButton {
                    val deletedFlight = f.copy(DELETEFLAG = 1, changed = 1)
                    flightDb.saveFlight(deletedFlight)
                    allFlights = allFlights.filter{flt -> flt.flightID != f.flightID} + listOf(deletedFlight)
                    flightsAdapter.flights = allFlights

                    // show UNDO snackbar
                    val snackBar = CustomSnackbar.make(flightsList)
                    snackBar.setOnActionBarShown { addButton.hide() }
                    snackBar.setOnActionBarGone { addButton.show() }
                    snackBar.setOnAction {
                        flightDb.saveFlight(f)
                        allFlights = (allFlights.filter{flt -> flt.flightID != f.flightID} + listOf(f)).sortedBy { it.tOut }.asReversed()
                        flightsAdapter.flights = allFlights
                        snackBar.dismiss()
                    }
                    snackBar.show()
                }
                noButton{ }
            }.show()


        }
    }

    val fragmentManager = supportFragmentManager

    private val flightDb = FlightDb()
    private val airportDb = AirportDb()
    private val aircraftDb = AircraftDb()
    private val balanceForwardDb = BalanceForwardDb()
    private val signatureDb = SignatureDb()
    var allFlights: List<Flight> = emptyList()
    set(value){
        //met dank aan LuukLuuk!
        field=value
        runOnUiThread {
            supportActionBar?.title = "${value.filter{it.DELETEFLAG == 0}.size} Flights"
        }
    }
    var allAircraft: List<Aircraft> = emptyList()
    private val flightsAdapter: FlightsAdapter = FlightsAdapter(allFlights, deleteListener) { showFlight(it) }
    private val flightSearcher = FlightSearcher(flightDb, airportDb, aircraftDb)
    private var namesWorker:NamesWorker = NamesWorker()
    private var showTotalTimesMenuListener: ShowMenuListener? = null
    private var showBalanceForwardListener: ShowMenuListener? = null
    private var balancesForward: List<BalanceForward> = emptyList()
    private var alreadyEditingFlight: Boolean = false
    private var foundMissingAircraft: List<Aircraft> = emptyList()

    private var snackbarShowing: CustomSnackbar? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_rebuild -> {
            val ENABLE_REBUILD = true
            if (ENABLE_REBUILD) {
                doAsync {
                    allFlights = Comms().rebuildFromServer()
                    runOnUiThread {
                        flightDb.clearDB()
                        flightDb.saveFlights(allFlights)
                        toast("Database rebuilt. Restarting.")
                        this@MainActivity.recreate()
                    }
                }
            }
            else{
                toast("Disabled!")
                /* //REMOVE BELOW THIS//
                val allAirports = airportDb.requestAllAirports()
                val lll = mutableListOf<Flight>()
                allFlights.forEach{f ->
                    f.actualAircraft = allAircraft.firstOrNull{ac -> ac.registration == f.registration }
                    val twilightCalculator = TwilightCalculator(
                        LocalDateTime.of(
                            f.tOut.toLocalDate(),
                            f.tOut.toLocalTime()
                        )
                    )
                    val orig = allAirports.firstOrNull{it.ident == f.orig }
                    val dest = allAirports.firstOrNull{it.ident == f.dest }
                    val augmentedFactor = f.correctedDuration.toMinutes().toDouble() / f.duration.toMinutes().toDouble()

                    val nightTime = ((twilightCalculator.minutesOfNight(orig, dest, f.tOut, f.tIn)* augmentedFactor)+0.5).toInt()

                    lll.add (f.copy(ifrTime = f.correctedDuration.toMinutes().toInt(), nightTime = nightTime, changed = 1))
                    if (lll.size % 300 == 0) Log.d("working...", "${lll.size} flights done")
                }
                flightDb.saveFlights(lll)
                toast("DONe!")
                 */ //REMOVE ABOVE THIS//
            }
            true
        }
        R.id.menu_add_flight -> {
            showFlight(null, true)
            true
        }
        R.id.menu_total_times -> {
            showTotalTimesMenuListener?.go()
            true
        }
        R.id.menu_balance_forward -> {
            showBalanceForwardListener?.go()
            true
        }
        R.id.app_bar_search -> {
            if (searchField.visibility == View.VISIBLE){
                searchField.visibility = View.GONE
                pilotNameSearch.setText("")
                airportSearch.setText("")
                aircraftSearch.setText("")
                addButton.show()
            }
            else {
                if (flightSearcher.initialized) {
                    searchField.visibility = View.VISIBLE
                    addButton.hide()
                }
                else toast("please wait, initializing search")
            }
            true
        }
        R.id.menu_edit_aircraft -> {
            val intent = Intent(this, EditAircraftActivity::class.java)
            intent.putExtra("missingAircraft", foundMissingAircraft.toTypedArray())
            intent.putExtra("allAircraft", allAircraft.toTypedArray())
            startActivity(intent)

            true
        }
        R.id.menu_export_pdf -> {
            val intent = Intent(this, ExportPdfActivity::class.java)
            startActivity(intent)

            true
        }
        R.id.menu_do_something -> {
            val intent = Intent(this, SaveToDriveActivity::class.java)
            startActivity(intent)

            true
        }

        else -> false

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allFlights = (flightDb.requestAllFlights()).sortedBy { it.tOut }.asReversed()
        balancesForward=balanceForwardDb.requestAllBalanceForwards()
        allAircraft = (aircraftDb.requestAllAircraft())

        // start initializing search functionality
        doAsync{
            namesWorker.initialize(allFlights)
            flightSearcher.init(allFlights)
            airportDb.requestAllAirports() // fill its cache, wont hurt
              // put all names of all flights into namesWorker
        }

        // TODO preferences check if this is wanted

        setTheme(R.style.AppTheme)

        setContentView(R.layout.activity_main_new) // .activity_main_with_search
        setSupportActionBar(my_toolbar as Toolbar)


        val layoutManager = LinearLayoutManager(this)
        flightsList.layoutManager = layoutManager
        flightsList.adapter = flightsAdapter
        flightsAdapter.flights=allFlights


        supportActionBar?.title="${allFlights.filter{it.DELETEFLAG == 0}.size} Flights"

        // start synching with server
        doAsync {
            syncWithServer(flightsAdapter, listOf(ThingsToSync.FLIGHTS, ThingsToSync.AIRCRAFT, ThingsToSync.AIRPORTS))
        }

        /**
         * fill list of missing aircraft (async)
         * a "missing aircraft" is one where registratin is in flights, but not in allAircraft
         */
        doAsync {
            foundMissingAircraft = findMissingaircraftInFlights(allFlights)
        }

        addButton.setOnClickListener{
            showFlight(null, true)
        }

        var nameSearchString: String? = null
        var airportSearchString: String? = null
        var aircraftSearchString: String? = null

        pilotNameSearch.onTextChanged {
            nameSearchString = if (it.isNotEmpty()) it else null
            flightsAdapter.flights=flightSearcher.search(nameSearchString, aircraftSearchString, airportSearchString)
            supportActionBar?.title="${flightsAdapter.flights.size} Flights"
        }
        airportSearch.onTextChanged {
            airportSearchString = if (it.isNotEmpty()) it else null
            flightsAdapter.flights=flightSearcher.search(nameSearchString, aircraftSearchString, airportSearchString)
            supportActionBar?.title="${flightsAdapter.flights.size} Flights"
        }
        aircraftSearch.onTextChanged {
            aircraftSearchString = if (it.isNotEmpty()) it else null
            flightsAdapter.flights=flightSearcher.search(nameSearchString, aircraftSearchString, airportSearchString)
            supportActionBar?.title="${flightsAdapter.flights.size} Flights"
        }

        showTotalTimesMenuListener = ShowMenuListener{
            val intent = Intent(this, TotalTimesActivity::class.java)
            val totalTimesCalculator = TotalTimesCalculator()
            totalTimesCalculator.balancesForward = balancesForward
            totalTimesCalculator.flightsList = allFlights
            intent.putExtra("totalTimes", totalTimesCalculator.totalsData.toTypedArray())
            startActivity(intent)
        }
        showBalanceForwardListener = ShowMenuListener{
            val intent = Intent(this, BalanceForwardActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        balancesForward=balanceForwardDb.requestAllBalanceForwards()
    }


    private fun syncWithServer(flightsAdapter: FlightsAdapter, thingsToSync: List<ThingsToSync>) {
/*        //TODO tempo to fix stuff
        val fixedFlights: MutableList<Flight> = mutableListOf()
        Log.d("fixing", "${allFlights.size} flights")
        var counter = 0
        allFlights.forEach {
            fixedFlights.add(flightAirportToIcao(it).copy(changed = 1))
            counter += 1
            if (counter %100 == 1)
                Log.d("Count", "$counter")
        }
        allFlights=fixedFlights.toList()
        flightDb.saveFlights(allFlights)
        Log.d("fixed", "${fixedFlights.size} flights")
        Log.d("check", "${allFlights.size} flights")
        */
        val comms = Comms()
        if (ThingsToSync.FLIGHTS in thingsToSync) {
            Log.d("SyncWithServer:", "FLIGHTS")
            val newFlights = comms.getUpdates()
            if (newFlights.isNotEmpty()) {
                flightDb.saveFlights(airportsToIcao(newFlights))
                allFlights = (flightDb.requestAllFlights()).sortedBy { it.tOut }.asReversed()
                runOnUiThread{
                    toast("Updated flights from server")
                    flightsAdapter.flights=allFlights
                }
            }
            if (comms.sendUpdates(allFlights)) {
                val updatedflights = (allFlights.filter { it.changed > 0 })
                if (updatedflights.isNotEmpty()) {
                    var flightsToSave = emptyList<Flight>()
                    updatedflights.forEach {
                        flightsToSave += it.copy(changed = 0)
                    }
                    flightDb.saveFlights(airportsToIcao(flightsToSave))
                    runOnUiThread{
                        toast("sent updated flights to server")
                    }
                }
            }
            if (!comms.checkifsynched(flightDb.getSumOfAllIds())) {
                runOnUiThread {
                    toast("out of sync! trying to resync...")
                }
                flightDb.clearDB()
                flightDb.saveFlights(airportsToIcao(comms.rebuildFromServer()))
                allFlights = (flightDb.requestAllFlights()).sortedBy { it.tOut }.asReversed()
                runOnUiThread {
                    this.recreate()
                }
            }
            var deletedFlights=false
            allFlights.forEach {
                if (it.DELETEFLAG>0 && it.changed == 0) flightDb.deleteFlight(it); deletedFlights=true
            }
            if (deletedFlights) {
                allFlights = (flightDb.requestAllFlights()).sortedBy { it.tOut }.asReversed()
                runOnUiThread {
                    flightsAdapter.flights = allFlights
                }
            }
        }

        if (ThingsToSync.AIRCRAFT in thingsToSync) {
            Log.d("SyncWithServer:", "AIRCRAFT")
            Log.d("Joozd nu", "11111")
            val newAircraft = comms.checkAircraft(aircraftDb.highestId)
            Log.d("Joozd nu", "22222")
            runOnUiThread{
                newAircraft?.let { toast("New aircraft downloaded") }
            }
            newAircraft?.let {aircraftDb.saveAircraft(it)}
            Log.d("Joozd nu", "33333")
        }

        if (ThingsToSync.AIRPORTS in thingsToSync) {
            Log.d("SyncWithServer:", "AIRPORTS")
            val newAirports = comms.checkAirports(airportDb.highestId)
            runOnUiThread {
                newAirports?.let { toast("New airports downloaded") }
            }
            newAirports?.let { airportDb.saveAirports(it) }
            comms.close()
        }
    }

    private fun showFlight(flight: Flight?, newFlight: Boolean = false) { // new flight gets (null, true)
        if (!alreadyEditingFlight) {
            alreadyEditingFlight = true
            //hide snackbar if showing:
            snackbarShowing?.dismiss()

            //hide toolbar:
            my_toolbar.visibility = View.GONE
            doAsync {
                val flightEditor = EditFlightNew()

                Log.d("DEBUG", "1")

                //set actions when closing dialog with SAVE:
                flightEditor.onSave = EditFlightNew.OnSave { flightToSave, oldFlight, oldSignature ->
                    alreadyEditingFlight = false
                    flightDb.saveFlight(flightToSave)
                    flightsAdapter.insertFlight(flightToSave)
                    supportActionBar?.title = "${flightsAdapter.flights.size} Flights"
                    flightSearcher.addFlight(flightToSave)
                    namesWorker.addFlight(flightToSave)
                    Log.d("DEBUG", "2")
                    allFlights =
                        (allFlights.filter { it.flightID != flightToSave.flightID } + flightToSave).sortedBy { it.tOut }
                            .asReversed()
                    flightDb.updateCachedFlights(allFlights)
                    flightsAdapter.flights = allFlights
                    Log.d("DEBUG", "3")

                    //define snackbar to be displayed when dialog is closed with SAVE
                    val snackBar = CustomSnackbar.make(mainActivityLayout)
                    snackBar.setMessage("Saved Flight")
                    if (newFlight)
                        snackBar.setOnAction {
                            Log.d("onAction", "ding 1" )
                            flightDb.deleteFlight(flightToSave)
                            allFlights = allFlights.filter { flt -> flt.flightID != flightToSave.flightID }
                            flightsAdapter.flights = allFlights
                            snackBar.dismiss()
                        }
                    else
                        snackBar.setOnAction {//onAction = UNDO
                            Log.d("onAction", "ding 2" )
                            flightDb.saveFlight(oldFlight)
                            oldSignature?.let {
                                signatureDb.setSignature(oldFlight.flightID, it)
                            }
                            allFlights = (allFlights.filter { flt -> flt.flightID != flightToSave.flightID } + listOf(oldFlight)).sortedBy { it.tOut }.asReversed()
                            flightsAdapter.flights = allFlights
                            snackBar.dismiss()
                        }
                    snackBar.setOnActionBarShown { addButton.hide() }
                    snackBar.setOnActionBarGone {
                        snackbarShowing = null
                        if (!alreadyEditingFlight) addButton.show()
                    }
                    snackbarShowing = snackBar
                    snackBar.show()


                    my_toolbar.showAnimated()
                    //make sure search field is closed and emptied
                    if (searchField.visibility != View.GONE) {
                        pilotNameSearch.text = pilotNameSearch.text
                        airportSearch.setText("")
                        aircraftSearch.setText("")
                    }
                }
                Log.d("DEBUG", "4")

                flightEditor.onCancel = EditFlightNew.OnCancel { canceledFlight, _ ->
                    alreadyEditingFlight = false

                    val snackBar = CustomSnackbar.make(mainActivityLayout)
                    snackBar.setMessage("Cancelled")
                    snackBar.setActionText("Continue")
                    snackBar.setOnAction {
                        showFlight(canceledFlight)
                        snackBar.dismiss()
                    }
                    snackBar.setOnActionBarShown { addButton.hide() }
                    snackBar.setOnActionBarGone {
                        snackbarShowing = null
                        if (!alreadyEditingFlight) addButton.show() }
                    snackbarShowing = snackBar
                    snackBar.show()

                    my_toolbar.showAnimated()


                }
                Log.d("DEBUG", "5")

                flightEditor.lastCompletedFlight =
                    mostRecentCompleteFlight(
                        allFlights
                    )
                flightEditor.flight =flight ?: reverseFlight(
                    flightEditor.lastCompletedFlight!!,
                    flightDb.highestId + 1
                )

                if (namesWorker.isInitialized) flightEditor.namesWorker = namesWorker
                else {
                    namesWorker.initializationListener = NamesWorker.InitializationListener {
                        flightEditor.namesWorker = namesWorker
                    } // if (or when) namesWorker is done making its names, put it into  EditFlight fragment
                }
                Log.d("DEBUG", "6")


                supportFragmentManager.beginTransaction()
                    .add(R.id.mainActivityLayout, flightEditor)
                    .addToBackStack(null)
                    .commit()
                Log.d("DEBUG", "7")
                addButton.fadeOut()
                Log.d("DEBUG", "8")
            }
        }
    }

    /**
     * fill list of missing aircraft (async)
     * a "missing aircraft" is one where registratin is in flights, but not in allAircraft
     */
    private fun findMissingaircraftInFlights(flights: List<Flight>): List<Aircraft>{
        val foundAircraft = mutableListOf<Aircraft>()
        val knownRegistrations: List<String> = allAircraft.map{ac -> ac.registration}
        val knownModels: List<String> = allAircraft.map{ac -> ac.model}
        var highestID = aircraftDb.highestId +1
        flights.forEach{f ->
            if (f.registration !in knownRegistrations + foundAircraft.map{ac -> ac.registration}) {
                if (f.aircraft in knownModels)
                    foundAircraft.add(aircraftDb.searchRegAndType(type=f.aircraft).first().copy(id = highestID, registration = f.registration))
                else
                    foundAircraft.add(
                        Aircraft(
                            highestID,
                            f.registration,
                            "",
                            f.aircraft,
                            "",
                            0,
                            0,
                            0,
                            if (f.name2.isNotEmpty()) 1 else 0,
                            if (f.ifrTime > 0) 1 else 0
                        )
                    )
                highestID += 1
            }
        }
        return foundAircraft
    }

}
