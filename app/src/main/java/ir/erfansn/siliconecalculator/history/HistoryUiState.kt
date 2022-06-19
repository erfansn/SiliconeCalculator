package ir.erfansn.siliconecalculator.history

import ir.erfansn.siliconecalculator.data.source.local.db.HistoryRecord

data class HistoryUiState(
    val recordsList: List<HistoryRecord>
)