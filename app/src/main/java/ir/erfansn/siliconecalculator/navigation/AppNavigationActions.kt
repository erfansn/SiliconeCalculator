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