package ir.erfansn.siliconecalculator.calculator.button.common

import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.data.model.Computation

object AllClear : CalculatorButton("AC") {

    override val applier: (String) -> String = { "" }

    override fun Computation.perform(): Computation {
        return copy(
            expression = applier(expression),
            result = "0"
        )
    }
}
