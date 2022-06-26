package ir.erfansn.siliconecalculator.history

import androidx.compose.runtime.Immutable
import ir.erfansn.siliconecalculator.data.model.HistoryItem

@Immutable
data class HistoryUiState(
    val historyItems: List<HistoryItem> = emptyList()
)