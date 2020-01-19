package nl.joozd.logbooktest1.data

data class BalanceForward (val logbookName: String, val aircraftTime: Int, val simTime: Int, val takeOffDay: Int, val takeOffNight: Int, val landingDay: Int, val landingNight: Int,
                           val nightTime: Int, val ifrTime: Int, val picTime: Int, val copilotTime: Int, val dualTime: Int, val instructortime: Int, val id: Int = -1){
    val grandTotal = aircraftTime + simTime
}