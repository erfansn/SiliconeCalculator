package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.model.History
import ir.erfansn.siliconecalculator.data.model.asHistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.data.source.local.db.model.asHistory
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeHistoryRepository @Inject constructor() : HistoryRepository {

    private val historyEntities = mutableListOf<HistoryEntity>()

    override val historyItemsStream = MutableStateFlow(listOf<History>())

    override suspend fun clearAllHistory() {
        historyEntities.clear()
        historyItemsStream.value = emptyList()
    }

    override suspend fun saveCalculation(calculation: Calculation) {
        historyEntities += calculation.asHistoryEntity()
        historyItemsStream.value = historyEntities.map(HistoryEntity::asHistory)
    }
}