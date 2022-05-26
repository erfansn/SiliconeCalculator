package ir.erfansn.siliconecalculator.ui.component

import android.view.MotionEvent
import androidx.annotation.FloatRange
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot
import android.graphics.Path as NativePath

@Composable
fun <T> CircularReveal(
    targetState: T,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    content: @Composable (T) -> Unit,
) {
    val transition = updateTransition(targetState)
    transition.CircularReveal(modifier, animationSpec, content = content)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> Transition<T>.CircularReveal(
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
                val progress by animateFloat(transitionSpec = { animationSpec }) {
                    if (stateForContent != currentlyVisible.last() || it == stateForContent) 1f else 0f
                }
                Box(Modifier.circularReveal(progress = progress, offset = offset)) {
                    content(stateForContent)
                }
            }
        }
    }

    Box(modifier.pointerInteropFilter {
        offset = when (it.action) {
            MotionEvent.ACTION_DOWN -> Offset(it.x, it.y)
            else -> null
        }
        false
    }) {
        currentlyVisible.forEach {
            key(it) {
                contentMap[it]?.invoke()
            }
        }
    }
}

fun Modifier.circularReveal(@FloatRange(from = 0.0, to = 1.0) progress: Float, offset: Offset? = null) = clip(CircularRevealShape(progress, offset))

class CircularRevealShape(
    @FloatRange(from = 0.0, to = 1.0) private val progress: Float,
    private val offset: Offset? = null,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Generic(NativePath().apply {
            addCircle(
                offset?.x ?: (size.width / 2f),
                offset?.y ?: (size.height / 2f),
                longestDistanceToACorner(size, offset) * progress,
                NativePath.Direction.CW
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

        return topLeft.coerceAtLeast(topRight).coerceAtLeast(bottomLeft).coerceAtLeast(bottomRight)
    }
}
