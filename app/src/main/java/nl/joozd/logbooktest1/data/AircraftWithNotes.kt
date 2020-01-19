package nl.joozd.logbooktest1.data

data class AircraftWithNotes(
    val id: Int,
    val registration: String,
    val manufacturer: String,
    val model: String,
    val engine_type: String,
    val mtow: Int,
    val se: Int,
    val me: Int,
    val multipilot: Int,
    val isIfr: Int,
    val isKnown: Boolean){
    constructor(a: Aircraft, isKnown: Boolean): this(a.id, a.registration, a.manufacturer,a.model,a.engine_type,a.mtow,a.se,a.me,a.multipilot,a.isIfr, isKnown)
    fun toAircraft() = Aircraft (id, registration, manufacturer, model, engine_type, mtow, se, me, multipilot, isIfr)
}