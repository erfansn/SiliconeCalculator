package ir.erfansn.siliconecalculator.data.repository

import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    val historyItemsStream: Flow<List<HistoryItem>>
    suspend fun clearAllHistory()
    suspend fun saveComputation(computation: Computation)
}