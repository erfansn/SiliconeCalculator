package ir.erfansn.siliconecalculator.history

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ir.erfansn.siliconecalculator.R
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import ir.erfansn.siliconecalculator.data.model.previewHistoryItems
import ir.erfansn.siliconecalculator.ui.component.FlatIconButton
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme
import ir.erfansn.siliconecalculator.util.formatNumbers

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
    onComputationSelect: (Computation) -> Unit,
) {
    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
    ) {
        HistoryTopBar(
            onBackPress = onBackPress,
            onHistoryClear = onHistoryClear
        )
        HistoryList(
            historyItems = uiState.historyItems,
            onComputationSelect = onComputationSelect
        )
    }
}

@Composable
fun HistoryTopBar(
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
) {
    Row(
        modifier = Modifier
            .layoutId("top_bar"),
        horizontalArrangement = Arrangement.spacedBy(10.dp,
            alignment = Alignment.Start)
    ) {
        FlatIconButton(
            modifier = Modifier
                .aspectRatio(1.25f),
            onClick = onBackPress,
            icon = Icons.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back_to_calculator)
        )

        FlatIconButton(
            modifier = Modifier
                .aspectRatio(1.25f),
            onClick = onHistoryClear,
            icon = Icons.Outlined.ClearAll,
            contentDescription = stringResource(R.string.clear_history)
        )
    }
}

@Composable
fun HistoryList(
    historyItems: List<HistoryItem>,
    onComputationSelect: (Computation) -> Unit,
) {
    val historyItemsByDate = remember(historyItems) {
        historyItems.groupBy(
            keySelector = HistoryItem::date,
            valueTransform = HistoryItem::computation
        )
    }

    Box(
        modifier = Modifier.layoutId("history_items"),
    ) {
        if (historyItemsByDate.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.nothing_to_show)
            )
        } else {
            val historyItemsList = stringResource(R.string.history_items_list)
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .semantics { contentDescription = historyItemsList },
                state = rememberLazyListState(
                    initialFirstVisibleItemIndex = historyItems.size + historyItemsByDate.size
                ),
            ) {
                historyItemsByDate.onEachIndexed { index, (date, computations) ->
                    historyItem(
                        computations = computations,
                        onComputationSelect = onComputationSelect,
                        date = date,
                        isLastItem = historyItemsByDate.size - 1 == index
                    )
                }
            }
        }
    }
}

private fun LazyListScope.historyItem(
    computations: List<Computation>,
    onComputationSelect: (Computation) -> Unit,
    date: String,
    isLastItem: Boolean,
) {
    items(computations) { computation ->
        ComputationItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onComputationSelect(computation) }
                .padding(vertical = 8.dp),
            computation = computation,
        )
    }
    item {
        Text(
            modifier = Modifier.padding(16.dp),
            text = date,
            style = MaterialTheme.typography.h6
        )
        if (!isLastItem) {
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun ComputationItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h4,
    computation: Computation,
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
            text = computation.expression.formatNumbers(),
            style = textStyle.copy(
                fontWeight = FontWeight.Light
            )
        )
        Text(
            modifier = Modifier
                .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                .padding(horizontal = 16.dp),
            text = computation.result.formatNumbers(),
            style = textStyle,
        )
    }
}

val constraintSet = ConstraintSet {
    val topBarRef = createRefFor("top_bar")
    val historyItems = createRefFor("history_items")

    val topGuideline1 = createGuidelineFromTop(0.01f)
    val topGuideline9 = createGuidelineFromTop(0.09f)
    val topGuideline10 = createGuidelineFromTop(0.11f)

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
    constrain(historyItems) {
        linkTo(
            top = topGuideline10,
            start = parent.start,
            bottom = parent.bottom,
            end = parent.end,
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
}

@Preview(
    name = "Light theme with items",
    showBackground = true,
)
@Preview(
    name = "Dark theme with items",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun HistoryScreenWithItemsPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            HistoryScreen(
                uiState = HistoryUiState(previewHistoryItems),
                onBackPress = { },
                onHistoryClear = { },
                onComputationSelect = { }
            )
        }
    }
}

@Preview(
    name = "Light theme empty",
    showBackground = true,
)
@Preview(
    name = "Dark theme empty",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun HistoryScreenEmptyPreview() {
    SiliconeCalculatorTheme {
        Surface(color = MaterialTheme.colors.background) {
            HistoryScreen(
                uiState = HistoryUiState(),
                onBackPress = { },
                onHistoryClear = { },
                onComputationSelect = { }
            )
        }
    }
}