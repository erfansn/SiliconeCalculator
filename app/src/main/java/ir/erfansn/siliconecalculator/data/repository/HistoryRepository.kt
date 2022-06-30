package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.asHistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.HistoryLocalDataSource
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.asHistoryItem
import kotlinx.coroutines.flow.map

class HistoryRepository(private val historyLocalDataSource: HistoryLocalDataSource) {

    val historyItemsStream = historyLocalDataSource
        .historyEntitiesStream
        .map {
            it.map(HistoryEntity::asHistoryItem)
        }

    suspend fun clearAllHistory() {
        historyLocalDataSource.clearHistoryEntities()
    }

    suspend fun saveComputation(computation: Computation) {
        historyLocalDataSource.insertHistoryEntity(computation.asHistoryEntity())
    }
}