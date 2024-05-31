/*
 * Copyright 2022 Erfan Sn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.erfansn.siliconecalculator.ui.component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ElevationOverlay
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.boundingRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NeuButton(
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
    onLongClick: () -> Unit = { },
    content: @Composable BoxWithConstraintsScope.() -> Unit,
) {
    check(borderWidthPercent in 0..100) { "The border width percent should be in the range of [0, 100]" }

    val colors = ButtonDefaults.buttonColors()
    val contentColor by colors.contentColor(true)

    Surface(
        onLongClick = onLongClick,
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = colors.backgroundColor(true).value,
        contentColor = contentColor.copy(alpha = 1f),
        elevation = elevation.elevation(
            enabled = true,
            interactionSource = interactionSource
        ).value,
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
                            val paint = Paint()
                                .asFrameworkPaint()
                                .apply {
                                    isAntiAlias = true
                                    style = android.graphics.Paint.Style.STROKE
                                    strokeWidth = size.minDimension * (borderWidthPercent / 100.0f)
                                    shader = LinearGradientShader(
                                        from = Offset.Zero,
                                        to = Offset(x = size.width, y = size.height),
                                        colors = listOf(darkColor, lightColor)
                                    )
                                    maskFilter =
                                        BlurMaskFilter(strokeWidth / 2,
                                            BlurMaskFilter.Blur.NORMAL)
                                }

                            onDrawBehind {
                                drawIntoCanvas {
                                    when (val outline =
                                        shape.createOutline(size, layoutDirection, this)) {
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
                                                roundRect.boundingRect.toAndroidRectF(),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Surface(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    val absoluteElevation = LocalAbsoluteElevation.current + elevation
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalAbsoluteElevation provides absoluteElevation
    ) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = surfaceColorAtElevation(
                        color = color,
                        elevationOverlay = LocalElevationOverlay.current,
                        absoluteElevation = absoluteElevation
                    ),
                    border = border,
                    elevation = elevation
                )
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = enabled,
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            propagateMinConstraints = true
        ) {
            content()
        }
    }
}

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    elevation: Dp
) = this.shadow(elevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

@Composable
private fun surfaceColorAtElevation(
    color: Color,
    elevationOverlay: ElevationOverlay?,
    absoluteElevation: Dp
): Color {
    return if (color == MaterialTheme.colors.surface && elevationOverlay != null) {
        elevationOverlay.apply(color, absoluteElevation)
    } else {
        color
    }
}

@Preview(showBackground = true)
@Composable
fun NeuButtonPreview() {
    MaterialTheme {
        NeuButton(
            modifier = Modifier.padding(10.dp),
            lightColor = MaterialTheme.colors.primary,
            onClick = { },
        ) {
            Text(text = "1")
        }
    }
}
