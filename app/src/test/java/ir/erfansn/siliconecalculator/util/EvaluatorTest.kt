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