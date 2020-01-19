package nl.joozd.logbooktest1.data.miscClasses

/**
 * Balances forward for filling pages in logbook. All values in minutes.
 * ie. 1 hr 17 = 77 mins
 * */
data class TotalsForward (var multiPilot: Int = 0, var totalTime: Int = 0, var landingDay: Int = 0,
                          var landingNight: Int = 0, var nightTime: Int = 0, var ifrTime: Int = 0,
                          var picTime: Int = 0, var copilotTime: Int = 0, var dualTime: Int = 0,
                          var instructorTime: Int = 0, var simTime: Int = 0)