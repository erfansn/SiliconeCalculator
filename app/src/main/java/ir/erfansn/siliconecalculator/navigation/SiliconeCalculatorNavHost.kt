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

package ir.erfansn.siliconecalculator.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.erfansn.siliconecalculator.calculator.CalculatorScreen
import ir.erfansn.siliconecalculator.calculator.CalculatorViewModel
import ir.erfansn.siliconecalculator.history.HistoryScreen
import ir.erfansn.siliconecalculator.history.HistoryViewModel
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinations.CALCULATOR_ROUTE
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinations.HISTORY_ROUTE

@Composable
fun SiliconeCalculatorNavHost(
    navController: NavHostController = rememberNavController(),
    navActions: AppNavigationActions = remember(navController) {
        AppNavigationActions(navController)
    },
    onThemeToggle: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = CALCULATOR_ROUTE,
        popEnterTransition = {
            fadeIn(animationSpec = tween(durationMillis = 700))
        },
        popExitTransition = {
            fadeOut(
                animationSpec = tween(durationMillis = 700)
            ) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis = 700)
            )
        },
    ) {
        composable(
            CALCULATOR_ROUTE,
            arguments = listOf(
                navArgument(SiliconeCalculatorDestinationsArg.EXPRESSION_ARG) { defaultValue = "" },
                navArgument(SiliconeCalculatorDestinationsArg.RESULT_ARG) { defaultValue = "0" },
            )
        ) { backStackEntry ->
            val calculatorViewModel = hiltViewModel<CalculatorViewModel>()
            val uiState by calculatorViewModel.uiState.collectAsState()

            CalculatorScreen(
                uiState = uiState,
                onCalculatorButtonClick = calculatorViewModel::performCalculatorButton,
                // Until fixes this [https://issuetracker.google.com/issues/342965874]
                // onHistoryNav = dropUnlessResumed(block = navActions::navigateToHistory),
                onHistoryNav = {
                    if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        navActions.navigateToHistory()
                    }
                },
                onThemeToggle = onThemeToggle
            )
        }
        composable(HISTORY_ROUTE) { backStackEntry ->
            val historyViewModel = hiltViewModel<HistoryViewModel>()
            val uiState by historyViewModel.uiState.collectAsState()

            HistoryScreen(
                uiState = uiState,
                onHistoryClear = historyViewModel::onHistoryClear,
                onBackPress = {
                    if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        navActions.onBackPress()
                    }
                },
                onCalculationClick = {
                    if (backStackEntry.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        navActions.navigateToCalculator(it)
                    }
                }
            )
        }
    }
}
