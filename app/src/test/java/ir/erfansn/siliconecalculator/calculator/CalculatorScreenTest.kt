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

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import ir.erfansn.siliconecalculator.calculator.button.calculatorButtonsInOrderClear
import ir.erfansn.siliconecalculator.data.repository.FakeHistoryRepository
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CalculatorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        setContent()
    }

    @Test
    fun initialState_whenButtonsOfKeyLayoutClicked_showsExpressionAndResultCorrectly() {
        with(composeTestRule) {
            val nodeTexts = listOf("1", ".", "2", "+", "2", "%", "±")
            nodeTexts.forEach {
                onNodeWithTag("calculator:$it").performClick()
            }

            onNodeWithTag("calculator:expression").assertTextEquals("1.2 + ")
            onNodeWithTag("calculator:result").assertTextEquals("-0.02")
        }
    }

    private fun setContent() {
        composeTestRule.setContent {
            val viewModel =
                remember { CalculatorViewModel(SavedStateHandle(), FakeHistoryRepository(), Dispatchers.Main.immediate) }
            val uiState by viewModel.uiState.collectAsState()

            CalculatorScreen(
                uiState = uiState,
                onCalculatorButtonClick = viewModel::performCalculatorButton,
                onHistoryNav = { },
                onThemeToggle = { },
                calculatorButtons = calculatorButtonsInOrderClear
            )
        }
    }
}