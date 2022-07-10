package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.utils.Evaluator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class CalculatorViewModel(
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

    fun onNumPadButtonClick(calculatorButton: CalculatorButton) {
        if (calculatorButton.isResultNotValidAllowOnlyAllClearButton) return

        val result = when (calculatorButton) {
            is CalculatorButton.Digit -> {
                val expressionWithoutExtraZero = currentExpression.amendExpression(calculatorButton)

                calculatorButton.applier(expressionWithoutExtraZero)
            }
            CalculatorButton.Decimal -> {
                if (calculatorButton.symbol in currentExpression.lastNumber) return

                calculatorButton.applier(currentExpression)
            }
            CalculatorButton.AllClear -> {
                _computation.update { it.copy(expression = "") }

                calculatorButton.applier(currentExpression)
            }
            CalculatorButton.NumSign, CalculatorButton.Percent -> {
                if (currentExpression.lastElementIsOperator) return

                evaluator.expression = calculatorButton.applier(currentExpression.lastNumber)

                currentExpression.replaceLastNumberWith(evaluator.eval())
            }
            CalculatorButton.Equals -> {
                if (isExpressionIncomplete) return

                _computation.update {
                    it.copy(expression = currentExpression.amendExpression(calculatorButton))
                }
                evaluator.expression = calculatorButton.applier(_computation.value.expression)

                operatorsStack.removeAll { it != "$" }

                evaluator.eval()
            }
            CalculatorButton.Add, CalculatorButton.Sub, CalculatorButton.Mul, CalculatorButton.Div -> {
                val expressionWithoutExtraOperator =
                    currentExpression.amendExpression(calculatorButton)
                operatorsStack.push(calculatorButton.symbol)

                calculatorButton.applier(expressionWithoutExtraOperator)
            }
        }
        _computation.update { it.copy(result = result) }

        if (calculatorButton.evaluationWasSuccess) saveComputationInHistory()
    }

    private val CalculatorButton.isResultNotValidAllowOnlyAllClearButton
        get() = !resultIsValid && this != CalculatorButton.AllClear

    private val resultIsValid get() = _computation.value.result != "NaN"

    private fun String.amendExpression(calculatorButton: CalculatorButton): String = when {
        lastNumber.isZero && calculatorButton is CalculatorButton.Digit -> removeLastNumber()
        lastElementIsOperator && calculatorButton in listOf(
            CalculatorButton.Add,
            CalculatorButton.Sub,
            CalculatorButton.Mul,
            CalculatorButton.Div
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

    private val CalculatorButton.evaluationWasSuccess
        get() = this == CalculatorButton.Equals && resultIsValid

    private fun saveComputationInHistory() {
        viewModelScope.launch {
            historyRepository.saveComputation(computation = _computation.value)
        }
    }
}
