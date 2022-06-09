package ir.erfansn.siliconecalculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.erfansn.siliconecalculator.calculator.CalculatorScreen
import ir.erfansn.siliconecalculator.calculator.CalculatorUiState
import ir.erfansn.siliconecalculator.history.HistoryScreen
import ir.erfansn.siliconecalculator.ui.component.CircularReveal
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isSystemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(isSystemDark) }
            val onThemeToggle = { darkTheme = !darkTheme }

            CircularReveal(
                targetState = darkTheme,
                animationSpec = tween(500)
            ) { isDark ->
                SiliconeCalculatorTheme(darkTheme = isDark) {
                    SiliconeCalculatorScreenActivity(onThemeToggle = onThemeToggle)
                }
            }
        }
    }
}

@Composable
fun SiliconeCalculatorScreenActivity(
    onThemeToggle: () -> Unit,
) {
    Surface(color = MaterialTheme.colors.background) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "calculator",
        ) {
            composable(
                "calculator",
            ) {
                CalculatorScreen(
                    uiState = CalculatorUiState("4,900 + 15,910", "20,810"),
                    onButtonClick = { },
                    onHistoryNav = { navController.navigate("history") },
                    onThemeToggle = onThemeToggle
                )
            }
            composable("history") {
                HistoryScreen(
                    onClearHistory = { },
                    onBackPress = { navController.popBackStack() }
                )
            }
        }
    }
}

@Preview(
    name = "Light theme",
    showBackground = true,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun DefaultPreview() {
    SiliconeCalculatorTheme {
        SiliconeCalculatorScreenActivity(onThemeToggle = { })
    }
}
