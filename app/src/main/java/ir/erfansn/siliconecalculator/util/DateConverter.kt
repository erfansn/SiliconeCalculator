package ir.erfansn.siliconecalculator.util

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class DateConverter {

    @TypeConverter
    fun Int.toLocalDate() = LocalDate.fromEpochDays(this)

    @TypeConverter
    fun LocalDate.toEpochDay() = toEpochDays()
}
