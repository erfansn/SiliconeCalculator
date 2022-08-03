package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    val historyItemsStream: Flow<List<History>>
    suspend fun clearAllHistory()
    suspend fun saveCalculation(calculation: Calculation)
}