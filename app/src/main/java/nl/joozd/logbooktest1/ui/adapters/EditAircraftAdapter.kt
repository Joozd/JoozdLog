package nl.joozd.logbooktest1.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_aircraft_edit.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.Aircraft
import nl.joozd.logbooktest1.data.AircraftWithNotes
import nl.joozd.logbooktest1.extensions.colorAllChildren
import nl.joozd.logbooktest1.extensions.ctx
import nl.joozd.logbooktest1.extensions.getColorFromAttr

class EditAircraftAdapter(allAircraft: List<Aircraft>, private var missingAircraft: List<Aircraft>, private val itemClick: (Aircraft) -> Unit): RecyclerView.Adapter<EditAircraftAdapter.ViewHolder>() {
    companion object{
        private var normalColor = 0
        private var highlightColor = 0
    }
    private var mergedArcraftList = missingAircraft.sortedBy{it.registration}.map{AircraftWithNotes(it,false)} + allAircraft.sortedBy { it.registration }.map{AircraftWithNotes(it,true)}

    var allAircraft = allAircraft
    set(it){
        field = it
        mergedArcraftList = missingAircraft.sortedBy{it.registration}.map{AircraftWithNotes(it,false)} + allAircraft.sortedBy { it.registration }.map{AircraftWithNotes(it,true)}
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditAircraftAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_aircraft_edit, parent, false)
        normalColor = parent.ctx.getColorFromAttr(android.R.attr.textColorSecondary)
        highlightColor = parent.ctx.getColorFromAttr(android.R.attr.textColorHighlight)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAircraft(mergedArcraftList[position])
    }

    override fun getItemCount(): Int = mergedArcraftList.size

    class ViewHolder(override val containerView: View, private val itemClick: (Aircraft) -> Unit) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun bindAircraft(aircraft: AircraftWithNotes) {
            with (aircraft){
                if (isKnown) aircraftItemLayout.colorAllChildren(normalColor)
                else aircraftItemLayout.colorAllChildren(highlightColor)

                Log.d("!!!!!!!!!!!!!!!!", "adding $registration")
                Log.d("!!!!!!!!!!!!!!!!", "$this")
                registrationText.text=registration
                makeModelText.text="$manufacturer $model"
                ifrText.visibility = if (isIfr > 0) View.VISIBLE else View.GONE
                engineTypeText.text=engine_type
                singleEngineText.visibility = if (se > 0) View.VISIBLE else View.GONE
                multiEngineText.visibility = if (me > 0) View.VISIBLE else View.GONE
                multiPilotText.visibility = if (multipilot > 0) View.VISIBLE else View.GONE
                aircraftItemLayout.setOnClickListener {
                    itemClick(this.toAircraft())
                }
            }
        }
    }

}