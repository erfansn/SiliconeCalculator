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

@file:OptIn(ExperimentalMaterialApi::class)

package ir.erfansn.siliconecalculator.history

import android.content.res.Configuration
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import ir.erfansn.siliconecalculator.R
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.model.History
import ir.erfansn.siliconecalculator.data.model.previewHistoryItems
import ir.erfansn.siliconecalculator.ui.component.CorneredFlatButton
import ir.erfansn.siliconecalculator.ui.component.CorneredFlatIconButton
import ir.erfansn.siliconecalculator.ui.component.OutlinedCorneredFlatButton
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
    onCalculationClick: (Calculation) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val clearHistoryBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    PredictiveBackHandler(enabled = clearHistoryBottomSheetState.isVisible) {
        try {
            it.collect()
            coroutineScope.launch { clearHistoryBottomSheetState.hide() }
        } catch (e: CancellationException) {
            // Nothing to do
        }
    }
    ModalBottomSheetLayout(
        modifier = Modifier.background(color = MaterialTheme.colors.surface.copy(alpha = 0.4f)),
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
            HistoryItemsList(
                historyItems = uiState.historyItems,
                onCalculationClick = onCalculationClick
            )
        }
    }
}

@Composable
fun ColumnScope.ClearHistoryBottomSheetContent(
    onCancelClick: () -> Unit,
    onClearClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(28.dp))

    Column(
        modifier = Modifier.align(CenterHorizontally),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.clear),
            style = MaterialTheme.typography.h6,
        )
        Text(
            text = stringResource(R.string.clear_history_now),
            style = MaterialTheme.typography.body1
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .wrapContentWidth()
            .height(48.dp)
            .align(CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedCorneredFlatButton(
            modifier = Modifier.aspectRatio(2.5f),
            onClick = onCancelClick
        ) {
            Text(text = stringResource(R.string.cancel))
        }

        CorneredFlatButton(
            modifier = Modifier
                .aspectRatio(2.5f)
                .testTag("history:clear"),
            onClick = onClearClick
        ) {
            Text(text = stringResource(R.string.clear))
        }
    }

    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
}

@Composable
fun HistoryTopBar(
    onBackPress: () -> Unit,
    onHistoryClear: () -> Unit,
) {
    Box(
        modifier = Modifier.layoutId("top_bar"),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.Start
            )
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
    Spacer(modifier = Modifier.layoutId("spacer"))
}

@Composable
fun HistoryItemsList(
    historyItems: List<History>,
    onCalculationClick: (Calculation) -> Unit,
) {
    val historyItemsByDate = remember(historyItems) {
        historyItems.groupBy(
            keySelector = History::date,
            valueTransform = History::calculation
        )
    }

    Box(
        modifier = Modifier.layoutId("history_list")
            .background(color = MaterialTheme.colors.background),
    ) {
        if (historyItemsByDate.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.nothing_to_show)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .testTag("history:items"),
                state = rememberLazyListState(
                    initialFirstVisibleItemIndex = historyItemsByDate.size * 2
                ),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                historyItemsByDate.onEachIndexed { index, (date, calculations) ->
                    item {
                        HistoryItem(
                            calculations = calculations,
                            onCalculationClick = onCalculationClick,
                            date = date
                        )
                    }
                    item {
                        val isLastItem = remember { index == historyItemsByDate.size - 1 }
                        if (!isLastItem) {
                            Divider(
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                )
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
    val spacerRef = createRefFor("spacer")
    val historyList = createRefFor("history_list")

    val topGuideline10 = createGuidelineFromTop(0.10f)
    val topGuideline11 = createGuidelineFromTop(0.11f)

    constrain(topBarRef) {
        linkTo(
            top = parent.top,
            start = parent.start,
            bottom = topGuideline10,
            end = parent.end,
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
    constrain(spacerRef) {
        linkTo(
            top = topBarRef.bottom,
            start = parent.start,
            bottom = topGuideline11,
            end = parent.end,
        )

        width = Dimension.fillToConstraints
        height = Dimension.fillToConstraints
    }
    constrain(historyList) {
        linkTo(
            top = spacerRef.bottom,
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
            val calculations = previewHistoryItems.toMutableList()

            HistoryScreen(
                uiState = HistoryUiState(calculations),
                onBackPress = { },
                onHistoryClear = { calculations.clear() },
                onCalculationClick = { }
            )
        }
    }
}
