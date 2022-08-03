package ir.erfansn.siliconecalculator.history

import androidx.activity.ComponentActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import ir.erfansn.siliconecalculator.R
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.repository.FakeHistoryRepository
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HistoryScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var nothingToShow: String
    private lateinit var clearHistory: String
    private lateinit var clear: String

    private val fakeHistoryRepository = FakeHistoryRepository()

    @Before
    fun setUp() {
        composeTestRule.activity.apply {
            nothingToShow = getString(R.string.nothing_to_show)
            clearHistory = getString(R.string.clear_history)
            clear = getString(R.string.clear)
        }
    }

    @Test
    fun noItem_whenNoItemForShowing_showAppropriateMessage() {
        setContent()

        with(composeTestRule) {
            onNodeWithTag("history:items")
                .assertDoesNotExist()

            onNodeWithText(nothingToShow)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun items_whenHistoryIsNotEmpty_showItems() {
        fakeHistoryRepository.saveCalculationBlocking(
            calculation = Calculation(
                expression = "1 + 1",
                result = "2"
            )
        )

        setContent()

        with(composeTestRule) {
            onNodeWithText(nothingToShow)
                .assertDoesNotExist()

            onNodeWithTag("history:items")
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun items_whenClickOnClearHistory_clearItems() {
        fakeHistoryRepository.saveCalculationBlocking(
            calculation = Calculation(
                expression = "2 + 1",
                result = "2"
            )
        )

        setContent()

        with(composeTestRule) {
            onNodeWithContentDescription(clearHistory)
                .performClick()

            onNode(hasText(clear) and hasClickAction())
                .performClick()

            onNodeWithTag("history:items")
                .assertDoesNotExist()

            onNodeWithText(nothingToShow)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    private fun setContent() {
        composeTestRule.setContent {
            val viewModel = remember { HistoryViewModel(fakeHistoryRepository) }
            val uiState by viewModel.uiState.collectAsState()

            HistoryScreen(
                uiState = uiState,
                onBackPress = { },
                onHistoryClear = viewModel::onHistoryClear,
                onCalculationClick = { }
            )
        }
    }
}

private fun HistoryRepository.saveCalculationBlocking(calculation: Calculation) {
    runBlocking { saveCalculation(calculation) }
}