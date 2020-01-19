package nl.joozd.logbooktest1.data.utils

import android.util.Log
import nl.joozd.logbooktest1.data.BalanceForward
import nl.joozd.logbooktest1.data.Flight
import nl.joozd.logbooktest1.data.miscClasses.TotalsListGroup
import nl.joozd.logbooktest1.data.miscClasses.TotalsListItem

class TotalTimesCalculator(initialFlightsList: List<Flight> = emptyList(), initialBalancesForward: List<BalanceForward> = emptyList()){
    companion object{
        const val TAG = "TotalTimesCalculator"
    }
    var flightsList: List<Flight> = emptyList()
    set(value){
        field=value
        fillData(flightsList, balancesForward)
    }
    var balancesForward: List<BalanceForward> = initialBalancesForward
    set(value){
        field=value
        fillData(flightsList, balancesForward)
        Log.d(TAG, "${balancesForward.size} balances forward")
    }
    var totalsData: List<TotalsListGroup> = emptyList()
    var initialized = false

    init{
        flightsList = initialFlightsList
    }


    private fun fillData(flightsWithPlanned: List<Flight>, balancesForward: List<BalanceForward>) {
        initialized=false
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
                timePerYear.add(TotalsListItem(it.toString(), flights.filter{ f -> f.tOut.year==it}.map{ f-> f.correctedDuration.seconds/60}.sum()))
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