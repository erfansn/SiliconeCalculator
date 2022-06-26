package ir.erfansn.siliconecalculator.data.model

data class HistoryItem(
    val id: Int,
    val date: String,
    val computation: Computation,
)

data class Computation(
    val expression: String = "",
    val result: String = "0",
)

val previewHistoryItems = listOf(
    HistoryItem(
        id = 0,
        date = "12 April",
        computation = Computation(
            expression = "1 + 788 * 875",
            result = "10"
        )
    ),
    HistoryItem(
        id = 1,
        date = "12 April",
        computation = Computation(
            expression = "68774 + 9888 * 4763 / 9847",
            result = "2675.09"
        )
    ),
    HistoryItem(
        id = 2,
        date = "15 March",
        computation = Computation(
            expression = "458867 / 76",
            result = "0.002"
        )
    ),
    HistoryItem(
        id = 3,
        date = "15 April",
        computation = Computation(
            expression = "9475 * 0.88888",
            result = "4755.2"
        )
    ),
    HistoryItem(
        id = 4,
        date = "19 April",
        computation = Computation(
            expression = "47362 / 1 / 98585",
            result = "12345"
        )
    ),
    HistoryItem(
        id = 5,
        date = "19 April",
        computation = Computation(
            expression = "5452 - 97584 + 9573 / 848 * 764",
            result = "14795"
        )
    ),
    HistoryItem(
        id = 6,
        date = "19 April",
        computation = Computation(
            expression = "12 - 957 + 857 - 9588 / 4388 * 8746",
            result = "25874333"
        )
    ),
    HistoryItem(
        id = 7,
        date = "Yesterday",
        computation = Computation(
            expression = "23857 - 979400 + 9488 / 8858",
            result = "234555"
        )
    ),
    HistoryItem(
        id = 8,
        date = "Yesterday",
        computation = Computation(
            expression = "1 * 2 * 3 * 6",
            result = "56776"
        )
    ),
    HistoryItem(
        id = 9,
        date = "Yesterday",
        computation = Computation(
            expression = "999 * 4678",
            result = "2"
        )
    ),
    HistoryItem(
        id = 10,
        date = "Today",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
)
