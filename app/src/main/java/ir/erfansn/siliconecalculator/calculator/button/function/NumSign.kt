package ir.erfansn.siliconecalculator.calculator.button.function

import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.button.FunctionButton
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.util.Evaluator

object NumSign : FunctionButton("±") {

    override val applier: (String) -> String = { n -> "-$n" }
}
