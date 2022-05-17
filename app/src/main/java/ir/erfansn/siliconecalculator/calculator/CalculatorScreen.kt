@file:OptIn(ExperimentalMaterialApi::class)

package ir.erfansn.siliconecalculator.calculator

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import ir.erfansn.siliconecalculator.ui.component.FlatIconButton
import ir.erfansn.siliconecalculator.ui.component.SiliconeButton
import ir.erfansn.siliconecalculator.ui.layout.Grid
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme

@Composable
fun CalculatorScreen(
    uiState: CalculatorUiState,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onButtonClick: (CalculatorButton) -> Unit,
) {
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .systemGesturesPadding()
    ) {
        FlatIconButton(
            modifier = Modifier
                .layoutId("theme_changer")
                .aspectRatio(1.25f),
            onClick = { onThemeToggle(!isDarkTheme) },
            icon = if (isDarkTheme) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
            contentDescription = "Theme changer"
        )

        val calculatorState = rememberCalculatorState()
        CalculationContent(
            mathExpression = uiState.mathExpression,
            evaluationResult = uiState.evaluationResult,
            calculatorState = calculatorState
        )
        ButtonsLayoutContent(
            calculatorState = calculatorState,
            onButtonClick = onButtonClick
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun CalculationContent(
    mathExpression: String,
    evaluationResult: String,
    calculatorState: CalculatorState,
) {
    Column(
        modifier = Modifier
            .layoutId("calculation")
            .wrapContentSize(),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            modifier = Modifier
                .horizontalScroll(
                    state = calculatorState.mathExpressionScrollState,
                    reverseScrolling = true
                )
                .padding(horizontal = 20.dp)
                .alpha(ContentAlpha.medium),
            text = mathExpression,
            style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.Light,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                ),
            ),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier
                .horizontalScroll(
                    state = calculatorState.evaluationResultScrollState,
                    reverseScrolling = true
                )
                .padding(horizontal = 20.dp),
            text = evaluationResult,
            style = MaterialTheme.typography.h2.copy(
                fontWeight = FontWeight.Normal,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None
                ),
            ),
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ButtonsLayoutContent(
    calculatorState: CalculatorState,
    onButtonClick: (CalculatorButton) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .aspectRatio(calculatorState.buttonsLayoutAspectRation)
            .layoutId("buttons_layout")
    ) {
        Grid(
            columns = BUTTON_LAYOUT_COLUMNS_COUNT,
            modifier = Modifier.fillMaxSize(),
        ) {
            val buttonWidth = maxWidth / BUTTON_LAYOUT_COLUMNS_COUNT
            val spaceBetweenButtons = calculatorState.calculateButtonPadding(buttonWidth)

            for ((columns, lightColor, button) in calculatorState.buttonsCharacteristic) {
                SiliconeButton(
                    modifier = Modifier
                        .span(
                            columns = columns,
                            rows = 1,
                        )
                        .padding(spaceBetweenButtons),
                    startColor = lightColor,
                    onClick = {
                        if (button == CalculatorButton.Equals) {
                            // TODO: 12/7/2021 Start move animation
                        }
                        onButtonClick(button)
                    }
                ) {
                    Text(
                        text = button.sign,
                        color = contentColorFor(backgroundColor = lightColor),
                        style = TextStyle(
                            fontSize = with(LocalDensity.current) { (maxHeight * 0.33f).toSp() },
                            fontWeight = FontWeight.Light,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            )
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

val constraintSet = ConstraintSet {
    val themeChanger = createRefFor("theme_changer")
    val calculation = createRefFor("calculation")
    val buttonsLayout = createRefFor("buttons_layout")

    val topGuideline40 = createGuidelineFromTop(0.4f)
    val topGuideline38 = createGuidelineFromTop(0.38f)
    val topGuideline10 = createGuidelineFromTop(0.09f)
    val topGuideline2 = createGuidelineFromTop(0.01f)
    val bottomGuideline1 = createGuidelineFromBottom(0.03f)

    constrain(themeChanger) {
        top.linkTo(topGuideline2)
        start.linkTo(buttonsLayout.start, margin = 10.dp)
        bottom.linkTo(topGuideline10)

        height = Dimension.preferredWrapContent
    }
    constrain(calculation) {
        bottom.linkTo(topGuideline38)
        end.linkTo(parent.end)

        width = Dimension.wrapContent
        height = Dimension.fillToConstraints
    }
    constrain(buttonsLayout) {
        linkTo(
            start = parent.start,
            end = parent.end,
        )
        bottom.linkTo(bottomGuideline1)
        top.linkTo(topGuideline40)

        height = Dimension.preferredWrapContent
        width = Dimension.preferredWrapContent
    }
}

private const val BUTTON_LAYOUT_COLUMNS_COUNT = 4

@Stable
class CalculatorState(
    val mathExpressionScrollState: ScrollState,
    val evaluationResultScrollState: ScrollState,
    private val colors: Colors,
) {
    val buttonsRowList = buttonList.chunked(BUTTON_LAYOUT_COLUMNS_COUNT)

    val buttonsCharacteristic
        get() = buttonList.map {
            Triple(buttonWidthRatio(it), buttonLightColor(it), it)
        }

    val buttonsLayoutAspectRation = BUTTON_LAYOUT_COLUMNS_COUNT.toFloat() / buttonsRowList.size

    fun calculateButtonPadding(buttonWidth: Dp): Dp {
        return buttonWidth * 0.04f
    }

    fun buttonWidthRatio(button: CalculatorButton): Int {
        return if (button.sign == "0") 2 else 1
    }

    fun buttonLightColor(button: CalculatorButton): Color {
        return when (button) {
            in buttonsRowList.map(List<CalculatorButton>::last) -> colors.secondary
            in buttonsRowList.first() -> colors.primaryVariant
            else -> colors.primary
        }
    }
}

@Composable
fun rememberCalculatorState(
    colors: Colors = MaterialTheme.colors,
    mathExpressionScrollState: ScrollState = rememberScrollState(),
    evaluationResultScrollState: ScrollState = rememberScrollState(),
) = remember(colors, mathExpressionScrollState, evaluationResultScrollState) {
    CalculatorState(mathExpressionScrollState, evaluationResultScrollState, colors)
}

@ExperimentalMaterialApi
@Preview(
    name = "Light theme",
    showBackground = true,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
fun CalculatorScreenPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            CalculatorScreen(
                uiState = CalculatorUiState("4,900 + 15,910", "20,810"),
                isDarkTheme = false,
                onThemeToggle = { },
                onButtonClick = { },
            )
        }
    }
}
