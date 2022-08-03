package ir.erfansn.siliconecalculator.calculator.button.common

import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.data.model.Computation

object Decimal : CalculatorButton(".") {

    override val applier: (String) -> String = { n -> "$n$symbol" }

    override fun Computation.perform(): Computation {
        if (symbol in result) return this

        return copy(result = applier(result))
    }
}
