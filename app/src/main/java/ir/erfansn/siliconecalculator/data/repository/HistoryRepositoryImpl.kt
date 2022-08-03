package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.model.asHistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.dao.HistoryDao
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.asHistory
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : HistoryRepository {

    override val historyItemsStream = historyDao.getHistoryEntitiesStream()
        .map {
            it.map(HistoryEntity::asHistory)
        }

    override suspend fun clearAllHistory() {
        historyDao.deleteAllHistoryEntities()
    }

    override suspend fun saveCalculation(calculation: Calculation) {
        historyDao.insertHistoryEntity(calculation.asHistoryEntity())
    }
}