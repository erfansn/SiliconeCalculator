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

package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.calculator.button.common.Clear
import ir.erfansn.siliconecalculator.calculator.button.common.Decimal
import ir.erfansn.siliconecalculator.calculator.button.common.Digit
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.calculator.button.function.NumSign
import ir.erfansn.siliconecalculator.calculator.button.function.Percent
import ir.erfansn.siliconecalculator.calculator.button.operator.Add
import ir.erfansn.siliconecalculator.calculator.button.operator.Div
import ir.erfansn.siliconecalculator.calculator.button.operator.Mul
import ir.erfansn.siliconecalculator.calculator.button.operator.Sub
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.EXPRESSION_ARG
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.RESULT_ARG
import ir.erfansn.siliconecalculator.rule.MainDispatcherRule
import ir.erfansn.siliconecalculator.util.DECIMAL_REGEX
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

    @MockK
    lateinit var savedStateHandle: SavedStateHandle
    @MockK(relaxed = true)
    lateinit var historyRepository: HistoryRepository
    lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        every { savedStateHandle.get<String?>(any()) } returns null

        viewModel = CalculatorViewModel(savedStateHandle, historyRepository, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `Ignores '0's when zero button repeatedly is pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('0'))
            viewModel.performCalculatorButton(Digit('0'))
            viewModel.performCalculatorButton(Digit('0'))

            assertThat(expectMostRecentItem().calculation.result).isEqualTo("0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows the corresponding number when various digit buttons are pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Digit('0'))
            viewModel.performCalculatorButton(Digit('0'))

            assertThat(expectMostRecentItem().calculation.result).isEqualTo("1200")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows only one decimal point when decimal button repeatedly is pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Decimal)
            viewModel.performCalculatorButton(Decimal)
            viewModel.performCalculatorButton(Decimal)

            assertThat(expectMostRecentItem().calculation.result).isEqualTo("0.")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Shows decimal number correctly`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Decimal)
            viewModel.performCalculatorButton(Digit('0'))
            viewModel.performCalculatorButton(Digit('0'))

            assertThat(expectMostRecentItem().calculation.result).isEqualTo("0.00")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Toggles sign the number from plus to minus and vice versa`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(NumSign)
            assertThat(expectMostRecentItem().calculation.result).isEqualTo("-12.0")

            viewModel.performCalculatorButton(NumSign)
            assertThat(expectMostRecentItem().calculation.result).isEqualTo("12.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Calculates and shows one percent of the number`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Percent)

            assertThat(expectMostRecentItem().calculation.result).isEqualTo("0.12")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Doesn't add operator when was in initial state`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Sub)
            viewModel.performCalculatorButton(Mul)
            viewModel.performCalculatorButton(Div)

            val uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("")
            assertThat(uiState.calculation.result).isEqualTo("0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Adds corresponding operator when number was entered`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)

            assertThat(expectMostRecentItem().calculation.expression).isEqualTo("1 + ")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Replaces last operator with corresponding operator when it is pressed`() =
        runTest {
            viewModel.uiState.test {
                viewModel.performCalculatorButton(Digit('1'))
                viewModel.performCalculatorButton(Add)
                assertThat(expectMostRecentItem().calculation.expression).isEqualTo("1 + ")

                viewModel.performCalculatorButton(Mul)
                assertThat(expectMostRecentItem().calculation.expression).isEqualTo("1 × ")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `No evaluates the entered expression when it was incomplete`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Div)
            viewModel.performCalculatorButton(Equals)

            val uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("1 ÷ ")
            assertThat(uiState.calculation.result).isEqualTo("0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Evaluates the entered expression when it has complete`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Equals)

            val uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("1 + 2")
            assertThat(uiState.calculation.result).isEqualTo("3.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Evaluates the entered expression only one time`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Equals)
            viewModel.performCalculatorButton(Equals)

            val uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("1 + 2")
            assertThat(uiState.calculation.result).isEqualTo("3.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Doesn't change expression after evaluation`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Equals)

            viewModel.performCalculatorButton(Digit('9'))
            viewModel.performCalculatorButton(Percent)
            viewModel.performCalculatorButton(Decimal)
            viewModel.performCalculatorButton(NumSign)

            val uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("1 + 2")
            assertThat(uiState.calculation.result).isEqualTo("3.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Evaluates the entered expression when it has extra operator`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Mul)

            var uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("1 + 2 × ")

            viewModel.performCalculatorButton(Equals)

            uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("1 + 2")
            assertThat(uiState.calculation.result).isEqualTo("3.0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Moves the result to expression when after evaluating a operator button pressed`() = runTest {
        viewModel.uiState.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Equals)
            viewModel.performCalculatorButton(Sub)

            val uiState = expectMostRecentItem()
            assertThat(uiState.calculation.expression).isEqualTo("3.0 - ")
            assertThat(uiState.calculation.result).isEqualTo("0")

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Stops all buttons action when the expression evaluated isn't finite except AllClear button`() =
        runTest {
            viewModel.uiState.test {
                repeat(1000) { viewModel.performCalculatorButton(Digit('9')) }
                viewModel.performCalculatorButton(Add)
                viewModel.performCalculatorButton(Digit('1'))
                viewModel.performCalculatorButton(Equals)
                viewModel.calculatorButtons.value.filterNot { it == AllClear }.forEach(viewModel::performCalculatorButton)

                var uiState = expectMostRecentItem()
                assertThat(uiState.calculation.result).doesNotMatch(DECIMAL_REGEX)

                viewModel.performCalculatorButton(AllClear)

                uiState = expectMostRecentItem()
                assertThat(uiState.calculation.expression).isEqualTo("")
                assertThat(uiState.calculation.result).isEqualTo("0")

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Save calculation only one time in history when result was neither 'NaN' or 'Infinity'`() {
        viewModel.performCalculatorButton(Digit('1'))
        viewModel.performCalculatorButton(Add)
        viewModel.performCalculatorButton(Digit('2'))

        viewModel.performCalculatorButton(Equals)
        viewModel.performCalculatorButton(Equals)

        coVerify(exactly = 1) { historyRepository.saveCalculation(any()) }
        confirmVerified(historyRepository)
    }

    @Test
    fun `Does not save calculation when result wasn't finite`() {
        viewModel.performCalculatorButton(Digit('1'))
        viewModel.performCalculatorButton(Div)
        viewModel.performCalculatorButton(Decimal)
        viewModel.performCalculatorButton(Digit('0'))

        viewModel.performCalculatorButton(Equals)

        coVerify(exactly = 0) { historyRepository.saveCalculation(any()) }
        confirmVerified(historyRepository)
    }

    @Test
    fun `Does not save again calculation when that retrieved`() {
        every { savedStateHandle.get<String>(EXPRESSION_ARG) } returns "1 + 3 - 6"
        every { savedStateHandle.get<String>(RESULT_ARG) } returns "-2.0"

        viewModel.performCalculatorButton(Equals)

        coVerify(exactly = 0) { historyRepository.saveCalculation(any()) }
        confirmVerified(historyRepository)
    }

    @Test
    fun `Gets calculator keys with AllClear button when expression evaluated`() = runTest {
        viewModel.calculatorButtons.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))
            viewModel.performCalculatorButton(Equals)

            val keys = expectMostRecentItem()

            assertThat(keys).contains(AllClear)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Gets calculator keys with 'Clear' button when expression hasn't evaluated`()  = runTest {
        viewModel.calculatorButtons.test {
            viewModel.performCalculatorButton(Digit('1'))
            viewModel.performCalculatorButton(Add)
            viewModel.performCalculatorButton(Digit('2'))

            val keys = expectMostRecentItem()

            assertThat(keys).contains(Clear)
            cancelAndIgnoreRemainingEvents()
        }
    }
}