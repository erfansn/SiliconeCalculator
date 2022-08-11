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

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EvaluatorTest {

    private var evaluator = Evaluator()

    @Test
    fun `Returns 'NaN' when expression '1 div 0' is evaluated`() {
        val result = eval("1 ÷ 0")

        assertThat(result.toDouble()).isEqualTo(Double.NaN)
    }

    @Test
    fun `Returns '121,0' when expression '11 mul 11' is evaluated`() {
        val result = eval("2 × 2")

        assertThat(result.toDouble()).isEqualTo(4.0)
    }

    @Test
    fun `Returns '1' when expression '1 + 0,' is evaluated`() {
        val result = eval("1 + 0.")

        assertThat(result.toDouble()).isEqualTo(1.0)
    }

    @Test
    fun `Returns '1' when expression '1 + 9r1000' is evaluated`() {
        val result = eval("1 + ${"9".repeat(1000)}")

        assertThat(result.toDouble()).isEqualTo(Double.POSITIVE_INFINITY)
    }

    @Test
    fun `Returns '1' when expression '1 - 9r1000' is evaluated`() {
        val result = eval("1 - ${"9".repeat(1000)}")

        assertThat(result.toDouble()).isEqualTo(Double.NEGATIVE_INFINITY)
    }
    
    private fun eval(expression: String): String {
        evaluator.expression = expression    
        return evaluator.eval()
    }
}