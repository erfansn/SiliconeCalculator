package ir.erfansn.siliconecalculator.calculator

val calculatorActionsList = listOf(
    CalculatorAction.AllClear, CalculatorAction.NumSign, CalculatorAction.Percent, CalculatorAction.Div,
    CalculatorAction.Digit(7), CalculatorAction.Digit(8), CalculatorAction.Digit(9), CalculatorAction.Mul,
    CalculatorAction.Digit(4), CalculatorAction.Digit(5), CalculatorAction.Digit(6), CalculatorAction.Sub,
    CalculatorAction.Digit(1), CalculatorAction.Digit(2), CalculatorAction.Digit(3), CalculatorAction.Add,
    CalculatorAction.Digit(0), CalculatorAction.Decimal, CalculatorAction.Equals,
)

sealed class CalculatorAction(
    val symbol: String,
    val applier: (String) -> String = { n -> "$n $symbol " },
) {
    data class Digit(val d: Int) : CalculatorAction("$d", { n -> "$n$d" })
    object Add : CalculatorAction("+")
    object Sub : CalculatorAction("-")
    object Div : CalculatorAction("÷")
    object Mul : CalculatorAction("×")
    object Decimal : CalculatorAction(".", { n -> "$n." })
    object NumSign : CalculatorAction("±", { n -> "-$n" })
    object Percent : CalculatorAction("%", { n -> "$n ÷ 100" })
    object AllClear : CalculatorAction("AC", { "0" })
    object Equals : CalculatorAction("=", { it })
}
