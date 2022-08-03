package ir.erfansn.siliconecalculator

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
    private lateinit var themeChanger: String
    private lateinit var calculationsHistory: String

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.apply {
            mathematicalExp = getString(R.string.mathematical_exp)
            evaluationResult = getString(R.string.evaluation_result)

            themeChanger = getString(R.string.theme_changer)
            calculationsHistory = getString(R.string.calculations_history)
        }
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
            onNodeWithContentDescription(mathematicalExp)
                .assertTextEquals("")
            onNodeWithContentDescription(evaluationResult)
                .assertTextEquals("0")

            onNodeWithContentDescription(calculationsHistory)
                .performClick()
            onNodeWithTag("history:items")
                .onChildren()
                .filterToOne(hasTextExactly("1 + 1", "2"))
                .performClick()

            onNodeWithContentDescription(mathematicalExp)
                .assertTextEquals("1 + 1")
            onNodeWithContentDescription(evaluationResult)
                .assertTextEquals("2")
        }
    }
}
