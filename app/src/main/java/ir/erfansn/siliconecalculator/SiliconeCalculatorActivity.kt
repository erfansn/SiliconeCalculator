@file:OptIn(ExperimentalMaterialApi::class)

package ir.erfansn.siliconecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.erfansn.siliconecalculator.calculator.CalculatorScreen
import ir.erfansn.siliconecalculator.calculator.CalculatorViewModel
import ir.erfansn.siliconecalculator.history.HistoryScreen
import ir.erfansn.siliconecalculator.history.HistoryViewModel
import ir.erfansn.siliconecalculator.ui.animation.CircularReveal
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme
import ir.erfansn.siliconecalculator.util.encodeReservedChars

@AndroidEntryPoint
class SiliconeCalculatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isSystemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(isSystemDark) }
            val onThemeToggle = { darkTheme = !darkTheme }

            val navController = rememberNavController()
            CircularReveal(
                targetState = darkTheme,
                animationSpec = tween(500)
            ) { isDark ->
                SiliconeCalculatorTheme(darkTheme = isDark) {
                    SiliconeCalculatorScreenActivity(
                        navController = navController,
                        onThemeToggle = onThemeToggle
                    )
                }
            }
        }
    }
}

@Composable
fun SiliconeCalculatorScreenActivity(
    navController: NavHostController,
    onThemeToggle: () -> Unit,
) {
    Surface(color = MaterialTheme.colors.background) {
        NavHost(
            navController = navController,
            startDestination = "calculator?expression={expression}&result={result}",
        ) {
            composable("calculator?expression={expression}&result={result}") {
                val calculatorViewModel = hiltViewModel<CalculatorViewModel>()
                val uiState by calculatorViewModel.uiState.collectAsState()

                CalculatorScreen(
                    uiState = uiState,
                    onNumPadButtonClick = calculatorViewModel::onNumPadButtonClick,
                    onHistoryNav = { navController.navigate("history") },
                    onThemeToggle = onThemeToggle
                )
            }
            composable("history") {
                val historyViewModel = hiltViewModel<HistoryViewModel>()
                val uiState by historyViewModel.uiState.collectAsState()

                HistoryScreen(
                    uiState = uiState,
                    onHistoryClear = historyViewModel::onHistoryClear,
                    onBackPress = navController::popBackStack,
                    onComputationSelect = { (expression, result) ->
                        navController.navigate(
                            route = "calculator?expression=${expression.encodeReservedChars}&result=${result}"
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
