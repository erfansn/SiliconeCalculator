@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package ir.erfansn.siliconecalculator.history

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ir.erfansn.siliconecalculator.R
import ir.erfansn.siliconecalculator.data.model.Computation
import ir.erfansn.siliconecalculator.data.model.HistoryItem
import ir.erfansn.siliconecalculator.data.model.previewHistoryItems
import ir.erfansn.siliconecalculator.ui.component.CorneredFlatButton
import ir.erfansn.siliconecalculator.ui.component.CorneredFlatIconButton
import ir.erfansn.siliconecalculator.ui.component.OutlinedCorneredFlatButton
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme
import ir.erfansn.siliconecalculator.util.formatNumbers
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
    onComputationSelect: (Computation) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val clearHistoryBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    BackHandler {
        if (clearHistoryBottomSheetState.isVisible) {
            coroutineScope.launch { clearHistoryBottomSheetState.hide() }
        } else {
            onBackPress()
        }
    }
    ModalBottomSheetLayout(
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetState = clearHistoryBottomSheetState,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
        scrimColor = Color.Black.copy(0.32f),
        sheetContent = {
            ClearHistoryBottomSheetContent(
                onCancelClick = {
                    coroutineScope.launch { clearHistoryBottomSheetState.hide() }
                },
                onClearClick = {
                    coroutineScope.launch {
                        onHistoryClear()
                        onBackPress()
                    }
                }
            )
        }
    ) {
        ConstraintLayout(
            constraintSet = constraintSet,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
        ) {
            HistoryTopBar(
                onBackPress = onBackPress,
                onHistoryClear = { coroutineScope.launch { clearHistoryBottomSheetState.show() } }
            )
            HistoryList(
                historyItems = uiState.historyItems,
                onComputationSelect = onComputationSelect
            )
        }
    }
}

@Composable
fun ColumnScope.ClearHistoryBottomSheetContent(
    onCancelClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(28.dp))

    Column(
        modifier = Modifier.align(CenterHorizontally),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Clear",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Clear history now?",
            style = MaterialTheme.typography.body1
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(48.dp)
            .align(CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedCorneredFlatButton(
            modifier = Modifier.aspectRatio(2.5f),
            onClick = onCancelClick
        ) {
            Text(text = "Cancel")
        }

        CorneredFlatButton(
            modifier = Modifier.aspectRatio(2.5f),
            onClick = onClearClick
        ) {
            Text(text = "Clear")
        }
    }

    Spacer(modifier = Modifier.height(
        16.dp + WindowInsets.safeContent.asPaddingValues().calculateBottomPadding()
    ))
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
        val baseModifier = Modifier.aspectRatio(1.25f)
        CorneredFlatIconButton(
            modifier = baseModifier,
            onClick = onBackPress,
            icon = Icons.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back_to_calculator)
        )
        CorneredFlatIconButton(
            modifier = baseModifier,
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
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 28.dp
            ),
            text = date,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Medium,
            )
        )
        if (!isLastItem) {
            Divider(modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ))
        }
    }
}

@Composable
fun ComputationItem(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h5,
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
            val computations = mutableListOf(*previewHistoryItems.toTypedArray())

            HistoryScreen(
                uiState = HistoryUiState(computations),
                onBackPress = { },
                onHistoryClear = { computations.clear() },
                onComputationSelect = { }
            )
        }
    }
}
