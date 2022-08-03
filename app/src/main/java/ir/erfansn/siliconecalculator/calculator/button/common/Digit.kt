package ir.erfansn.siliconecalculator.calculator.button.common

import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.data.model.Calculation

data class Digit(val digit: Char) : CalculatorButton("$digit") {

    override val applier: (String) -> String = { n -> "$n$digit" }

    override fun Calculation.perform(): Calculation {
        val amendedResult = result.takeUnless { it == "0" }.orEmpty()

        return copy(result = applier(amendedResult))
    }
}
