package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.util.Evaluator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    private val evaluator = Evaluator()

    private var _computation = MutableStateFlow(Computation())
    private val currentExpression: String get() = _computation.value.result

    private var operatorsStack = Stack<String>().apply { push("$") }
    private val lastOperator get() = operatorsStack.peek()
    private val String.lastNumber get() = substringAfterLast(" $lastOperator ")
    private val String.lastElementIsOperator get() = lastNumber.isEmpty() && lastOperator.isNotEmpty()

    val uiState = _computation
        .map(::CalculatorUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CalculatorUiState()
        )

    init {
        updateCalculatorDisplay(
            expression = savedStateHandle["expression"],
            result = savedStateHandle["result"]
        )
    }

    private fun updateCalculatorDisplay(expression: String?, result: String?) {
        if (expression == null || result == null) return

        _computation.update {
            it.copy(expression = expression, result = result)
        }
    }

    fun onNumPadButtonClick(calculatorAction: CalculatorAction) {
        if (calculatorAction.isResultNotValidAllowOnlyAllClearButton) return

        val result = when (calculatorAction) {
            is CalculatorAction.Digit -> {
                val expressionWithoutExtraZero = currentExpression.amendExpression(calculatorAction)

                calculatorAction.applier(expressionWithoutExtraZero)
            }
            CalculatorAction.Decimal -> {
                if (calculatorAction.symbol in currentExpression.lastNumber) return

                calculatorAction.applier(currentExpression)
            }
            CalculatorAction.AllClear -> {
                _computation.update { it.copy(expression = "") }

                calculatorAction.applier(currentExpression)
            }
            CalculatorAction.NumSign, CalculatorAction.Percent -> {
                if (currentExpression.lastElementIsOperator) return

                evaluator.expression = calculatorAction.applier(currentExpression.lastNumber)

                currentExpression.replaceLastNumberWith(evaluator.eval())
            }
            CalculatorAction.Equals -> {
                if (isExpressionIncomplete) return

                _computation.update {
                    it.copy(expression = currentExpression.amendExpression(calculatorAction))
                }
                evaluator.expression = calculatorAction.applier(_computation.value.expression)

                operatorsStack.removeAll { it != "$" }

                evaluator.eval()
            }
            CalculatorAction.Add, CalculatorAction.Sub, CalculatorAction.Mul, CalculatorAction.Div -> {
                val expressionWithoutExtraOperator =
                    currentExpression.amendExpression(calculatorAction)
                operatorsStack.push(calculatorAction.symbol)

                calculatorAction.applier(expressionWithoutExtraOperator)
            }
        }
        _computation.update { it.copy(result = result) }

        if (calculatorAction.evaluationWasSuccess) saveComputationInHistory()
    }

    private val CalculatorAction.isResultNotValidAllowOnlyAllClearButton
        get() = !resultIsValid && this != CalculatorAction.AllClear

    private val resultIsValid get() =
        _computation.value.result != "NaN" && _computation.value.result != "Infinity"

    private fun String.amendExpression(calculatorAction: CalculatorAction): String = when {
        lastNumber.isZero && calculatorAction is CalculatorAction.Digit -> removeLastNumber()
        lastElementIsOperator && calculatorAction in listOf(
            CalculatorAction.Add,
            CalculatorAction.Sub,
            CalculatorAction.Mul,
            CalculatorAction.Div
        ) -> removeLastOperatorAndGet()
        else -> this
    }

    private val String.isZero: Boolean
        get() = this == "0"

    private fun String.removeLastNumber() =
        substringBeforeLast(lastNumber)

    private fun String.removeLastOperatorAndGet() =
        substringBeforeLast(" $lastOperator ").also { operatorsStack.pop() }

    private val isExpressionIncomplete: Boolean
        get() = currentExpression.isOnlyNumber ||
                currentExpression.lastElementIsOperator && operatorsStack.size == 2

    private val String.isOnlyNumber get() = this == lastNumber

    private fun String.replaceLastNumberWith(replacement: String): String {
        return replaceAfterLast(
            delimiter = " $lastOperator ",
            replacement = replacement,
            missingDelimiterValue = replacement
        )
    }

    private val CalculatorAction.evaluationWasSuccess
        get() = this == CalculatorAction.Equals && resultIsValid

    private fun saveComputationInHistory() {
        viewModelScope.launch {
            historyRepository.saveComputation(computation = _computation.value)
        }
    }
}
