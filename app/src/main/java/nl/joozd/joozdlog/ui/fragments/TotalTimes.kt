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

package nl.joozd.joozdlog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.total_times.view.*
import nl.joozd.joozdlog.R
import nl.joozd.joozdlog.shared.BalanceForward
import nl.joozd.joozdlog.data.Flight
import nl.joozd.joozdlog.data.db.AircraftDb
import nl.joozd.joozdlog.data.db.AirportDb
import nl.joozd.joozdlog.data.db.FlightDb
import nl.joozd.joozdlog.data.miscClasses.TotalsListGroup
import nl.joozd.joozdlog.data.miscClasses.TotalsListItem
import nl.joozd.joozdlog.ui.adapters.TotalTimesExpandableListAdapter

class TotalTimes : androidx.fragment.app.Fragment() {
    class OnStartListener(private val f: () -> Unit){
        fun starting(){
            f()
        }
    }
    class OnStopListener(private val f: () -> Unit){
        fun stopping(){
            f()
        }
    }

    var initialized = false
    private val flightDB = FlightDb()
    private val airportDb = AirportDb()
    private val aircraftDb = AircraftDb()
    private lateinit var thisView: View
    private var adapter: TotalTimesExpandableListAdapter? = null
    private lateinit var totalsData: List<TotalsListGroup>
    var onStart: OnStartListener? = null
    var onStop: OnStopListener? = null


    // trigger listeners onStart and onStop, in case something needs to be done (spoiler: something needs to be done)
    override fun onStart() {
        onStart?.starting()
        super.onStart()
    }

    override fun onDestroyView() {
        onStop?.stopping()
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thisView = inflater.inflate(R.layout.total_times, container, false)

        val expandableList: ExpandableListView = thisView.findViewById(R.id.expandible_listview)
        adapter = TotalTimesExpandableListAdapter(requireActivity(), totalsData as MutableList<TotalsListGroup>) // maybe fill async and load while expanding items
        expandableList.setAdapter(adapter)

        thisView.backButton.setOnClickListener { fragmentManager?.popBackStack() }

        return thisView
    }

    fun fillData(flightsWithPlanned: List<Flight>, balancesForward: List<BalanceForward>) {
        // Will create all kinds of fun data to display! Add any you like!
        val totalsListGroupList: MutableList<TotalsListGroup> = mutableListOf()
        val flights = flightsWithPlanned.filter{it.isPlanned==0}
        totalsListGroupList.add(
            TotalsListGroup(
                "Grand Totals",
                listOf(
                    TotalsListItem("Total Time:", flights.filter{it.isSim == 0}.map{it.correctedDuration.seconds/60}.sum() + flights.map{it.simTime}.sum() + balancesForward.map{it.grandTotal}.sum()),
                    TotalsListItem("Aircraft:", flights.filter{it.isSim == 0}.map{it.correctedDuration.seconds/60}.sum() + balancesForward.map{it.aircraftTime}.sum()),
                    TotalsListItem("Simulator:", flights.map{it.simTime}.sum().toLong() + balancesForward.map{it.simTime}.sum()),
                    TotalsListItem("IFR Time:", flights.map{it.ifrTime}.sum().toLong() + balancesForward.map{it.ifrTime}.sum()), // shouldn't be in grand totals I guess? Function time?
                    TotalsListItem("PIC Time:", flights.map{it.isPIC}.sum().toLong() + balancesForward.map{it.picTime}.sum())
                )
            )
        )

        //time per type
        val aircraftTypes= flights.filter{it.isSim==0}.map{it.aircraft}.distinct()
        val timePerType: MutableList<TotalsListItem> = mutableListOf()
        aircraftTypes.forEach{
            timePerType.add(TotalsListItem(it, flights.filter{ f -> f.aircraft == it}.map{ f -> f.correctedDuration.seconds/60}.sum()))
        }
        totalsListGroupList.add(
            TotalsListGroup(
                "Aircraft types",
                timePerType.toList()
            )
        )

        //time per reg:
        val aircraftRegs = flights.filter{it.isSim==0}.map{it.registration}.distinct().sorted()
        val timePerRegistration: MutableList<TotalsListItem> = mutableListOf()
        aircraftRegs.forEach {
            timePerRegistration.add(TotalsListItem(it, flights.filter{ f -> f.registration == it}.map{ f -> f.correctedDuration.seconds/60}.sum()))
        }
        totalsListGroupList.add(
            TotalsListGroup(
                "Aircraft Registration",
                timePerRegistration.toList()
            )
        )

        // time Per Year:
        if (flights.isNotEmpty()){
            val timePerYear: MutableList<TotalsListItem> = mutableListOf()
            (flights.minBy{it.tOut}!!.tOut.year .. flights.maxBy { it.tOut }!!.tOut.year).forEach{
                timePerYear.add(TotalsListItem(it.toString(), flights.filter{f -> f.tOut.year==it}.map{f-> f.correctedDuration.seconds/60}.sum()))
            }
            totalsListGroupList.add(
                TotalsListGroup(
                    "Time per year",
                    timePerYear.toList()
                )
            )
        }

        initialized=true
        totalsData = totalsListGroupList
    }
}

