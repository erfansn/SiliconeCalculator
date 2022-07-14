package ir.erfansn.siliconecalculator.util

val String.encodeReservedChars
    get() = replace(
        """[!*'();:@&=+$,/?%#\[\]]""".toRegex()
    ) { "%${it.value.single().code.toString(16)}" }
