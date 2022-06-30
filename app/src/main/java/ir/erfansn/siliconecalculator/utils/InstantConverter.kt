package ir.erfansn.siliconecalculator.utils

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {

    @TypeConverter
    fun Long.toInstant() = Instant.fromEpochMilliseconds(this)

    @TypeConverter
    fun Instant.toTimestamp() = toEpochMilliseconds()
}
