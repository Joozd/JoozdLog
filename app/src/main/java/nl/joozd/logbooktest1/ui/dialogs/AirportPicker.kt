package nl.joozd.logbooktest1.ui.dialogs

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_airports.view.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.Airport
import nl.joozd.logbooktest1.data.db.AirportDb
import nl.joozd.logbooktest1.extensions.getColorFromAttr
import nl.joozd.logbooktest1.extensions.onTextChanged
import nl.joozd.logbooktest1.ui.adapters.AirportPickerAdapter
import nl.joozd.logbooktest1.utils.makeAirport
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import kotlin.math.abs

class AirportPicker: androidx.fragment.app.Fragment() {
    class AirportSelectedListener(private val f: (airport: Airport) -> Unit){
        fun airportSelected(airport: Airport){
            f(airport)
        }
    }
    var onSelectListener: AirportSelectedListener? = null
    var airportDb: AirportDb? = null        // to be filled before inflating with an initialized NamesWorker
    var selectedAirportIdent = ""            // holds Airport.ident if filled properly
    private var selectedAirport: Airport? = null
    var airportsList: List<Airport> = emptyList()
    private val airportPickerAdapter = AirportPickerAdapter(airportsList) { airport ->
        selectedAirport = airport
        setPickedFlight(thisView, airport)
    }
    private lateinit var thisView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.dialog_airports, container, false)

        if (selectedAirportIdent.isNotEmpty()) {
            doAsync {
                val foundAirport = airportDb?.searchAirport(selectedAirportIdent)
                thisView.context.runOnUiThread {
                    setPickedFlight(thisView, foundAirport)
                }
            }
        }


        val airportsDialogTopHalf: ConstraintLayout = thisView.airportsDialogTopHalf
        (airportsDialogTopHalf.background as GradientDrawable).colorFilter = PorterDuffColorFilter(activity!!.getColorFromAttr(android.R.attr.colorPrimary), PorterDuff.Mode.SRC_IN)

        thisView.airportPickerDialogLayout.setOnClickListener {  }

        thisView.airportPickerLayout.setOnClickListener { fragmentManager?.popBackStack() }

        thisView.airportsPickerList.layoutManager = LinearLayoutManager(context)
        thisView.airportsPickerList.adapter = airportPickerAdapter


        //Search field changed and initial fill:
        var alreadySearching: Boolean = true
        var nextSearchString: String = ""
        var haveOneWaiting: Boolean = false

        airportDb?.let { db ->
            doAsync {
                var foundAirports: List<Airport> = db.requestAllAirports()
                thisView.context.runOnUiThread {
                    Log.d("airportPicker", "found ${foundAirports.size} airports")
                    airportPickerAdapter.updateData(foundAirports)
                }
                while (haveOneWaiting){
                    haveOneWaiting = false
                    foundAirports = if (nextSearchString.isNotEmpty()) db.searchAirports(nextSearchString) else db.requestAllAirports()
                    thisView.context.runOnUiThread {
                        airportPickerAdapter.updateData(foundAirports)
                    }
                }
                alreadySearching = false
            }
        }


        thisView.airportsSearchField.onTextChanged { t ->
            airportDb?.let {db ->
                if (alreadySearching) {
                    haveOneWaiting = true
                    nextSearchString = t
                }
                else {
                    doAsync {
                        alreadySearching = true
                        var foundAirports: List<Airport> = db.searchAirports(t)
                        thisView.context.runOnUiThread {
                            Log.d("airportPicker", "found ${foundAirports.size} airports")
                            airportPickerAdapter.updateData(foundAirports)
                        }
                        while (haveOneWaiting){
                            haveOneWaiting = false
                            foundAirports = if (nextSearchString.isNotEmpty()) db.searchAirports(nextSearchString) else db.requestAllAirports()
                            thisView.context.runOnUiThread {
                                airportPickerAdapter.updateData(foundAirports)
                            }
                        }
                        alreadySearching = false
                    }
                }
            }

        }

        thisView.setCurrentTextButton.setOnClickListener {
            if (thisView.airportsSearchField.text.toString().isNotEmpty()) {
                selectedAirport = makeAirport(ident = thisView.airportsSearchField.text.toString(), name = "Custom airport")
                setPickedFlight(thisView, selectedAirport)
            }
        }


        // Save/Cancel onClickListeners:
        thisView.saveAirportDialog.setOnClickListener{
            selectedAirport?.let {
                onSelectListener?.airportSelected(selectedAirport!!)
            }
            fragmentManager?.popBackStack()
        }

        thisView.cancelAirportDialog.setOnClickListener { fragmentManager?.popBackStack() }

        return thisView
    }
    @SuppressLint("SetTextI18n")
    private fun setPickedFlight(view: View, airport: Airport?) {
        airport?.let {airport ->
            with(view) {
                icaoIataField.text = "${airport.ident} - ${airport.iata_code}"
                cityAirportNameField.text = "${airport.municipality} - ${airport.name}"
                val latString = "${abs(airport.latitude_deg).toInt().toString().padStart(
                    2,
                    '0'
                )}.${(airport.latitude_deg % 1).toString().drop(2).take(3)}${if (airport.latitude_deg > 0) "N" else "S"}"
                val lonString = "${abs(airport.longitude_deg).toInt().toString().padStart(
                    3,
                    '0'
                )}.${(airport.longitude_deg % 1).toString().drop(2).take(3)}${if (airport.longitude_deg > 0) "E" else "W"}"
                latLonField.text = "$latString - $lonString"
                altitudeField.text = "alt: ${airport.elevation_ft}\'"
            }
            airportPickerAdapter.pickAirport(airport)
            airportPickerAdapter.notifyDataSetChanged()
        }
    }
}