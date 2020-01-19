package nl.joozd.logbooktest1.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main_new.view.*
import kotlinx.android.synthetic.main.picker_aircraft.*
import kotlinx.android.synthetic.main.picker_aircraft.view.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.Aircraft
import nl.joozd.logbooktest1.data.db.AircraftDb
import nl.joozd.logbooktest1.extensions.getColorFromAttr
import nl.joozd.logbooktest1.extensions.onTextChanged
import nl.joozd.logbooktest1.ui.adapters.AircraftPickerAdapter


class AircraftPicker : Fragment() {
    companion object{
        const val TAG = "AircraftPicker"
    }
    class FlightSelectedListener (private val f: (aircraft: Aircraft) -> Unit){
        fun flightSelected(aircraft: Aircraft){
            f(aircraft)
        }
    }
    private lateinit var thisView: View
    private var viewReady = false


    private val aircraftDB = AircraftDb()
    var allAircraft = aircraftDB.requestAllAircraft()
    // var allAircraft: List<Aircraft> = emptyList()
    var setAircraft: Aircraft?
    get(){
        return pickedAircraft
    }
    set(ac){
        pickedAircraft = ac
        if (viewReady){
            ac?.let { ac ->
                setPickedAircraft(thisView, ac)
            }
        }
    }
    private var pickedAircraft: Aircraft? = null
    var flightSelectedListener: FlightSelectedListener? = null
    private val aircraftPickerAdapter = AircraftPickerAdapter(allAircraft) {ac ->
        pickedAircraft = ac
        setPickedAircraft(thisView, ac)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // allAircraft = aircraftDB.requestAllAircraft()
        aircraftPickerAdapter.allAircraft = allAircraft
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.picker_aircraft, container,            false)

        (thisView.aircraftPickerTopHalf.background as GradientDrawable).colorFilter = PorterDuffColorFilter(activity!!.getColorFromAttr(android.R.attr.colorPrimary), PorterDuff.Mode.SRC_IN)

        thisView.aircraftPickerList.layoutManager = LinearLayoutManager(context)
        thisView.aircraftPickerList.adapter = aircraftPickerAdapter

        thisView.aircraftPickerLayout.setOnClickListener { fragmentManager?.popBackStack() }

        thisView.aircraftPickerCancel.setOnClickListener { fragmentManager?.popBackStack() }

        thisView.aircraftPickerOk.setOnClickListener {
            pickedAircraft?.let { flightSelectedListener?.flightSelected(it) }
            fragmentManager?.popBackStack()
        }
        Log.d(TAG, "pickedAircraft is $pickedAircraft")
        pickedAircraft?.let{
            setPickedAircraft(thisView, it)
        }
        thisView.aircraftSearchField.onTextChanged { text ->
            aircraftPickerAdapter.allAircraft =
                if (text.isEmpty()) allAircraft
                else (allAircraft.filter {text.toUpperCase() in it.registration.toUpperCase()}.sortedBy { it.registration } + allAircraft.filter { text.toUpperCase() in it.manufacturer.toUpperCase() || text.toUpperCase() in it.model.toUpperCase()}).distinct()
        }


        viewReady = true
        return thisView
    }
    private fun setPickedAircraft(v: View, ac: Aircraft){
        v.pickedRegistrationText.text = ac.registration
        v.pickedManufacturerTextView.text = ac.manufacturer
        v.pickedModelTextView.text = ac.model
        v.pickedMpSpTextView.text = if(ac.multipilot > 0) "MP" else "SP"
        v.pickedMeSeTextView.text = if(ac.me > 0) "ME" else "SE"
        v.pickedEngineTypeTextView.text = ac.engine_type
        v.pickedIFRTextView.visibility = if (ac.isIfr > 0) View.VISIBLE else View.INVISIBLE
        aircraftPickerAdapter.pickAircraft(ac)
        aircraftPickerAdapter.notifyDataSetChanged()
    }

    fun setAircraft(registration: String){
        val reg = if ('(' in registration) registration.slice(0 until registration.indexOf('(')) else registration
        setAircraft = allAircraft.firstOrNull { it.registration == reg }
    }


}
