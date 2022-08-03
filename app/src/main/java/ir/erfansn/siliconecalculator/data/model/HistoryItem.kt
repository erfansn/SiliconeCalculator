package ir.erfansn.siliconecalculator.data.model

import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.util.OPERATORS_REGEX

data class HistoryItem(
    val id: Int,
    val date: String,
    val calculation: Calculation,
)

data class Calculation(
    val expression: String = "",
    val result: String = "0",
) {
    val isComplete: Boolean
        get() = expression.isNotEmpty() && ((result != "0" || operators.count() > 1) && expression.endsWith(lastOperator))

    val resultIsInvalid: Boolean
        get() = result.matches("-?Infinity|NaN".toRegex())

    val lastOperator
        get() = operators.lastOrNull()?.value ?: "$"

    private val operators
        get() = "\\s$OPERATORS_REGEX\\s".toRegex().findAll(expression)
}

fun Calculation.asHistoryEntity() = HistoryEntity(
    expression = expression,
    result = result
)

val previewHistoryItems = listOf(
    HistoryItem(
        id = 0,
        date = "12 April",
        calculation = Calculation(
            expression = "1 + 788 * 875",
            result = "10"
        )
    ),
    HistoryItem(
        id = 1,
        date = "12 April",
        calculation = Calculation(
            expression = "68774 + 9888 * 4763 / 9847",
            result = "2675.09"
        )
    ),
    HistoryItem(
        id = 2,
        date = "15 March",
        calculation = Calculation(
            expression = "458867 / 76",
            result = "0.002"
        )
    ),
    HistoryItem(
        id = 3,
        date = "15 April",
        calculation = Calculation(
            expression = "9475 * 0.88888",
            result = "4755.2"
        )
    ),
    HistoryItem(
        id = 4,
        date = "19 April",
        calculation = Calculation(
            expression = "47362 / 1 / 98585",
            result = "12345"
        )
    ),
    HistoryItem(
        id = 5,
        date = "19 April",
        calculation = Calculation(
            expression = "5452 - 97584 + 9573 / 848 * 764",
            result = "14795"
        )
    ),
    HistoryItem(
        id = 6,
        date = "19 April",
        calculation = Calculation(
            expression = "12 - 957 + 857 - 9588 / 4388 * 8746",
            result = "25874333"
        )
    ),
    HistoryItem(
        id = 7,
        date = "Yesterday",
        calculation = Calculation(
            expression = "23857 - 979400 + 9488 / 8858",
            result = "234555"
        )
    ),
    HistoryItem(
        id = 8,
        date = "Yesterday",
        calculation = Calculation(
            expression = "1 * 2 * 3 * 6",
            result = "56776"
        )
    ),
    HistoryItem(
        id = 9,
        date = "Yesterday",
        calculation = Calculation(
            expression = "999 * 4678",
            result = "2"
        )
    ),
    HistoryItem(
        id = 10,
        date = "Today",
        calculation = Calculation(
            expression = "1 + 1",
            result = "2"
        )
    ),
)
