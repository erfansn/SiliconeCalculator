package ir.erfansn.siliconecalculator.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.utils.DateConverter

@Database(entities = [HistoryEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class SiliconeCalculatorDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
