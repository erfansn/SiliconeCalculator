package ir.erfansn.siliconecalculator

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ir.erfansn.siliconecalculator.calculator.CalculatorViewModel
import ir.erfansn.siliconecalculator.history.HistoryViewModel

class ViewModelFactory(
    context: Context,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    private val application = context.applicationContext as SiliconeCalculator
    private val historyRepository = application.appContainer.historyRepository

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ) = with(modelClass) {
        when {
            isAssignableFrom(CalculatorViewModel::class.java) -> CalculatorViewModel(handle, historyRepository)
            isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(historyRepository)
            else -> IllegalArgumentException("Unknown viewmodel class: ${modelClass.name}")
        }
    } as T
}
