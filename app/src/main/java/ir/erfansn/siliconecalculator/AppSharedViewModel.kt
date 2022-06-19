package ir.erfansn.siliconecalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.erfansn.siliconecalculator.utils.Evaluator
import ir.erfansn.siliconecalculator.calculator.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.CalculatorUiState
import kotlinx.coroutines.flow.*
import java.util.*

class AppSharedViewModel : ViewModel() {

    private val evaluator = Evaluator()

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

    private var operatorsStack = Stack<String>().apply { push("$") }

    private val lastOperator get() = operatorsStack.peek()
    private val lastNumber get() = _result.value.substringAfterLast(" $lastOperator ")
    private val lastElementIsOperator get() = lastNumber.isEmpty() && lastOperator.isNotEmpty()
    private val lastElementIsDecimal get() = _result.value.last() == '.'

    fun onNumberPadClick(calculatorButton: CalculatorButton) {
        if (_result.value == "NaN" && calculatorButton !is CalculatorButton.AllClear) return

        val result = when (calculatorButton) {
            is CalculatorButton.Digit -> {
                val perfectExpression = _result.value.let { if (lastNumber.isZero) it.removeLastNumber() else it }

                calculatorButton.applier(perfectExpression)
            }
            CalculatorButton.Decimal -> {
                if (calculatorButton.symbol in lastNumber) return

                calculatorButton.applier(_result.value)
            }
            CalculatorButton.AllClear -> {
                _expression.update { "" }

                calculatorButton.applier(_result.value)
            }
            CalculatorButton.NumSign, CalculatorButton.Percent -> {
                if (lastElementIsOperator) return

                val expression = calculatorButton.applier(lastNumber)
                val evaluatedExpression = evaluator.eval(expression)

                replaceWithLastNumber(evaluatedExpression)
            }
            CalculatorButton.Equals -> {
                if (_result.value == lastNumber) return

                _expression.update { _result.value }
                val expression = calculatorButton.applier(_expression.value)

                evaluator.eval(expression)
            }
            else -> {
                val perfectExpression = _result.value.let {
                    if (lastElementIsOperator) it.removeLastOperatorAndGet() else it
                }
                calculatorButton.symbol.addNewOperator()

                calculatorButton.applier(perfectExpression)
            }
        }
        _result.update { result }
    }

    fun updateCalculatorDisplay(expression: String, result: String) {
        _expression.update { expression }
        _result.update { result }
    }

    private val String.isZero: Boolean
        get() = this == "0"

    private fun replaceWithLastNumber(replacement: String): String {
        return _result.value.replaceAfterLast(
            delimiter = " $lastOperator ",
            replacement = replacement,
            missingDelimiterValue = replacement
        )
    }

    private fun String.addNewOperator() {
        operatorsStack.push(this)
    }

    private fun removeLastOperator() {
        operatorsStack.pop()
    }

    private fun String.removeLastOperatorAndGet() =
        substringBeforeLast(" $lastOperator ").also { removeLastOperator() }

    private fun String.removeLastNumber() =
        substringBeforeLast(lastNumber)

    private fun String.completeExpression() =
        if (last() == '.') "${this}0" else this
}
