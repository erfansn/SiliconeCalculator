package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.asHistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.asHistoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
    private val ioDispatcher: CoroutineDispatcher
) : HistoryRepository {

    override val historyItemsStream = historyDao.getHistoryEntitiesStream()
        .map {
            it.map(HistoryEntity::asHistoryItem)
        }

    override suspend fun clearAllHistory() {
        withContext(ioDispatcher) {
            historyDao.deleteAllHistoryEntities()
        }
    }

    override suspend fun saveComputation(computation: Computation) {
        withContext(ioDispatcher) {
            historyDao.insertHistoryEntity(computation.asHistoryEntity())
        }
    }
}