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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.erfansn.siliconecalculator.ui.theme.BlueGrey100

@Composable
fun CorneredFlatButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.7f).compositeOver(BlueGrey100),
    border: BorderStroke? = null,
    shape: Shape = MaterialTheme.shapes.small.copy(bottomStart = CornerSize(percent = 0)),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) = Button(
    modifier = modifier,
    shape = shape,
    elevation = null,
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor
    ),
    contentPadding = contentPadding,
    border = border,
    content = content
)

@Composable
fun OutlinedCorneredFlatButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small.copy(bottomStart = CornerSize(percent = 0)),
    onClick: () -> Unit,
    border: BorderStroke = ButtonDefaults.outlinedBorder,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) = CorneredFlatButton(
    modifier = modifier,
    border = border,
    backgroundColor = Color.Transparent,
    shape = shape,
    contentPadding = contentPadding,
    onClick = onClick,
    content = content
)

@Composable
fun CorneredFlatIconButton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small.copy(bottomStart = CornerSize(percent = 0)),
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
) {
    CorneredFlatButton(
        modifier = modifier,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(0.60f),
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
fun CorneredFlatButtonPreview() {
    MaterialTheme {
        CorneredFlatButton(
            onClick = { }
        ) {
            Text("Flat button")
        }
    }
}

@Preview
@Composable
fun OutlinedCorneredFlatButtonPreview() {
    MaterialTheme {
        OutlinedCorneredFlatButton(
            onClick = { }
        ) {
            Text("Flat button")
        }
    }
}

@Preview
@Composable
fun CorneredFlatIconButtonPreview() {
    MaterialTheme {
        CorneredFlatIconButton(
            onClick = { },
            icon = Icons.Default.Adb,
            contentDescription = "Adb",
        )
    }
}
