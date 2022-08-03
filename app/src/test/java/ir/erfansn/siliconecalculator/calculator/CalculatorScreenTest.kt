package ir.erfansn.siliconecalculator.calculator

import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import ir.erfansn.siliconecalculator.R
import ir.erfansn.siliconecalculator.data.repository.FakeHistoryRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CalculatorScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var mathematicalExp: String
    lateinit var evaluationResult: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            mathematicalExp = getString(R.string.mathematical_exp)
            evaluationResult = getString(R.string.evaluation_result)
        }

        setContent()
    }

    @Test
    fun initialState_whenButtonsOfKeyLayoutClicked_showsExpressionAndResultCorrectly() {
        with(composeTestRule) {
            val nodeTexts = listOf("1", ".", "2", "+", "2", "%", "±")
            nodeTexts.forEach {
                onNode(hasText(it) and hasClickAction()).performClick()
            }

            onNodeWithContentDescription(mathematicalExp).assertTextEquals("1.2 + ")
            onNodeWithContentDescription(evaluationResult).assertTextEquals("-0.02")
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