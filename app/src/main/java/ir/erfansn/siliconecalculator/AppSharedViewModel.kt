package ir.erfansn.siliconecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.erfansn.siliconecalculator.calculator.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.CalculatorUiState
import kotlinx.coroutines.flow.*

class AppSharedViewModel : ViewModel() {

    private var _expression = MutableStateFlow("")
    private var _result = MutableStateFlow("0")

    val calculatorUiState = combine(
        _expression,
        _result,
        ::CalculatorUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CalculatorUiState()
    )

    fun onNumberPadClick(calculatorButton: CalculatorButton) {
        if (calculatorButton is CalculatorButton.Equals) {
            _expression.value = _result.value
        } else if (calculatorButton is CalculatorButton.AllClear) {
            _expression.value = ""
            _result.value = "0"
        } else {
            _result.value = calculatorButton.applier(_result.value)
        }
    }

    fun updateCalculatorDisplay(expression: String, result: String) {
        _expression.update { expression }
        _result.update { result }
    }
}
