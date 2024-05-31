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

package ir.erfansn.siliconecalculator.calculator.button.function

import ir.erfansn.siliconecalculator.calculator.button.FunctionButton
import ir.erfansn.siliconecalculator.data.model.Calculation

data object Equals : FunctionButton("=") {

    override val applier: (String) -> String = { it }

    override fun perform(calculation: Calculation): Calculation {
        if (!calculation.isComplete) return calculation

        val amendedExpression = if (calculation.result == "0")
            calculation.expression.substringBeforeLast(calculation.lastOperator)
        else
            calculation.expression.plus(calculation.result)

        evaluator.expression = applier(amendedExpression)

        return calculation.copy(expression = evaluator.expression, result = evaluator.eval())
    }
}
