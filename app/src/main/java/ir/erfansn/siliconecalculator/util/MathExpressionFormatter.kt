package ir.erfansn.siliconecalculator.util

private val DECIMAL_NUMBER_REGEX = """(0|-\d+|\d*)(\.)?(\d*)(E)?(-?\d+)?""".toRegex()

fun String.formatNumbers(): String {
    fun String.separateByComma() = reversed().chunked(3).joinToString(",").reversed()

    return replace(DECIMAL_NUMBER_REGEX) {
        val (integer, point, fraction, e, exponent) = it.destructured
        "${integer.separateByComma()}$point$fraction$e${exponent.separateByComma()}"
    }
}