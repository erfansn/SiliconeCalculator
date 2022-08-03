package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.EXPRESSION_ARG
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.RESULT_ARG
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private var _computation = MutableStateFlow(Computation())
    private val currentComputation get() = _computation.value

    private var previousExpression = currentComputation.expression

    val uiState = _computation
        .map(::CalculatorUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CalculatorUiState()
        )

    init {
        updateCalculatorDisplay(
            expression = savedStateHandle[EXPRESSION_ARG],
            result = savedStateHandle[RESULT_ARG]
        )
    }

    private fun updateCalculatorDisplay(expression: String?, result: String?) {
        if (expression == null || result == null) return

        _computation.update {
            it.copy(expression = expression, result = result)
        }

        previousExpression = expression
    }

    fun performCalculatorButton(calculatorButton: CalculatorButton) {
        if (currentComputation.resultIsInvalid && calculatorButton != AllClear) return

        _computation.update {
            with(calculatorButton) { it.perform() }
        }
    }

    fun saveComputationInHistory() = with(currentComputation) {
        if (isNotEvaluated || resultIsInvalid) return

        viewModelScope.launch {
            historyRepository.saveComputation(computation = this@with)
        }

        previousExpression = expression
    }

    private val Computation.isNotEvaluated
        get() = expression.endsWith(lastOperator) || expression.isEmpty() || expression == previousExpression

    private val Computation.resultIsInvalid: Boolean
        get() = result.matches("-?Infinity|NaN".toRegex())

    private val Computation.lastOperator
        get() = operators.lastOrNull()?.value ?: "$"

    private val Computation.operators
        get() = "\\s$OPERATORS_REGEX\\s".toRegex().findAll(expression)
}
