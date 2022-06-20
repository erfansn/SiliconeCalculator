package ir.erfansn.siliconecalculator.history

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryRecord
import ir.erfansn.siliconecalculator.ui.component.FlatIconButton
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme

@Composable
fun HistoryScreen(
    historyRecords: List<HistoryRecord> = fakeHistoryRecords,
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
    onRecordSelect: (Computation) -> Unit = { }
) {
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .safeGesturesPadding(),
    ) {
        HistoryTopBar(
            onBackPress = onBackPress,
            onHistoryClear = onHistoryClear
        )
        HistoryList(
            recordsList = historyRecords,
            onRecordSelect = onRecordSelect
        )
    }
}

@Composable
fun HistoryTopBar(
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
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
            onClick = onHistoryClear,
            icon = Icons.Outlined.ClearAll,
            contentDescription = "Clear all records"
        )
    }
}

@Composable
fun HistoryList(
    recordsList: List<HistoryRecord>,
    onRecordSelect: (Computation) -> Unit
) {
    val recordsListByDate = remember(recordsList) {
        recordsList.groupBy { it.date }
    }

    Box(
        modifier = Modifier.layoutId("history_records"),
        contentAlignment = Alignment.Center
    ) {
        if (recordsListByDate.isEmpty()) {
            Text(text = "There is no record!")
        } else {
            LazyColumn(
                state = rememberLazyListState(
                    initialFirstVisibleItemIndex = recordsListByDate.size + recordsList.size
                ),
            ) {
                recordsListByDate.forEach { (date, list) ->
                    items(list, key = { it.id }) { item ->
                        HistoryItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = { onRecordSelect(item) })
                                .padding(vertical = 8.dp),
                            expression = item.expression,
                            result = item.result,
                        )
                    }
                    item(date) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = date,
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (date != "Today") {
                            Divider(
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

val constraintSet = ConstraintSet {
    val topBarRef = createRefFor("top_bar")
    val historyList = createRefFor("history_records")

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

val fakeHistoryRecords = listOf(
    HistoryRecord(date = "12 April", expression = "1 + 788 * 875", result = "10"),
    HistoryRecord(date = "12 April", expression = "68774 + 9888 * 4763 / 9847", result = "2675.09"),
    HistoryRecord(date = "15 March", expression = "458867 / 76", result = "0.002"),
    HistoryRecord(date = "15 April", expression = "9475 * 0.88888", result = "4755.2"),
    HistoryRecord(date = "19 April", expression = "47362 / 1 / 98585", result = "12345"),
    HistoryRecord(date = "19 April", expression = "5452 - 97584 + 9573 / 848 * 764", result = "14795"),
    HistoryRecord(date = "19 April", expression = "12 - 957 + 857 - 9588 / 4388 * 8746", result = "25874333"),
    HistoryRecord(date = "Yesterday", expression = "23857 - 979400 + 9488 / 8858", result = "234555"),
    HistoryRecord(date = "Yesterday", expression = "1 * 2 * 3 * 6", result = "56776"),
    HistoryRecord(date = "Yesterday", expression = "999 * 4678", result = "2"),
    HistoryRecord(date = "Today", expression = "1 + 1", result = "2"),
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
                onBackPress = { },
                onHistoryClear = { }
            )
        }
    }
}
