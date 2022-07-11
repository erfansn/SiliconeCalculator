package ir.erfansn.siliconecalculator.utils

val String.encodeReservedChars
    get() = replace(
        """[!*'();:@&=+$,/?%#\[\]]""".toRegex()
    ) { "%${it.value.single().code.toString(16)}" }
