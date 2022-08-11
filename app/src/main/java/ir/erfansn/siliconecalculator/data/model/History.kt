/*
 * Copyright 2022 Erfan Sn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.erfansn.siliconecalculator.data.model

import ir.erfansn.siliconecalculator.data.source.local.db.model.HistoryEntity
import ir.erfansn.siliconecalculator.util.OPERATORS_REGEX

data class History(
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
    History(
        id = 0,
        date = "12 April",
        calculation = Calculation(
            expression = "1 + 788 * 875",
            result = "10"
        )
    ),
    History(
        id = 1,
        date = "12 April",
        calculation = Calculation(
            expression = "68774 + 9888 * 4763 / 9847",
            result = "2675.09"
        )
    ),
    History(
        id = 2,
        date = "15 March",
        calculation = Calculation(
            expression = "458867 / 76",
            result = "0.002"
        )
    ),
    History(
        id = 3,
        date = "15 April",
        calculation = Calculation(
            expression = "9475 * 0.88888",
            result = "4755.2"
        )
    ),
    History(
        id = 4,
        date = "19 April",
        calculation = Calculation(
            expression = "47362 / 1 / 98585",
            result = "12345"
        )
    ),
    History(
        id = 5,
        date = "19 April",
        calculation = Calculation(
            expression = "5452 - 97584 + 9573 / 848 * 764",
            result = "14795"
        )
    ),
    History(
        id = 6,
        date = "19 April",
        calculation = Calculation(
            expression = "12 - 957 + 857 - 9588 / 4388 * 8746",
            result = "25874333"
        )
    ),
    History(
        id = 7,
        date = "Yesterday",
        calculation = Calculation(
            expression = "23857 - 979400 + 9488 / 8858",
            result = "234555"
        )
    ),
    History(
        id = 8,
        date = "Yesterday",
        calculation = Calculation(
            expression = "1 * 2 * 3 * 6",
            result = "56776"
        )
    ),
    History(
        id = 9,
        date = "Yesterday",
        calculation = Calculation(
            expression = "999 * 4678",
            result = "2"
        )
    ),
    History(
        id = 10,
        date = "Today",
        calculation = Calculation(
            expression = "1 + 1",
            result = "2"
        )
    ),
)
