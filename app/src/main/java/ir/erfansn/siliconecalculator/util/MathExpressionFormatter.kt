package ir.erfansn.siliconecalculator.util

private val DECIMAL_NUMBER_REGEX = """(-)?(0|\d*)(\.)?(\d*)(E)?(-)?(\d+)?""".toRegex()

fun String.formatNumbers(): String {
    fun String.separateByComma() = reversed().chunked(3).joinToString(",").reversed()

    return replace(DECIMAL_NUMBER_REGEX) {
        val (integerSign, integer, point, fraction, e, exponentSign, exponent) = it.destructured
        "$integerSign${integer.separateByComma()}$point$fraction$e$exponentSign${exponent.separateByComma()}"
    }
}