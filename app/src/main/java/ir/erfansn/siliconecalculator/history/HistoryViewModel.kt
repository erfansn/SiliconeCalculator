package ir.erfansn.siliconecalculator.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    val uiState = historyRepository.historyItemsStream
        .map(::HistoryUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = HistoryUiState()
        )

    fun onHistoryClear() {
        viewModelScope.launch {
            historyRepository.clearAllHistory()
        }
    }
}