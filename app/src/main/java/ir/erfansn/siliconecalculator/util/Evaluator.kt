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

package ir.erfansn.siliconecalculator.util

import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License

class Evaluator {

    init {
        License.iConfirmNonCommercialUse("Erfan Sn")
    }

    private val _expression = Expression()

    var expression: String = ""
       set(value) {
           field = value.amendExpression().also(_expression::setExpressionString)
       }

    fun eval(): String = _expression.calculate().toBigDecimal().toPlainString()

    private fun String.amendExpression(): String {
        return replace(
            regex = """(\d+)\.(\d*)""".toRegex(),
        ) { result ->
            val (integer, fraction) = result.destructured
            "$integer.${fraction.ifEmpty { "0" }}"
        }
    }
}
