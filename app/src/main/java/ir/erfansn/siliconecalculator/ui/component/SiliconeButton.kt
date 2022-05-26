@file:OptIn(ExperimentalMaterialApi::class)

package ir.erfansn.siliconecalculator.ui.component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.erfansn.siliconecalculator.ui.theme.BlueGrey800

@Composable
fun SiliconeButton(
    modifier: Modifier = Modifier,
    lightColor: Color,
    darkColor: Color = lightColor.copy(
        red = (lightColor.red + 0.125f).coerceAtMost(1.0f),
        green = (lightColor.green + 0.125f).coerceAtMost(1.0f),
        blue = (lightColor.blue + 0.125f).coerceAtMost(1.0f),
    ),
    cornerRadiusPercent: Int = 36,
    elevation: ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = 18.dp,
        pressedElevation = 8.dp
    ),
    onClick: () -> Unit,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    val colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    val contentColor by colors.contentColor(true)

    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadiusPercent),
        color = colors.backgroundColor(true).value,
        contentColor = contentColor.copy(alpha = 1f),
        elevation = elevation.elevation(enabled = true, interactionSource = interactionSource).value,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinWidth
                        )
                        .drawBehind {
                            drawRect(
                                brush = Brush.linearGradient(
                                    0.0f to lightColor,
                                    1.0f to darkColor,
                                ),
                            )

                            val size = size
                            drawIntoCanvas {
                                val paint = Paint()
                                    .asFrameworkPaint()
                                    .apply {
                                        isAntiAlias = true
                                        style = android.graphics.Paint.Style.STROKE
                                        strokeWidth = size.minDimension * 0.08f
                                        shader = LinearGradientShader(
                                            from = Offset.Zero,
                                            to = Offset(x = size.width, y = size.height),
                                            colors = listOf(
                                                darkColor,
                                                lightColor,
                                            )
                                        )
                                        maskFilter =
                                            BlurMaskFilter(strokeWidth / 2,
                                                BlurMaskFilter.Blur.NORMAL)
                                    }

                                val topLeftOffset = size.minDimension * 0.01f
                                it.nativeCanvas.drawRoundRect(
                                    topLeftOffset,
                                    topLeftOffset,
                                    size.width - topLeftOffset,
                                    size.height - topLeftOffset,
                                    size.minDimension * cornerRadiusPercent / 100f,
                                    size.minDimension * cornerRadiusPercent / 100f,
                                    paint
                                )
                            }
                        },
                    contentAlignment = Alignment.Center,
                    content = content
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NeuButtonPreview() {
    MaterialTheme {
        SiliconeButton(
            modifier = Modifier.padding(10.dp),
            lightColor = MaterialTheme.colors.primary,
            onClick = { },
        ) {
            Text(text = "1")
        }
    }
}
