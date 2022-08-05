package ir.erfansn.siliconecalculator.calculator

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import ir.erfansn.siliconecalculator.data.repository.FakeHistoryRepository
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
                remember { CalculatorViewModel(SavedStateHandle(), FakeHistoryRepository()) }
            val uiState by viewModel.uiState.collectAsState()

            CalculatorScreen(
                uiState = uiState,
                onCalculatorButtonClick = viewModel::performCalculatorButton,
                onHistoryNav = { },
                onThemeToggle = { }
            )
        }
    }
}