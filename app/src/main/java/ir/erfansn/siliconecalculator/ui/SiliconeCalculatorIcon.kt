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

package ir.erfansn.siliconecalculator.ui

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.boundingRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.erfansn.siliconecalculator.ui.theme.BlueGrey300
import ir.erfansn.siliconecalculator.ui.theme.DeepOrange800

@Preview(
    backgroundColor = 0xFFECECEC,
    showBackground = true,
    widthDp = 108,
    heightDp = 108
)
@Composable
fun SiliconeCalculatorIconPreview() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val baseModifier = Modifier
            .width(58.dp)
            .height(16.dp)

        NeuShape(
            modifier = baseModifier,
            lightColor = BlueGrey300,
            shape = CircleShape,
            borderWidthPercent = 25,
            elevation = 24.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        NeuShape(
            modifier = baseModifier,
            lightColor = DeepOrange800,
            shape = CircleShape,
            borderWidthPercent = 25,
            elevation = 24.dp
        )
    }
}

@Composable
private fun NeuShape(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(36),
    lightColor: Color,
    darkColor: Color = lightColor.copy(
        red = (lightColor.red + 0.125f).coerceAtMost(1.0f),
        green = (lightColor.green + 0.125f).coerceAtMost(1.0f),
        blue = (lightColor.blue + 0.125f).coerceAtMost(1.0f),
    ),
    borderWidthPercent: Int = 12,
    elevation: Dp = 18.dp,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        elevation = elevation
    ) {
        Canvas(
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
        ) {
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

            drawIntoCanvas {
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
    }
}