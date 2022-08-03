package ir.erfansn.siliconecalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.erfansn.siliconecalculator.calculator.CalculatorScreen
import ir.erfansn.siliconecalculator.calculator.CalculatorViewModel
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.history.HistoryScreen
import ir.erfansn.siliconecalculator.history.HistoryViewModel
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinations.CALCULATOR_ROUTE
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinations.HISTORY_ROUTE

@Composable
fun SiliconeCalculatorNavHost(
    navController: NavHostController = rememberNavController(),
    navActions: AppNavigationActions = remember(navController) {
        AppNavigationActions(navController)
    },
    onThemeToggle: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = CALCULATOR_ROUTE,
    ) {
        composable(CALCULATOR_ROUTE) {
            val calculatorViewModel = hiltViewModel<CalculatorViewModel>()
            val uiState by calculatorViewModel.uiState.collectAsState()

            CalculatorScreen(
                uiState = uiState,
                onCalculatorButtonClick = {
                    calculatorViewModel.performCalculatorButton(it)
                    if (it == Equals) calculatorViewModel.saveComputationInHistory()
                },
                onHistoryNav = navActions::navigateToHistory,
                onThemeToggle = onThemeToggle
            )
        }
        composable(HISTORY_ROUTE) {
            val historyViewModel = hiltViewModel<HistoryViewModel>()
            val uiState by historyViewModel.uiState.collectAsState()

            HistoryScreen(
                uiState = uiState,
                onHistoryClear = historyViewModel::onHistoryClear,
                onBackPress = navActions::onBackPress,
                onCalculationClick = navActions::navigateToCalculator
            )
        }
    }
}