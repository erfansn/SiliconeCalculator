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
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.calculator.button.operator.Add
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
class SiliconeCalculatorTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<SiliconeCalculatorActivity>()

    @Test
    fun savedCalculation_whenRetrieveItFromHistory_showItAsCurrentCalculation() {
        with(composeRule) {
            onNodeWithContentDescription(activity.getString(R.string.calculations_history))
                .performClick()
            onNodeWithContentDescription(activity.getString(R.string.clear_history))
                .performClick()
            onNodeWithTag("history:clear")
                .performClick()

            listOf("1", "2", Add.symbol, "3", "4", Equals.symbol).forEach {
                onNodeWithTag("calculator:$it")
                    .performClick()
            }
            onNodeWithTag("calculator:expression")
                .assertTextEquals("12 + 34")
            onNodeWithTag("calculator:result")
                .assertTextEquals("46.0")

            onNodeWithTag("calculator:${AllClear.symbol}")
                .performClick()
            onNodeWithTag("calculator:expression")
                .assertTextEquals("")
            onNodeWithTag("calculator:result")
                .assertTextEquals("0")

            onNodeWithContentDescription(activity.getString(R.string.calculations_history))
                .performClick()
            onNodeWithTag("history:items")
                .onChildren()
                .onFirst()
                .performClick()

            onNodeWithTag("calculator:expression")
                .assertTextEquals("12 + 34", includeEditableText = false)
            onNodeWithTag("calculator:result")
                .assertTextEquals("46.0")
        }
    }
}
