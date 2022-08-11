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

const val OPERATORS_REGEX = """[+\-×÷]"""
const val DECIMAL_REGEX = """-?\d+\.?(?:\d+(?:E-?\d+)?)?"""

private const val SPECIFIC_NUMBER_REGEX = """(-)?(0|\d*)(\.)?(\d*)(E-?)?(\d+)?"""

fun String.formatNumbers(): String {
    fun String.separateByComma() = reversed().chunked(3).joinToString(",").reversed()

    return replace(SPECIFIC_NUMBER_REGEX.toRegex()) {
        val (integerSign, integer, point, fraction, exponentSign, exponent) = it.destructured

        "$integerSign${integer.separateByComma()}$point$fraction$exponentSign${exponent.separateByComma()}"
    }
}