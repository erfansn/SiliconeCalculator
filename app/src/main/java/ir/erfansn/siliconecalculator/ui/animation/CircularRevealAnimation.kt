package ir.erfansn.siliconecalculator.ui.animation

import android.graphics.Path
import android.view.MotionEvent
import androidx.annotation.FloatRange
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme
import kotlin.math.hypot

@Composable
fun <T> CircularReveal(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: @Composable (T) -> Unit,
) {
    val transition = updateTransition(targetState, label = "Circular reveal")
    transition.CircularReveal(modifier, animationSpec, content = content)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun <T> Transition<T>.CircularReveal(
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: @Composable (targetState: T) -> Unit,
) {
    var offset: Offset? by remember { mutableStateOf(null) }
    val currentlyVisible = remember { mutableStateListOf<T>().apply { add(currentState) } }
    val contentMap = remember {
        mutableMapOf<T, @Composable () -> Unit>()
    }
    if (currentState == targetState) {
        // If not animating, just display the current state
        if (currentlyVisible.size != 1 || currentlyVisible[0] != targetState) {
            // Remove all the intermediate items from the list once the animation is finished.
            currentlyVisible.removeAll { it != targetState }
            contentMap.clear()
        }
    }
    if (!contentMap.contains(targetState)) {
        // Replace target with the same key if any
        val replacementId = currentlyVisible.indexOfFirst {
            it == targetState
        }
        if (replacementId == -1) {
            currentlyVisible.add(targetState)
        } else {
            currentlyVisible[replacementId] = targetState
        }
        contentMap.clear()
        currentlyVisible.forEach { stateForContent ->
            contentMap[stateForContent] = {
                val progress by animateFloat(
                    label = "Progress",
                    transitionSpec = { animationSpec }
                ) {
                    val targetedContent = stateForContent != currentlyVisible.last() || it == stateForContent
                    if (targetedContent) 1f else 0f
                }
                Box(Modifier.circularReveal(progress = progress, offset = offset)) {
                    content(stateForContent)
                }
            }
        }
    }
    Box(
        modifier = modifier.pointerInteropFilter {
            if (it.action == MotionEvent.ACTION_DOWN) {
                if (!started) offset = Offset(it.x, it.y)
            }
            started
        }
    ) {
        currentlyVisible.forEach {
            key(it) {
                contentMap[it]?.invoke()
            }
        }
    }
}

private val <T> Transition<T>.started get() =
    currentState != targetState || isRunning

private fun Modifier.circularReveal(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    offset: Offset? = null,
) = clip(CircularRevealShape(progress, offset))

private class CircularRevealShape(
    @FloatRange(from = 0.0, to = 1.0) private val progress: Float,
    private val offset: Offset? = null,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Generic(Path().apply {
            addCircle(
                offset?.x ?: (size.width / 2f),
                offset?.y ?: (size.height / 2f),
                longestDistanceToACorner(size, offset) * progress,
                Path.Direction.CW
            )
        }.asComposePath())
    }

    private fun longestDistanceToACorner(size: Size, offset: Offset?): Float {
        if (offset == null) {
            return hypot(size.width / 2f, size.height / 2f)
        }

        val topLeft = hypot(offset.x, offset.y)
        val topRight = hypot(size.width - offset.x, offset.y)
        val bottomLeft = hypot(offset.x, size.height - offset.y)
        val bottomRight = hypot(size.width - offset.x, size.height - offset.y)

        return maxOf(topLeft, topRight, bottomLeft, bottomRight)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CircularRevealAnimationPreview() {
    val isSystemDark = isSystemInDarkTheme()
    var darkTheme by remember { mutableStateOf(isSystemDark) }
    val onThemeToggle = { darkTheme = !darkTheme }

    CircularReveal(
        targetState = darkTheme,
        animationSpec = tween(1500)
    ) { isDark ->
        SiliconeCalculatorTheme(darkTheme = isDark) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
                onClick = onThemeToggle
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(120.dp),
                        imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle",
                    )
                }
            }
        }
    }
}
