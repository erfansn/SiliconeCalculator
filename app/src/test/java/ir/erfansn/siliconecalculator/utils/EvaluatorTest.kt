package ir.erfansn.siliconecalculator.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EvaluatorTest {

    private var evaluator = Evaluator()

    @Test
    fun `Returns '-1,0' when expression '1 - 2' is evaluated`() {
        val expression = "1 - 2"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(-1.0)
    }

    @Test
    fun `Returns '2,0' when expression '1 + 1' is evaluated`() {
        val expression = "1 + 1"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(2.0)
    }

    @Test
    fun `Returns 'NaN' when expression '1 div 0' is evaluated`() {
        val expression = "1 ÷ 0"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(Double.NaN)
    }

    @Test
    fun `Returns '121,0' when expression '11 mul 11' is evaluated`() {
        val expression = "11 × 11"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(121.0)
    }

    @Test
    fun `Returns '30,25' when expression '11 mul 11 div 4' is evaluated`() {
        val expression = "11 × 11 ÷ 4"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(30.25)
    }

    @Test
    fun `Returns '30,25' when expression '100 + 11 mul 11 div 4 - 100' is evaluated`() {
        val expression = "100 + 11 × 11 ÷ 4 - 100"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(30.25)
    }

    @Test
    fun `Returns '230,25' when expression '100 + 11 mul 11 div 4 - -100' is evaluated`() {
        val expression = "100 + 11 × 11 ÷ 4 - -100"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(230.25)
    }

    @Test
    fun `Returns '100,0' when expression '100E2 div -100' is evaluated`() {
        val expression = "100E2 ÷ -100"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(-100.0)
    }

    @Test
    fun `Returns 'NaN' when expression '1 + ,' is evaluated`() {
        val expression = "1 + ."

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(Double.NaN)
    }

    @Test
    fun `Returns '1' when expression '1 + 0,' is evaluated`() {
        val expression = "1 + 0."

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(1.0)
    }

    @Test
    fun `Returns '1' when expression '1 + ,0' is evaluated`() {
        val expression = "1 + .0"

        val result = evaluator.eval(expression)

        assertThat(result.toDouble()).isEqualTo(1.0)
    }
}