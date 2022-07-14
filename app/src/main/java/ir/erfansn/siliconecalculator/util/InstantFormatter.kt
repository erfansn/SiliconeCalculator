package ir.erfansn.siliconecalculator.util

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter

fun Instant.formatToString(): String {
    val targetInstant = this
    val currentInstant = Clock.System.now()

    return when (targetInstant.daysUntil(currentInstant, TimeZone.currentSystemDefault())) {
        0 -> "Today"
        1 -> "Yesterday"
        else -> {
            val formatter = DateTimeFormatter.ofPattern("MMM d")
            val localDateTime = targetInstant.toLocalDateTime(TimeZone.currentSystemDefault())
            formatter.format(localDateTime.toJavaLocalDateTime())
        }
    }
}