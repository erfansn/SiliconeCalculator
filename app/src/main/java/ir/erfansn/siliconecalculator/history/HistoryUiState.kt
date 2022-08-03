package ir.erfansn.siliconecalculator.history

import androidx.compose.runtime.Immutable
import ir.erfansn.siliconecalculator.data.model.History

@Immutable
data class HistoryUiState(
    val historyItems: List<History> = emptyList()
)