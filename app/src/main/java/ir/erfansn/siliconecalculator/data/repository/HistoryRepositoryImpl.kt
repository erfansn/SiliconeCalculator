package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.asHistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.asHistoryItem
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override val historyItemsStream = historyDao.getHistoryEntitiesStream()
        .map {
            it.map(HistoryEntity::asHistoryItem)
        }

    override suspend fun clearAllHistory() {
        historyDao.deleteAllHistoryEntities()
    }

    override suspend fun saveComputation(computation: Computation) {
        historyDao.insertHistoryEntity(computation.asHistoryEntity())
    }
}