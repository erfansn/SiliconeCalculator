@file:OptIn(ExperimentalMaterialApi::class)

package ir.erfansn.siliconecalculator.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FlatIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50)
        .copy(bottomStart = CornerSize(percent = 0)),
    icon: ImageVector,
    contentDescription: String,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight
            ),
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

@Preview(showBackground = true)
@Composable
fun FlatIconButtonPreview() {
    MaterialTheme {
        FlatIconButton(
            onClick = { },
            icon = Icons.Default.Abc,
            contentDescription = ""
        )
    }
}
