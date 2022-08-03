package ir.erfansn.siliconecalculator.calculator.button.function

import ir.erfansn.siliconecalculator.calculator.button.FunctionButton
import ir.erfansn.siliconecalculator.data.model.Calculation

object Equals : FunctionButton("=") {

    override val applier: (String) -> String = { it }

    override fun Calculation.perform(): Calculation {
        if (!isComplete) return this

        val amendedExpression = if (result == "0")
            expression.substringBeforeLast(lastOperator)
        else
            expression.plus(result)

        evaluator.expression = applier(amendedExpression)

        return copy(expression = evaluator.expression, result = evaluator.eval())
    }
}
