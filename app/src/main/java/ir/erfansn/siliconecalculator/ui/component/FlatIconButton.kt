package ir.erfansn.siliconecalculator.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

@ExperimentalMaterialApi
@Composable
fun FlatIconButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium
        .copy(all = CornerSize(percent = 50))
        .copy(bottomStart = CornerSize(percent = 0)),
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize(0.60f),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}
