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

package ir.erfansn.siliconecalculator

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalComposeUiApi
@HiltAndroidTest
class SiliconeCalculatorTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<SiliconeCalculatorActivity>()

    @Inject
    lateinit var historyRepository: HistoryRepository

    private lateinit var mathematicalExp: String
    private lateinit var evaluationResult: String
    private lateinit var calculationsHistory: String

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.apply {
            calculationsHistory = getString(R.string.calculations_history)
        }
        mathematicalExp = "calculator:expression"
        evaluationResult = "calculator:result"
    }

    @Test
    fun savedCalculation_whenRetrieveItFromHistory_showItAsCurrentCalculation() {
        runBlocking {
            historyRepository.clearAllHistory()
            historyRepository.saveCalculation(
                calculation = Calculation(
                    expression = "1 + 1",
                    result = "2"
                )
            )
        }

        with(composeRule) {
            onNodeWithTag(mathematicalExp)
                .assertTextEquals("")
            onNodeWithTag(evaluationResult)
                .assertTextEquals("0")

            onNodeWithContentDescription(calculationsHistory)
                .performClick()
            onNodeWithTag("history:items")
                .onChildren()
                .filterToOne(hasTextExactly("1 + 1", "2"))
                .performClick()

            onNodeWithTag(mathematicalExp)
                .assertTextEquals("1 + 1")
            onNodeWithTag(evaluationResult)
                .assertTextEquals("2")
        }
    }
}
