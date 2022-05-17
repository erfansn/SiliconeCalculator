package ir.erfansn.siliconecalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = BlueGrey900,
    primaryVariant = BlueGrey700,
    secondary = DeepOrange800,
    background = BlueGrey900,
    surface = BlueGrey600,
    onPrimary = BlueGrey50,
    onSecondary = BlueGrey50,
    onBackground = BlueGrey50,
    onSurface = BlueGrey100,
)

private val LightColorPalette = lightColors(
    primary = BlueGrey100,
    primaryVariant = BlueGrey300,
    secondary = DeepOrange900,
    background = BlueGrey50,
    surface = BlueGrey100,
    onPrimary = BlueGrey800,
    onSecondary = BlueGrey50,
    onBackground = BlueGrey800,
    onSurface = BlueGrey800,
)

@Composable
fun SiliconeCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUi = rememberSystemUiController()
    systemUi.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = !darkTheme,
        isNavigationBarContrastEnforced = false
    )

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}