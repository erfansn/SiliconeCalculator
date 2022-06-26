package ir.erfansn.siliconecalculator.data.source.local

import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity

class HistoryLocalDataSource(private val historyDao: HistoryDao) {

    val historyEntitiesStream = historyDao.getHistoryEntitiesStream()

    suspend fun clearHistoryEntities() {
        historyDao.clearHistoryEntities()
    }

    suspend fun insertHistoryEntity(historyEntity: HistoryEntity) {
        historyDao.insertHistoryEntity(historyEntity)
    }
}
