package ir.erfansn.siliconecalculator.data.source.local.db.dao

import androidx.room.*
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistoryEntity(historyEntity: HistoryEntity)

    @Query("SELECT * FROM History")
    fun getHistoryEntitiesStream(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM History")
    suspend fun deleteAllHistoryEntities()
}