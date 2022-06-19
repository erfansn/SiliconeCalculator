package ir.erfansn.siliconecalculator

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import ir.erfansn.siliconecalculator.calculator.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.buttonList
import ir.erfansn.siliconecalculator.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AppSharedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AppSharedViewModel

    @Before
    fun setUp() {
        viewModel = AppSharedViewModel()
    }

    @Test
    fun `Shows '0' when zero button repeatedly is pressed`() = runTest {
        viewModel.onNumberPadClick(CalculatorButton.Digit(0))
        viewModel.onNumberPadClick(CalculatorButton.Digit(0))
        viewModel.onNumberPadClick(CalculatorButton.Digit(0))

        val uiState = viewModel.calculatorUiState.first()
        assertThat(uiState.evaluationResult).isEqualTo("0")
    }

    @Test
    fun `Shows the corresponding number when various digit buttons are pressed`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(2))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("120")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows only one decimal point when decimal button repeatedly is pressed`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Decimal)
            viewModel.onNumberPadClick(CalculatorButton.Decimal)
            viewModel.onNumberPadClick(CalculatorButton.Decimal)

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows decimal number correctly`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(2))
            viewModel.onNumberPadClick(CalculatorButton.Decimal)
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("12.00")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Does not add extra decimal point when number has it`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Decimal)
            viewModel.onNumberPadClick(CalculatorButton.Digit(2))
            viewModel.onNumberPadClick(CalculatorButton.Decimal)

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("1.2")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Toggles sign an entered number from plus to minus and vice versa`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(2))
            viewModel.onNumberPadClick(CalculatorButton.NumSign)
            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("-12.0")

            viewModel.onNumberPadClick(CalculatorButton.NumSign)
            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("12.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Calculates and shows one percent of a number`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(2))
            viewModel.onNumberPadClick(CalculatorButton.Percent)

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0.12")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Assigns minus sign to a number with scientific notation correctly`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(2))
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.NumSign)

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("-1.2E-5")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Adds corresponding operator when an operator button is pressed`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Add)

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0 + ")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Replaces last operator with corresponding operator when an operator button is pressed`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Add)
            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0 + ")

            viewModel.onNumberPadClick(CalculatorButton.Mul)
            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0 × ")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows entered expression correctly`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Add)
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0 + 1")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Changes last number sign and calculates and replaces with one percent of it correctly`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Add)
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.NumSign)
            viewModel.onNumberPadClick(CalculatorButton.Percent)

            assertThat(expectMostRecentItem().evaluationResult).isEqualTo("0 + -0.01")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Evaluates the entered expression correctly`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(7))
            viewModel.onNumberPadClick(CalculatorButton.Div)
            viewModel.onNumberPadClick(CalculatorButton.Digit(4))
            viewModel.onNumberPadClick(CalculatorButton.NumSign)
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.Percent)
            viewModel.onNumberPadClick(CalculatorButton.Sub)
            viewModel.onNumberPadClick(CalculatorButton.Digit(5))
            viewModel.onNumberPadClick(CalculatorButton.Mul)
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Add)
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(5))
            viewModel.onNumberPadClick(CalculatorButton.Equals)

            val uiState = expectMostRecentItem()
            assertThat(uiState.mathExpression).isEqualTo("7 ÷ -4.0E-8 - 5 × 1000 + 15")
            assertThat(uiState.evaluationResult).isEqualTo("-1.75004985E8")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Evaluates the entered expression into 'NaN'`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Digit(7))
            viewModel.onNumberPadClick(CalculatorButton.Div)
            viewModel.onNumberPadClick(CalculatorButton.Decimal)
            viewModel.onNumberPadClick(CalculatorButton.Sub)
            viewModel.onNumberPadClick(CalculatorButton.Digit(5))
            viewModel.onNumberPadClick(CalculatorButton.Mul)
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Digit(0))
            viewModel.onNumberPadClick(CalculatorButton.Add)
            viewModel.onNumberPadClick(CalculatorButton.Digit(1))
            viewModel.onNumberPadClick(CalculatorButton.Digit(5))
            viewModel.onNumberPadClick(CalculatorButton.Equals)

            val uiState = expectMostRecentItem()
            assertThat(uiState.mathExpression).isEqualTo("7 ÷ . - 5 × 1000 + 15")
            assertThat(uiState.evaluationResult).isEqualTo("NaN")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Stops all buttons action when the expression evaluated is 'NaN' except AllClear button`() = runTest {
        viewModel.calculatorUiState.test {
            viewModel.onNumberPadClick(CalculatorButton.Div)
            viewModel.onNumberPadClick(CalculatorButton.Decimal)
            viewModel.onNumberPadClick(CalculatorButton.Equals)
            buttonList.filter { it != CalculatorButton.AllClear }.forEach(viewModel::onNumberPadClick)

            var uiState = expectMostRecentItem()
            assertThat(uiState.mathExpression).isEqualTo("0 ÷ .")
            assertThat(uiState.evaluationResult).isEqualTo("NaN")

            viewModel.onNumberPadClick(CalculatorButton.AllClear)

            uiState = expectMostRecentItem()
            assertThat(uiState.mathExpression).isEqualTo("")
            assertThat(uiState.evaluationResult).isEqualTo("0")

            cancelAndIgnoreRemainingEvents()
        }
    }
}

