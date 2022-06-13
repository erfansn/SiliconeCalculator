package ir.erfansn.siliconecalculator.history

import ir.erfansn.siliconecalculator.data.source.local.db.HistoryEntity

data class HistoryUiState(
    val recordsList: List<HistoryEntity>
)