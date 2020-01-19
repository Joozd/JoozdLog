package nl.joozd.logbooktest1.ui.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_airport_picker.*
import nl.joozd.logbooktest1.R
import nl.joozd.logbooktest1.data.Airport
import nl.joozd.logbooktest1.extensions.ctx
import nl.joozd.logbooktest1.extensions.getActivity
import nl.joozd.logbooktest1.extensions.getColorFromAttr

class AirportPickerAdapter(private var airports: List<Airport>, private val itemClick: (Airport) -> Unit): RecyclerView.Adapter<AirportPickerAdapter.ViewHolder>() {
    private var pickedAirport = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportPickerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_airport_picker, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAirports(airports[position])
        val activity = holder.backgroundLayout.getActivity()
        activity?.let {
            if (airports[position].ident == pickedAirport) {
                holder.backgroundLayout.setBackgroundColor(it.getColorFromAttr(android.R.attr.colorPrimaryDark))
                holder.identifier.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondaryInverse))
                holder.cityName.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondaryInverse))
            }
            else{
                holder.backgroundLayout.setBackgroundColor(0x00000000)
                holder.identifier.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondary))
                holder.cityName.setTextColor(it.getColorFromAttr(android.R.attr.textColorSecondary))
            }
        }

    }

    override fun getItemCount(): Int = airports.size

    fun updateData(newAirports: List<Airport>) {
        airports = newAirports
        this.notifyDataSetChanged()
    }

    class ViewHolder(override val containerView: View, private val itemClick: (Airport) -> Unit) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun bindAirports(airport: Airport) {
            with(airport) {
                identifier.text = "$ident - $iata_code"
                cityName.text = "$municipality  - $name"
                itemView.setOnClickListener {
                    itemClick(this)
                }
            }

        }
    }

    fun pickAirport(airport: Airport){
        pickedAirport = airport.ident
    }
}
