package ir.erfansn.siliconecalculator.history

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h4,
    expression: String,
    result: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                .padding(horizontal = 16.dp),
            text = expression,
            style = textStyle.copy(
                fontWeight = FontWeight.Light
            )
        )
        Text(
            modifier = Modifier
                .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                .padding(horizontal = 16.dp),
            text = result,
            style = textStyle,
        )
    }
}

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
fun HistoryItemPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            HistoryItem(
                expression = "25,600 - 500",
                result = "25,100"
            )
        }
    }
}
