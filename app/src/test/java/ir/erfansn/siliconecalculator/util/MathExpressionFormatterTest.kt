package ir.erfansn.siliconecalculator.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MathExpressionFormatterTest {

    @Test
    fun `Returns empty when given input is empty`() {
        assertThat("".formatNumbers()).isEmpty()
    }

    @Test
    fun `Returns '100,000' when given input is '100000'`() {
        assertThat("100000".formatNumbers()).isEqualTo("100,000")
    }

    @Test
    fun `Returns '1,000,000' when given input is '1000000'`() {
        assertThat("1000000".formatNumbers()).isEqualTo("1,000,000")
    }

    @Test
    fun `Returns 'dot' when given input is 'dot'`() {
        assertThat(".".formatNumbers()).isEqualTo(".")
    }

    @Test
    fun `Returns 'dot0000' when given input is 'dot0000'`() {
        assertThat(".0000".formatNumbers()).isEqualTo(".0000")
    }

    @Test
    fun `Returns 'dot0000E0,000' when given input is 'dot0000E0000'`() {
        assertThat(".0000E0000".formatNumbers()).isEqualTo(".0000E0,000")
    }

    @Test
    fun `Returns '1,000dot' when given input is '1000dot'`() {
        assertThat("1000.".formatNumbers()).isEqualTo("1,000.")
    }

    @Test
    fun `Returns '1,000dot0000' when given input is '1000dot0000'`() {
        assertThat("1000.0000".formatNumbers()).isEqualTo("1,000.0000")
    }

    @Test
    fun `Returns '1,000dot0000E0,000' when given input is '1000dot0000E0000'`() {
        assertThat("1000.0000E0000".formatNumbers()).isEqualTo("1,000.0000E0,000")
    }

    @Test
    fun `Returns '1,000dot0E0,000 + 1,000' when given input is '1000dot0000E0,000 + 1000'`() {
        assertThat("1000dot0E0,000 + 1000".formatNumbers()).isEqualTo("1,000dot0E0,000 + 1,000")
    }
}