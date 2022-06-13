package ir.erfansn.siliconecalculator.calculator

val buttonList = listOf(
    CalculatorButton.AllClear,  CalculatorButton.NumSign,     CalculatorButton.Percent,     CalculatorButton.Div,
    CalculatorButton.Digit(7), CalculatorButton.Digit(8), CalculatorButton.Digit(9), CalculatorButton.Mul,
    CalculatorButton.Digit(4), CalculatorButton.Digit(5), CalculatorButton.Digit(6), CalculatorButton.Sub,
    CalculatorButton.Digit(1), CalculatorButton.Digit(2), CalculatorButton.Digit(3), CalculatorButton.Add,
    CalculatorButton.Digit(0), CalculatorButton.Decimal,     CalculatorButton.Equals,
)

sealed class CalculatorButton(
    val symbol: String,
    val applier: (String) -> String = { n -> "$n $symbol " },
) {
    data class Digit(private val d: Int) : CalculatorButton("$d", { n -> "$n$d" })
    object Add : CalculatorButton("+")
    object Sub : CalculatorButton("-")
    object Div : CalculatorButton("÷")
    object Mul : CalculatorButton("×")
    object Decimal : CalculatorButton(".", { n -> "$n." })
    object NumSign : CalculatorButton("±", { n -> "-$n" })
    object Percent : CalculatorButton("%", { n -> "$n ÷ 100" })
    object AllClear : CalculatorButton("AC", { "0" })
    object Equals : CalculatorButton("=", { it })
}
