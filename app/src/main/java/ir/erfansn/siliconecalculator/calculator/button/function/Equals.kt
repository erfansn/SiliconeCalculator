package ir.erfansn.siliconecalculator.calculator.button.function

import ir.erfansn.siliconecalculator.calculator.button.FunctionButton
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.util.OPERATORS_REGEX

object Equals : FunctionButton("=") {

    override val applier: (String) -> String = { it }

    override fun Computation.perform(): Computation {
        if (!isComplete) return this

        val amendedExpression = if (result == "0")
            expression.substringBeforeLast(lastOperator)
        else
            expression.plus(result)

        evaluator.expression = applier(amendedExpression)

        return copy(expression = evaluator.expression, result = evaluator.eval())
    }

    private val Computation.isComplete: Boolean
        get() = expression.isNotEmpty() && ((result != "0" || operators.count() > 1) && expression.endsWith(lastOperator))

    private val Computation.lastOperator
        get() = operators.lastOrNull()?.value ?: "$"

    private val Computation.operators
        get() = "\\s$OPERATORS_REGEX\\s".toRegex().findAll(expression)
}
