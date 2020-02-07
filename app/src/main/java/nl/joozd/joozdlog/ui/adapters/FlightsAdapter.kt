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

package nl.joozd.joozdlog.ui.adapters


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_flight.*

import nl.joozd.joozdlog.R
import nl.joozd.joozdlog.data.Flight
import android.widget.TextView
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import nl.joozd.joozdlog.data.db.AirportDb
import nl.joozd.joozdlog.data.miscClasses.CrewValue
import nl.joozd.joozdlog.data.utils.flightAirportsToIata
import nl.joozd.joozdlog.extensions.*
import nl.joozd.joozdlog.extensions.noColon
import nl.joozd.joozdlog.extensions.toMonthYear


class FlightsAdapter(enteredFlights: List<Flight>, private val deleteListener: (Flight) -> Unit, private val itemClick: (Flight) -> Unit) : RecyclerView.Adapter<FlightsAdapter.ViewHolder>(),
    RecyclerViewFastScroller.OnPopupTextUpdate {
    companion object {
        private var normalColor: Int = 0
        private var plannedColor: Int = 0
        private val airportDb = AirportDb()
    }

    private lateinit var mRecyclerView: RecyclerView

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.flightLayout.translationX = 0f
        super.onViewAttachedToWindow(holder)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    private val icaoIataPairs=airportDb.makeIcaoIataPairs()
    private var allFlights = flightAirportsToIata(enteredFlights, icaoIataPairs)
    private var savedFlights=allFlights
    var flights: List<Flight>
        set(newFlights){
            allFlights = flightAirportsToIata(newFlights.filter {it.DELETEFLAG == 0}, icaoIataPairs)
            this.notifyDataSetChanged()
            val firstNotPlanned=allFlights.indexOfFirst { it.isPlanned == 0 }
            mRecyclerView.scrollToPosition(if (firstNotPlanned > 3) firstNotPlanned - 3 else 0)
        }
    get() = allFlights



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // val view = LayoutInflater.from(parent.ctx).inflate(R.layout.another_item_flight, parent, false)
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_flight, parent, false)
        normalColor = parent.ctx.getColorFromAttr(android.R.attr.textColorSecondary)
        plannedColor = parent.ctx.getColorFromAttr(android.R.attr.textColorHighlight)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFlight(allFlights[position])
        holder.deleteLayer.setOnClickListener { deleteListener(allFlights[position]) }
        holder.flightLayout.setOnClickListener { itemClick(allFlights[position]) }
    }

    override fun getItemCount(): Int = allFlights.size


    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindFlight(flight: Flight) {
            with(flight) {
                for (c in 0 until flightLayout.childCount) {
                    val v: View = flightLayout.getChildAt(c)
                    if (v is TextView) {
                        v.setTextColor(if (planned) plannedColor else normalColor)
                    }
                }
                dateDayText.text = tOut.dayOfMonth.toString()
                dateMonthYearText.text = tOut.toMonthYear().toUpperCase()
                namesText.text = allNames
                aircraftTypeText.text = aircraft
                remarksText.text = remarks
                if (sim) {
                    flightNumberText.visibility = View.INVISIBLE
                    registrationText.visibility = View.INVISIBLE
                    arrow1.visibility = View.INVISIBLE
                    arrow2.visibility = View.INVISIBLE
                    timeOutText.visibility = View.INVISIBLE
                    timeInText.visibility = View.INVISIBLE
                    origText.visibility = View.INVISIBLE
                    destText.visibility = View.INVISIBLE
                    simText.visibility = View.VISIBLE
                    takeoffLandingText.visibility = View.INVISIBLE

                    totalTimeText.text = simTimeString
                } else {
                    flightNumberText.visibility = View.VISIBLE
                    registrationText.visibility = View.VISIBLE
                    arrow1.visibility = View.VISIBLE
                    arrow2.visibility = View.VISIBLE
                    timeOutText.visibility = View.VISIBLE
                    timeInText.visibility = View.VISIBLE
                    origText.visibility = View.VISIBLE
                    destText.visibility = View.VISIBLE
                    simText.visibility = View.INVISIBLE

                    takeoffLandingText.visibility = View.VISIBLE

                    flightNumberText.text = flightNumber
                    origText.text = orig
                    destText.text = dest
                    timeOutText.text = tOut.noColon()
                    totalTimeText.text = correctedTotalTimeString
                    timeInText.text = tIn.noColon()
                    registrationText.text = registration
                    takeoffLandingText.text = takeoffLanding
                }

                /*
                if (sim) isSimText.setLayoutToOn() else isSimText.setLayoutToOff()
                if (dual) isDualText.setLayoutToOn() else isDualText.setLayoutToOff()
                if (instructor) isInstructorText.setLayoutToOn() else isInstructorText.setLayoutToOff()
                if (picus) isPicusText.setLayoutToOn() else isPicusText.setLayoutToOff()
                if (pic) isPicText.setLayoutToOn() else isPicText.setLayoutToOff()
                if (pf) isPFText.setLayoutToOn() else isPFText.setLayoutToOff()
                */
                val crewValue = CrewValue.of(augmentedCrew)
                isSimText.visibility = if (sim) View.VISIBLE else View.GONE
                isAugmentedText.visibility = if (crewValue.crewSize <= 2) View.GONE else View.VISIBLE
                isIFRText.visibility= if (ifrTime > 0) View.VISIBLE else View.GONE
                isDualText.visibility = if (dual) View.VISIBLE else View.GONE
                isInstructorText.visibility = if (instructor) View.VISIBLE else View.GONE
                isPicusText.visibility = if (picus) View.VISIBLE else View.GONE
                isPicText.visibility = if (pic) View.VISIBLE else View.GONE
                isPFText.visibility = if (pf) View.VISIBLE else View.GONE
                remarksText.visibility = if (remarks.isEmpty()) View.GONE else View.VISIBLE
            }

        }
    }

    fun restoreFlights(){
        allFlights=savedFlights
        this.notifyDataSetChanged()
    }
    fun saveFlights(){
        savedFlights=allFlights
    }
    fun insertFlight(flight: Flight) {
        if (allFlights.firstOrNull { it.flightID == flight.flightID } == null) { // its a new flight!
            allFlights += flightAirportsToIata(listOf(flight), icaoIataPairs)
        } else {// its a known flight!
            allFlights = allFlights.map { if (flight.flightID == it.flightID) flightAirportsToIata(listOf(flight), icaoIataPairs).first() else it}
        }
        this.notifyDataSetChanged()
    }
    override fun onChange(position: Int): CharSequence {
        return allFlights[position].date
    }
}