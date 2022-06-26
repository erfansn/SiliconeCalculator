package ir.erfansn.siliconecalculator.utils

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun Long.toDate() = Date(this)

    @TypeConverter
    fun Date.toTimestamp() = time
}
