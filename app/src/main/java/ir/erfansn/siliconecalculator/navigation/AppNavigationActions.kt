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

import androidx.navigation.NavController
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.EXPRESSION_ARG
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.RESULT_ARG
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorScreens.CALCULATOR
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorScreens.HISTORY
import ir.erfansn.siliconecalculator.util.encodeReservedChars

object SiliconeCalculatorScreens {
    const val CALCULATOR = "calculator"
    const val HISTORY = "history"
}

object SiliconeCalculatorDestinationsArg {
    const val EXPRESSION_ARG = "expression"
    const val RESULT_ARG = "result"
}

object SiliconeCalculatorDestinations {
    const val CALCULATOR_ROUTE =
        "$CALCULATOR?$EXPRESSION_ARG={$EXPRESSION_ARG}&$RESULT_ARG={$RESULT_ARG}"
    const val HISTORY_ROUTE = HISTORY
}

class AppNavigationActions(private val navController: NavController) {

    fun navigateToCalculator(calculation: Calculation) {
        val (expression, result) = calculation

        navController.navigate(
            route = "$CALCULATOR?$EXPRESSION_ARG=${expression.encodeReservedChars}&$RESULT_ARG=${result}"
        ) {
            launchSingleTop = true
        }
    }

    fun navigateToHistory() {
        navController.navigate(HISTORY)
    }

    fun onBackPress() {
        navController.popBackStack()
    }
}