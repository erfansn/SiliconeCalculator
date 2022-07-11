package ir.erfansn.siliconecalculator.calculator

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import ir.erfansn.siliconecalculator.data.model.Computation
import org.junit.Rule
import org.junit.Test

class CalculatorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialState_whenRead_showsExpressionAndResultCorrectly() {
        composeTestRule.setContent {
            CalculatorScreen(
                uiState = CalculatorUiState(),
                onNumPadButtonClick = { },
                onHistoryNav = { },
                onThemeToggle = { }
            )
        }

        with(composeTestRule) {
            onNodeWithContentDescription("Mathematical expression").assertTextEquals("")
            onNodeWithContentDescription("Evaluation result").assertTextEquals("0")
        }
    }

    @Test
    fun computation_whenNoExpressionEvaluated_resultIsNotSelectable() {
        composeTestRule.setContent {
            CalculatorScreen(
                uiState = CalculatorUiState(
                    computation = Computation(
                        result = "1 + 1"
                    )
                ),
                onNumPadButtonClick = { },
                onHistoryNav = { },
                onThemeToggle = { }
            )
        }

        with(composeTestRule) {
            onNodeWithContentDescription("Selectable result").assertDoesNotExist()
        }
    }

    @Test
    fun computation_whenExpressionEvaluated_resultIsSelectable() {
        composeTestRule.setContent {
            CalculatorScreen(
                uiState = CalculatorUiState(
                    computation = Computation(
                        expression = "1 + 1",
                        result = "2"
                    )
                ),
                onNumPadButtonClick = { },
                onHistoryNav = { },
                onThemeToggle = { }
            )
        }

        with(composeTestRule) {
            onNodeWithContentDescription("Selectable result").assertExists()
        }
    }
}