@file:OptIn(ExperimentalMaterialApi::class)

package ir.erfansn.siliconecalculator.calculator

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SiliconeButton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(36),
    lightColor: Color,
    darkColor: Color = lightColor.copy(
        red = (lightColor.red + 0.125f).coerceAtMost(1.0f),
        green = (lightColor.green + 0.125f).coerceAtMost(1.0f),
        blue = (lightColor.blue + 0.125f).coerceAtMost(1.0f),
    ),
    borderWidthPercent: Int = 12,
    elevation: ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = 18.dp,
        pressedElevation = 8.dp
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable BoxWithConstraintsScope.() -> Unit
) {
    check(borderWidthPercent in 0..100) { "The border width percent should be in the range of [0, 100]" }

    val colors = ButtonDefaults.buttonColors()
    val contentColor by colors.contentColor(true)

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
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
                        .background(
                            brush = Brush.linearGradient(
                                0.0f to lightColor,
                                1.0f to darkColor,
                            )
                        )
                        .drawWithCache {
                            onDrawBehind {
                                drawIntoCanvas {
                                    val paint = Paint()
                                        .asFrameworkPaint()
                                        .apply {
                                            isAntiAlias = true
                                            style = android.graphics.Paint.Style.STROKE
                                            strokeWidth = size.minDimension * (borderWidthPercent.coerceIn(0..100) / 100.0f)
                                            shader = LinearGradientShader(
                                                from = Offset.Zero,
                                                to = Offset(x = size.width, y = size.height),
                                                colors = listOf(darkColor, lightColor)
                                            )
                                            maskFilter =
                                                BlurMaskFilter(strokeWidth / 2,
                                                    BlurMaskFilter.Blur.NORMAL)
                                        }

                                    when (val outline = shape.createOutline(size, layoutDirection, this)) {
                                        is Outline.Rectangle -> {
                                            val rect = outline.rect
                                            it.nativeCanvas.drawRect(
                                                rect.toAndroidRect(),
                                                paint
                                            )
                                        }
                                        is Outline.Rounded -> {
                                            val roundRect = outline.roundRect
                                            it.nativeCanvas.drawRoundRect(
                                                roundRect.top,
                                                roundRect.left,
                                                roundRect.right,
                                                roundRect.bottom,
                                                roundRect.topLeftCornerRadius.x,
                                                roundRect.topLeftCornerRadius.y,
                                                paint
                                            )
                                        }
                                        is Outline.Generic -> {
                                            val path = outline.path
                                            it.nativeCanvas.drawPath(
                                                path.asAndroidPath(),
                                                paint
                                            )
                                        }
                                    }
                                }
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
fun SiliconeButtonPreview() {
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
