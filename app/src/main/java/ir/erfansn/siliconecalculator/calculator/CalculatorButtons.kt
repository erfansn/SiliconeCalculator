package ir.erfansn.siliconecalculator.calculator

val buttonList = listOf(
    CalculatorButton.ClearEntry,  CalculatorButton.NumSign,     CalculatorButton.Percent,     CalculatorButton.Div,
    CalculatorButton.Digit(7), CalculatorButton.Digit(8), CalculatorButton.Digit(9), CalculatorButton.Mul,
    CalculatorButton.Digit(4), CalculatorButton.Digit(5), CalculatorButton.Digit(6), CalculatorButton.Sub,
    CalculatorButton.Digit(1), CalculatorButton.Digit(2), CalculatorButton.Digit(3), CalculatorButton.Add,
    CalculatorButton.Digit(0), CalculatorButton.Decimal,     CalculatorButton.Equals,
)

sealed class CalculatorButton(
    val sign: String,
    val operation: (String) -> String = { n -> "$n$sign" },
) {
    class Digit(d: Int) : CalculatorButton("$d")
    object Add : CalculatorButton("+")
    object Sub : CalculatorButton("-")
    object Div : CalculatorButton("÷")
    object Mul : CalculatorButton("×")
    object Decimal : CalculatorButton(".")
    object NumSign : CalculatorButton("±", { n -> "-$n" })
    object Percent : CalculatorButton("%", { n -> "$n ÷ 100" })
    object ClearEntry : CalculatorButton("CE", { "0" })
    object Equals : CalculatorButton("=", { it })
}
