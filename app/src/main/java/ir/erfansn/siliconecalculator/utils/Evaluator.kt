package ir.erfansn.siliconecalculator.utils

import org.mariuszgromada.math.mxparser.Expression

class Evaluator {
    private val _expression = Expression()

    fun eval(expression: String): String {
        _expression.expressionString = expression.let {
            it.replace(
                regex = """(\d*)\.(\d*)""".toRegex(),
            ) { result ->
                val (integer, fraction) = result.destructured
                when {
                    integer.isEmpty() -> "0.$fraction"
                    fraction.isEmpty() -> "$integer.0"
                    else -> "$integer.$fraction"
                }
            }
        }
        return _expression.calculate().toString()
    }
}
