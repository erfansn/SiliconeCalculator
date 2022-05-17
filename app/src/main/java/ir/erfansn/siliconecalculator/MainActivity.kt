package ir.erfansn.siliconecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.erfansn.siliconecalculator.calculator.CalculatorScreen
import ir.erfansn.siliconecalculator.calculator.CalculatorUiState
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val darkTheme = isSystemInDarkTheme()
            val (isDarkTheme, setTheme) = remember { mutableStateOf(darkTheme) }

            SiliconeCalculatorTheme(darkTheme = isDarkTheme) {
                SiliconeCalculatorScreenActivity(isDarkTheme, setTheme)
            }
        }
    }
}

@Composable
fun SiliconeCalculatorScreenActivity(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
) {
    Surface(color = MaterialTheme.colors.background) {
        CalculatorScreen(
            uiState = CalculatorUiState("4,900 + 15,910", "20,810000000"),
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle,
            onButtonClick = { },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SiliconeCalculatorTheme {
        SiliconeCalculatorScreenActivity(
            isDarkTheme = false,
            onThemeToggle = { }
        )
    }
}
