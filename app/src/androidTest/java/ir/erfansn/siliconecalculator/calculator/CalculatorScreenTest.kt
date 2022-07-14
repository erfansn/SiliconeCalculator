package ir.erfansn.siliconecalculator.calculator

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import ir.erfansn.siliconecalculator.data.model.Computation
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ir.erfansn.siliconecalculator.R

class CalculatorScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var mathematicalExp: String
    lateinit var evaluationResult: String
    lateinit var selectionContainerResult: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            mathematicalExp = getString(R.string.mathematical_exp)
            evaluationResult = getString(R.string.evaluation_result)
            selectionContainerResult = getString(R.string.selection_container_result)
        }
    }

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
            onNodeWithContentDescription(mathematicalExp).assertTextEquals("")
            onNodeWithContentDescription(evaluationResult).assertTextEquals("0")
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

        composeTestRule.onNodeWithContentDescription(selectionContainerResult).assertDoesNotExist()
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

        composeTestRule.onNodeWithContentDescription(selectionContainerResult).assertExists()
    }
}