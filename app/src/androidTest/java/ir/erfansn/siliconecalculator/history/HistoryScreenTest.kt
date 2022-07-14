package ir.erfansn.siliconecalculator.history

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ir.erfansn.siliconecalculator.R

class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var nothingToShow: String
    private lateinit var historyItemsList: String

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            nothingToShow = getString(R.string.nothing_to_show)
            historyItemsList = getString(R.string.history_items_list)
        }
    }

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

        composeTestRule.onNodeWithText(nothingToShow)
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

        composeTestRule.onNodeWithContentDescription(historyItemsList)
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