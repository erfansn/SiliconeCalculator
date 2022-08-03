package ir.erfansn.siliconecalculator.util

const val OPERATORS_REGEX = """[+\-×÷]"""
const val DECIMAL_REGEX = """-?\d+\.?(?:\d+(?:E-?\d+)?)?"""

private const val SPECIFIC_NUMBER_REGEX = """(-)?(0|\d*)(\.)?(\d*)(E-?)?(\d+)?"""

fun String.formatNumbers(): String {
    fun String.separateByComma() = reversed().chunked(3).joinToString(",").reversed()

    return replace(SPECIFIC_NUMBER_REGEX.toRegex()) {
        val (integerSign, integer, point, fraction, exponentSign, exponent) = it.destructured

        "$integerSign${integer.separateByComma()}$point$fraction$exponentSign${exponent.separateByComma()}"
    }
}