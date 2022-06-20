package ir.erfansn.siliconecalculator.data.model

import java.util.*

data class HistoryRecord(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    override val expression: String,
    override val result: String,
) : Computation()
