package nl.joozd.logbooktest1.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_aircraft_picker.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.Aircraft
import nl.joozd.logbooktest1.extensions.ctx
import nl.joozd.logbooktest1.extensions.getActivity
import nl.joozd.logbooktest1.extensions.getColorFromAttr

class AircraftPickerAdapter(allAircraft: List<Aircraft>, private val itemClick: (Aircraft) -> Unit): RecyclerView.Adapter<AircraftPickerAdapter.ViewHolder>() {
    var allAircraft: List<Aircraft> = allAircraft
    set(acList){
        field = acList
        notifyDataSetChanged()
    }
    private var pickedAircraft = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_aircraft_picker, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAircraft(allAircraft[position])
        val activity = holder.backgroundLayout.getActivity()
        activity?.let {
            if (allAircraft[position].registration == pickedAircraft) {
                holder.backgroundLayout.setBackgroundColor(it.getColorFromAttr(android.R.attr.colorPrimaryDark))
                holder.makeModelText.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondaryInverse))
                holder.registrationText.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondaryInverse))
            }
            else {
                holder.backgroundLayout.setBackgroundColor(0x00000000)
                holder.makeModelText.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondary))
                holder.registrationText.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondary))
            }
        }
    }

    override fun getItemCount(): Int = allAircraft.size

    class ViewHolder(override val containerView: View, private val itemClick: (Aircraft) -> Unit) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindAircraft(aircraft: Aircraft) {
            with(aircraft) {
                registrationText.text = registration
                makeModelText.text = "$manufacturer $model"
                itemView.setOnClickListener {
                    itemClick(this)
                }
            }

        }
    }
    fun pickAircraft(ac: Aircraft){
        pickedAircraft = ac.registration
    }
}
