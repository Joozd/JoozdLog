package nl.joozd.logbooktest1.data.miscClasses

import nl.joozd.logbooktest1.extensions.getBit
import nl.joozd.logbooktest1.extensions.setBit

/************************************************************************************
 * CrewValue will store info on augmented crews:                                    *
 * bits 0-3: amount of crew (no crews >15 ppl :) )                                  *
 * bit 4: in seat on takeoff                                                        *
 * bit 5: in seat on landing                                                        *
 * bit 6-31: amount of time reserved for takeoff/landing (standard in settings)     *
 ************************************************************************************/
data class CrewValue(var crewSize: Int = 2,
                     val didTakeoff: Boolean = true,
                     val didLanding: Boolean = true,
                     val takeoffLandingTimes: Int = 0)
{
    companion object {
        fun of(value: Int) = if (value == 0) CrewValue() else CrewValue(
            crewSize = 15.and(value),
            didTakeoff = value.getBit(4),
            didLanding = value.getBit(5),
            takeoffLandingTimes = value.ushr(6)
        )
        fun of(crewSize: Int, didTakeoff: Boolean, didLanding: Boolean, nonStandardTimes: Int) = CrewValue(crewSize,didTakeoff,didLanding,nonStandardTimes)
    }

    fun toInt():Int {
        var value = if (crewSize > 15) 15 else crewSize
        value = value.setBit(4, didTakeoff).setBit(5, didLanding)
        value += takeoffLandingTimes.shl(6)
        return value
    }
}