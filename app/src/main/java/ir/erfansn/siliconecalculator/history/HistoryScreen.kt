package ir.erfansn.siliconecalculator.history

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ir.erfansn.siliconecalculator.ui.component.FlatIconButton
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme
import java.util.*

@Composable
fun HistoryScreen(
    historyRecords: List<HistoryEntity> = historyItems,
    onBackPress: () -> Unit,
    onClearHistory: () -> Unit,
) {
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .safeGesturesPadding(),
    ) {
        HistoryTopBar(
            onBackPress = onBackPress,
            onClearHistory = onClearHistory
        )
        HistoryList(
            recordsList = historyRecords
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryTopBar(
    onBackPress: () -> Unit,
    onClearHistory: () -> Unit,
) {
    Row(
        modifier = Modifier.layoutId("top_bar"),
        horizontalArrangement = Arrangement.spacedBy(10.dp,
            alignment = Alignment.Start)
    ) {
        FlatIconButton(
            modifier = Modifier
                .aspectRatio(1.25f),
            onClick = onBackPress,
            icon = Icons.Outlined.ArrowBack,
            contentDescription = "Back to calculator"
        )

        FlatIconButton(
            modifier = Modifier
                .aspectRatio(1.25f),
            onClick = onClearHistory,
            icon = Icons.Outlined.ClearAll,
            contentDescription = "Clear all records"
        )
    }
}

@Composable
fun HistoryList(
    recordsList: List<HistoryEntity>
) {
    val recordsListByDate = remember(recordsList) {
        recordsList.groupBy { it.date }
    }

    LazyColumn(
        state = rememberLazyListState(
            initialFirstVisibleItemIndex = recordsListByDate.size + historyItems.size
        ),
        modifier = Modifier.layoutId("history_list"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        recordsListByDate.forEach { (date, list) ->
            items(list, key = { it.id }) { item ->
                HistoryItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    expression = item.expression,
                    result = item.result,
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = date,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (date != "Today") {
                    Divider()
                }
            }
        }
    }
}

val constraintSet = ConstraintSet {
    val topBarRef = createRefFor("top_bar")
    val historyList = createRefFor("history_list")

    val topGuideline1 = createGuidelineFromTop(0.01f)
    val topGuideline9 = createGuidelineFromTop(0.09f)
    val topGuideline10 = createGuidelineFromTop(0.1f)

    constrain(topBarRef) {
        linkTo(
            top = topGuideline1,
            start = parent.start,
            bottom = topGuideline9,
            end = parent.end,
            startMargin = 20.dp
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
    constrain(historyList) {
        top.linkTo(topGuideline10)
        bottom.linkTo(parent.bottom)
        linkTo(
            start = parent.start,
            end = parent.end
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
}

val historyItems = listOf(
    HistoryEntity(date = "12 April", expression = "1 + 788 * 875", result = "10"),
    HistoryEntity(date = "12 April", expression = "68774 + 9888 * 4763 / 9847", result = "2675.09"),
    HistoryEntity(date = "15 March", expression = "458867 / 76", result = "0.002"),
    HistoryEntity(date = "15 April", expression = "9475 * 0.88888", result = "4755.2"),
    HistoryEntity(date = "19 April", expression = "47362 / 1 / 98585", result = "12345"),
    HistoryEntity(date = "19 April", expression = "5452 - 97584 + 9573 / 848 * 764", result = "14795"),
    HistoryEntity(date = "19 April", expression = "12 - 957 + 857 - 9588 / 4388 * 8746", result = "25874333"),
    HistoryEntity(date = "Yesterday", expression = "23857 - 979400 + 9488 / 8858", result = "234555"),
    HistoryEntity(date = "Yesterday", expression = "1 * 2 * 3 * 6", result = "56776"),
    HistoryEntity(date = "Yesterday", expression = "999 * 4678", result = "2"),
    HistoryEntity(date = "Today", expression = "1 + 1", result = "2"),
)

data class HistoryEntity(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val expression: String = "",
    val result: String = "0",
)

@Preview(
    name = "Light theme",
    showBackground = true,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun HistoryScreenPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            HistoryScreen(
                onClearHistory = { },
                onBackPress = { }
            )
        }
    }
}
