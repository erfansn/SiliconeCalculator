/*
 * Copyright 2024 Erfan Sn
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

package ir.erfansn.siliconecalculator.calculator.button.common

import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.util.DECIMAL_REGEX

data object Clear : CalculatorButton("C") {

    override val applier: (String) -> String = {
        it.substring(0, it.length - 1).let { reducedString ->
            if (reducedString.isEmpty() || reducedString == "-") {
                "0"
            } else {
                reducedString
            }
        }
    }

    override fun perform(calculation: Calculation): Calculation {
        val (amendExpression, amendResult) = if (calculation.expression.isEmpty() || applier(calculation.result) != "0") {
            calculation.expression to applier(calculation.result)
        } else {
            calculation.expression.substringBeforeLast(calculation.lastNumber) to calculation.lastNumber
        }

        return calculation.copy(
            expression = amendExpression,
            result = amendResult
        )
    }

    private val Calculation.lastNumber
        get() = DECIMAL_REGEX.toRegex().findAll(expression).last().value
}
