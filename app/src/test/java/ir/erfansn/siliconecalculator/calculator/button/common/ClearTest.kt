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

import com.google.common.truth.Truth.assertThat
import ir.erfansn.siliconecalculator.data.model.Calculation
import org.junit.Test

class ClearTest {

    @Test
    fun `Clears one digit when a number is in result`() {
        val calculation = Calculation(
            result = "1234"
        )

        val result = Clear.perform(calculation)

        assertThat(result.result).isEqualTo("123")
    }

    @Test
    fun `Sets zero as result when clearing the last digit with sign`() {
        val calculation = Calculation(
            result = "-1"
        )

        val result = Clear.perform(calculation)

        assertThat(result.result).isEqualTo("0")
    }

    @Test
    fun `Moves last number from expression to result when clearing last digit in result`() {
        val calculation = Calculation(
            expression = "12 + -23 -",
            result = "-1"
        )

        val result = Clear.perform(calculation)

        assertThat(result.result).isEqualTo("-23")
    }
}
