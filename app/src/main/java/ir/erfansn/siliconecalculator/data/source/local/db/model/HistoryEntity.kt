package ir.erfansn.siliconecalculator.data.source.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import ir.erfansn.siliconecalculator.util.formatToString
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(tableName = "History")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val instant: Instant = Clock.System.now(),
    val expression: String,
    val result: String,
)

fun HistoryEntity.asHistoryItem() = HistoryItem(
    id = id,
    date = instant.formatToString(),
    computation = Computation(
        expression = expression,
        result = result
    )
)
