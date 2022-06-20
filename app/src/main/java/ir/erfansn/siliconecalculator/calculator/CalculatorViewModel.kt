package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.erfansn.siliconecalculator.utils.Evaluator
import kotlinx.coroutines.flow.*
import java.util.*

class CalculatorViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val evaluator = Evaluator()

    private var _expression = MutableStateFlow("")
    private var _result = MutableStateFlow("0")
    private val currentExpression: String get() = _result.value

    private var operatorsStack = Stack<String>().apply { push("$") }
    private val lastOperator get() = operatorsStack.peek()
    private val lastNumber get() = currentExpression.substringAfterLast(" $lastOperator ")
    private val lastElementIsOperator get() = lastNumber.isEmpty() && lastOperator.isNotEmpty()

    val calculatorUiState = combine(
        _expression,
        _result,
        ::CalculatorUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = CalculatorUiState()
    )

    init {
        updateCalculatorDisplay(
            expression = savedStateHandle.get<String>("expression"),
            result = savedStateHandle.get<String>("result")
        )
    }

    private fun updateCalculatorDisplay(expression: String?, result: String?) {
        if (expression == null || result == null) return

        _expression.update { expression }
        _result.update { result }
    }

    fun onNumberPadClick(calculatorButton: CalculatorButton) {
        if (calculatorButton.isResultNotValidAllowOnlyAllClearButton) return

        val result = when (calculatorButton) {
            is CalculatorButton.Digit -> {
                val expressionWithoutExtraZero = currentExpression.let {
                    if (lastNumber.isZero) it.removeLastNumber() else it
                }

                calculatorButton.applier(expressionWithoutExtraZero)
            }
            CalculatorButton.Decimal -> {
                if (calculatorButton.symbol in lastNumber) return

                calculatorButton.applier(currentExpression)
            }
            CalculatorButton.AllClear -> {
                _expression.update { "" }

                calculatorButton.applier(currentExpression)
            }
            CalculatorButton.NumSign, CalculatorButton.Percent -> {
                if (lastElementIsOperator) return

                val appliedExpression = calculatorButton.applier(lastNumber)
                val evaluatedResult = evaluator.eval(appliedExpression)

                replaceWithLastNumber(evaluatedResult)
            }
            CalculatorButton.Equals -> {
                if (currentExpression == lastNumber) return

                _expression.update { currentExpression }
                val appliedExpression = calculatorButton.applier(_expression.value)

                evaluator.eval(appliedExpression)
            }
            else -> {
                val expressionWithoutExtraOperator = currentExpression.let {
                    if (lastElementIsOperator) it.removeLastOperatorAndGet() else it
                }
                operatorsStack.push(calculatorButton.symbol)

                calculatorButton.applier(expressionWithoutExtraOperator)
            }
        }
        _result.update { result }
    }

    private val CalculatorButton.isResultNotValidAllowOnlyAllClearButton
        get() = _result.value == "NaN" && this !is CalculatorButton.AllClear

    private val String.isZero: Boolean
        get() = this == "0"

    private fun String.removeLastNumber() =
        substringBeforeLast(lastNumber)

    private fun replaceWithLastNumber(replacement: String): String {
        return currentExpression.replaceAfterLast(
            delimiter = " $lastOperator ",
            replacement = replacement,
            missingDelimiterValue = replacement
        )
    }

    private fun String.removeLastOperatorAndGet() =
        substringBeforeLast(" $lastOperator ").also { operatorsStack.pop() }
}
