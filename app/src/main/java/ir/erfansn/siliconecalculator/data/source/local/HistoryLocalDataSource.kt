package ir.erfansn.siliconecalculator.data.source.local

import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HistoryLocalDataSource(
    private val historyDao: HistoryDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    val historyEntitiesStream = historyDao.getHistoryEntitiesStream()

    suspend fun clearHistoryEntities() {
        withContext(ioDispatcher) {
            historyDao.deleteAllHistoryEntities()
        }
    }

    suspend fun insertHistoryEntity(historyEntity: HistoryEntity) {
        withContext(ioDispatcher) {
            historyDao.insertHistoryEntity(historyEntity)
        }
    }
}
