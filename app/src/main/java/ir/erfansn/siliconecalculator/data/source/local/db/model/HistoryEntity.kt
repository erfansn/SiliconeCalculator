package ir.erfansn.siliconecalculator.data.source.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import ir.erfansn.siliconecalculator.util.format
import kotlinx.datetime.*

@Entity(tableName = "History")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "epoch_day") val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val expression: String,
    val result: String,
)

fun HistoryEntity.asHistoryItem() = HistoryItem(
    id = id,
    date = date.format(),
    computation = Computation(
        expression = expression,
        result = result
    )
)
