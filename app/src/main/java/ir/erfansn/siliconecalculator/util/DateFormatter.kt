package ir.erfansn.siliconecalculator.util

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalDate.format(pattern: String = "MMM d"): String =
    when (daysUntil(Clock.System.todayIn(TimeZone.currentSystemDefault()))) {
        0 -> "Today"
        1 -> "Yesterday"
        else -> {
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
            formatter.format(toJavaLocalDate())
        }
    }