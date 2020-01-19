package nl.joozd.logbooktest1.extensions

import nl.joozd.logbooktest1.data.Flight

fun Flight.asSimIfNeeded(): Flight{
    return if (this.isSim == 0) this
    else this.copy(orig="SIMULATOR", dest="SIMULATOR", timeIn = timeOut, registration = "", nightTime = 0, ifrTime = 0, isPIC = 0, isPICUS = 0, isCoPilot = 0, isDual = 0, isInstructor = 0)
}