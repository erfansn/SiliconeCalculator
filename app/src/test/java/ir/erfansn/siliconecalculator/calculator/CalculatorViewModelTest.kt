package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CalculatorViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxed = true)
    lateinit var historyRepository: HistoryRepository
    lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle()
        viewModel = CalculatorViewModel(savedStateHandle, historyRepository)
    }

    @Test
    fun `Shows '0' when zero button repeatedly is pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(0))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(0))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(0))

            assertThat(expectMostRecentItem().computation.result).isEqualTo("0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows the corresponding number when various digit buttons are pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(0))

            assertThat(expectMostRecentItem().computation.result).isEqualTo("120")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows only one decimal point when decimal button repeatedly is pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Decimal)
            viewModel.onNumPadButtonClick(CalculatorAction.Decimal)
            viewModel.onNumPadButtonClick(CalculatorAction.Decimal)

            assertThat(expectMostRecentItem().computation.result).isEqualTo("0.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows decimal number correctly`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
            viewModel.onNumPadButtonClick(CalculatorAction.Decimal)
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(0))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(0))

            assertThat(expectMostRecentItem().computation.result).isEqualTo("12.00")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Does not add extra decimal point when number has it`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Decimal)
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
            viewModel.onNumPadButtonClick(CalculatorAction.Decimal)

            assertThat(expectMostRecentItem().computation.result).isEqualTo("1.2")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Toggles sign an entered number from plus to minus and vice versa`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
            viewModel.onNumPadButtonClick(CalculatorAction.NumSign)
            assertThat(expectMostRecentItem().computation.result).isEqualTo("-12.0")

            viewModel.onNumPadButtonClick(CalculatorAction.NumSign)
            assertThat(expectMostRecentItem().computation.result).isEqualTo("12.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Calculates and shows one percent of a number`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
            viewModel.onNumPadButtonClick(CalculatorAction.Percent)

            assertThat(expectMostRecentItem().computation.result).isEqualTo("0.12")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Assigns minus sign to a number with scientific notation correctly`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
            viewModel.onNumPadButtonClick(CalculatorAction.Percent)
            viewModel.onNumPadButtonClick(CalculatorAction.Percent)
            viewModel.onNumPadButtonClick(CalculatorAction.Percent)
            viewModel.onNumPadButtonClick(CalculatorAction.NumSign)

            assertThat(expectMostRecentItem().computation.result).isEqualTo("-1.2E-5")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Adds corresponding operator when an operator button is pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Add)

            assertThat(expectMostRecentItem().computation.result).isEqualTo("0 + ")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Replaces last operator with corresponding operator when an operator button is pressed`() =
        runTest {
            viewModel.uiState.test {
                viewModel.onNumPadButtonClick(CalculatorAction.Add)
                assertThat(expectMostRecentItem().computation.result).isEqualTo("0 + ")

                viewModel.onNumPadButtonClick(CalculatorAction.Mul)
                assertThat(expectMostRecentItem().computation.result).isEqualTo("0 × ")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Shows entered expression correctly`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Add)
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))

            assertThat(expectMostRecentItem().computation.result).isEqualTo("0 + 1")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Changes last number sign and calculates and replaces with one percent of it correctly`() =
        runTest {
            viewModel.uiState.test {
                viewModel.onNumPadButtonClick(CalculatorAction.Add)
                viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
                viewModel.onNumPadButtonClick(CalculatorAction.NumSign)
                viewModel.onNumPadButtonClick(CalculatorAction.Percent)

                assertThat(expectMostRecentItem().computation.result).isEqualTo("0 + -0.01")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `No evaluates the entered expression when it was incomplete`() = runTest {
        viewModel.uiState.test {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
            viewModel.onNumPadButtonClick(CalculatorAction.Div)
            viewModel.onNumPadButtonClick(CalculatorAction.Equals)

            val uiState = expectMostRecentItem()
            assertThat(uiState.computation.result).isEqualTo("1 ÷ ")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Stops all buttons action when the expression evaluated is 'NaN' except AllClear button`() =
        runTest {
            viewModel.uiState.test {
                viewModel.onNumPadButtonClick(CalculatorAction.Div)
                viewModel.onNumPadButtonClick(CalculatorAction.Decimal)
                viewModel.onNumPadButtonClick(CalculatorAction.Equals)
                calculatorActionsList.filter { it != CalculatorAction.AllClear }
                    .forEach(viewModel::onNumPadButtonClick)

                var uiState = expectMostRecentItem()
                assertThat(uiState.computation.result).isEqualTo("NaN")

                viewModel.onNumPadButtonClick(CalculatorAction.AllClear)

                uiState = expectMostRecentItem()
                assertThat(uiState.computation.expression).isEqualTo("")
                assertThat(uiState.computation.result).isEqualTo("0")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Stops all buttons action when the expression evaluated is 'Infinity' except AllClear button`() =
        runTest {
            viewModel.uiState.test {
                viewModel.onNumPadButtonClick(CalculatorAction.AllClear)
                repeat(1000) {
                    viewModel.onNumPadButtonClick(CalculatorAction.Digit(9))
                }
                viewModel.onNumPadButtonClick(CalculatorAction.Add)
                viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
                viewModel.onNumPadButtonClick(CalculatorAction.Equals)
                calculatorActionsList.filter { it != CalculatorAction.AllClear }
                    .forEach(viewModel::onNumPadButtonClick)

                var uiState = expectMostRecentItem()
                assertThat(uiState.computation.result).isEqualTo("Infinity")

                viewModel.onNumPadButtonClick(CalculatorAction.AllClear)

                uiState = expectMostRecentItem()
                assertThat(uiState.computation.expression).isEqualTo("")
                assertThat(uiState.computation.result).isEqualTo("0")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Evaluates the entered expression correctly and save computation when result not 'NaN' or 'Infinity'`() =
        runTest {
            viewModel.uiState.test {
                viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
                viewModel.onNumPadButtonClick(CalculatorAction.Add)
                viewModel.onNumPadButtonClick(CalculatorAction.Digit(2))
                viewModel.onNumPadButtonClick(CalculatorAction.Equals)

                val uiState = expectMostRecentItem()
                assertThat(uiState.computation.expression).isEqualTo("1 + 2")
                assertThat(uiState.computation.result).isEqualTo("3.0")
            }

            coVerify(exactly = 1) { historyRepository.saveComputation(any()) }
            confirmVerified(historyRepository)
        }

    @Test
    fun `Does not save computation when result was 'NaN' or 'Infinity'`() {
        viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
        viewModel.onNumPadButtonClick(CalculatorAction.Add)
        viewModel.onNumPadButtonClick(CalculatorAction.Decimal)
        viewModel.onNumPadButtonClick(CalculatorAction.Equals)

        coVerify(exactly = 0) { historyRepository.saveComputation(any()) }
        confirmVerified(historyRepository)

        viewModel.onNumPadButtonClick(CalculatorAction.AllClear)
        repeat(1000) {
            viewModel.onNumPadButtonClick(CalculatorAction.Digit(9))
        }
        viewModel.onNumPadButtonClick(CalculatorAction.Add)
        viewModel.onNumPadButtonClick(CalculatorAction.Digit(1))
        viewModel.onNumPadButtonClick(CalculatorAction.Equals)

        coVerify(exactly = 0) { historyRepository.saveComputation(any()) }
        confirmVerified(historyRepository)
    }
}