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

package ir.erfansn.siliconecalculator.calculator.button

import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.calculator.button.common.Decimal
import ir.erfansn.siliconecalculator.calculator.button.common.Digit
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.calculator.button.function.NumSign
import ir.erfansn.siliconecalculator.calculator.button.function.Percent
import ir.erfansn.siliconecalculator.calculator.button.operator.Add
import ir.erfansn.siliconecalculator.calculator.button.operator.Div
import ir.erfansn.siliconecalculator.calculator.button.operator.Mul
import ir.erfansn.siliconecalculator.calculator.button.operator.Sub
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.util.Evaluator

abstract class CalculatorButton(val symbol: String) {
    open val applier = { n: String -> "$n $symbol " }
    abstract fun Calculation.perform(): Calculation
}

open class FunctionButton(symbol: String) : CalculatorButton(symbol) {

    protected val evaluator = Evaluator()

    override fun Calculation.perform(): Calculation {
        if (result == "0") return this

        evaluator.expression = applier(result)

        return copy(result = evaluator.eval())
    }
}

open class OperatorButton(symbol: String) : CalculatorButton(symbol) {

    override fun Calculation.perform(): Calculation {
        if (expression.isEmpty() && result == "0") return this

        val amendedExpression = when {
            result == "0" -> expression.substringBeforeLast(lastOperator)
            expression.endsWith(lastOperator) -> expression.plus(result)
            else -> result
        }

        return copy(
            expression = applier(amendedExpression),
            result = "0"
        )
    }
}

val calculatorButtons = listOf(
    AllClear,
    NumSign,
    Percent,
    Div,
    Digit('7'),
    Digit('8'),
    Digit('9'),
    Mul,
    Digit('4'),
    Digit('5'),
    Digit('6'),
    Sub,
    Digit('1'),
    Digit('2'),
    Digit('3'),
    Add,
    Digit('0'),
    Decimal,
    Equals,
)
