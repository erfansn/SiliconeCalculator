package ir.erfansn.siliconecalculator.calculator.button.function

import ir.erfansn.siliconecalculator.calculator.button.FunctionButton

object Percent : FunctionButton("%") {

    override val applier: (String) -> String = { n -> "$n ÷ 100" }
}
