package ir.erfansn.siliconecalculator.history

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import org.junit.Rule
import org.junit.Test

class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noItem_whenNoItemForShowing_showAppropriateMessage() {
        composeTestRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(),
                onBackPress = { },
                onHistoryClear = { },
                onComputationSelect = { }
            )
        }

        composeTestRule.onNodeWithText("Nothing to show!")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun items_whenHistoryIsNotEmpty_showItems() {
        composeTestRule.setContent {
            HistoryScreen(
                uiState = HistoryUiState(testHistoryItems),
                onBackPress = { },
                onHistoryClear = { },
                onComputationSelect = { }
            )
        }

        composeTestRule.onNodeWithContentDescription("History items")
            .assertExists()
            .assertIsDisplayed()
    }
}

private val testHistoryItems = listOf(
    HistoryItem(
        id = 0,
        date = "April 21",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
    HistoryItem(
        id = 1,
        date = "April 21",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
    HistoryItem(
        id = 2,
        date = "April 21",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
    HistoryItem(
        id = 3,
        date = "Today",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
    HistoryItem(
        id = 4,
        date = "Today",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
    HistoryItem(
        id = 5,
        date = "Today",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    ),
    HistoryItem(
        id = 6,
        date = "Today",
        computation = Computation(
            expression = "1 + 1",
            result = "2"
        )
    )
)