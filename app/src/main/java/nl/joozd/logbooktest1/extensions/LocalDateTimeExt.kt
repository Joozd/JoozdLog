package nl.joozd.logbooktest1.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val monthYearFormatter = DateTimeFormatter.ofPattern(("MMM yyyy"))
private val timeFormatterNoColon = DateTimeFormatter.ofPattern("HH:mm")
private val dateAndTimeFormatter =  DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")

fun LocalDate.toDateString() = this.format(dateFormatter)
fun LocalDateTime.toDateString() = this.format(dateFormatter)
fun LocalDateTime.toTimeString() = this.format(timeFormatter)
fun LocalDateTime.toMonthYear() = this.format(monthYearFormatter)
fun LocalDateTime.noColon() = this.format(timeFormatterNoColon)

fun String.makeLocalDate(): LocalDate = LocalDate.parse(this,
    dateFormatter
)
fun String.makeLocalDateTime(): LocalDateTime = LocalDateTime.parse(this,
    dateAndTimeFormatter
)
fun String.makeLocalDateTimeTime(): LocalDateTime = LocalDateTime.parse(this,
    timeFormatter
)
fun String.addColonToTime() = LocalDateTime.parse(this,
    timeFormatterNoColon
).toTimeString()
fun String.makeLocalTime() = LocalTime.parse(this, timeFormatter)

fun LocalDateTime.roundToMinutes() =
    if (this.second < 30) this.withSecond(0)
    else this.withSecond(0).plusMinutes(1)
