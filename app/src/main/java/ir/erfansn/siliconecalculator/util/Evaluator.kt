package ir.erfansn.siliconecalculator.util

import org.mariuszgromada.math.mxparser.Expression

class Evaluator {

    private val _expression = Expression()

    var expression: String = ""
       set(value) {
           field = value.amendExpression().also(_expression::setExpressionString)
       }

    fun eval() = _expression.calculate().toString()

    private fun String.amendExpression(): String {
        return replace(
            regex = """(\d+)\.(\d*)""".toRegex(),
        ) { result ->
            val (integer, fraction) = result.destructured
            "$integer.${fraction.ifEmpty { "0" }}"
        }
    }
}
