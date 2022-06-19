package ir.erfansn.siliconecalculator.data.source.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class HistoryRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo val date: String = "",
    @ColumnInfo val expression: String = "",
    @ColumnInfo val result: String = "0",
)
