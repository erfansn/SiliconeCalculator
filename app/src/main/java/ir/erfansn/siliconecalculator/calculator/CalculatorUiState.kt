package ir.erfansn.siliconecalculator.calculator

import androidx.compose.runtime.Immutable
import ir.erfansn.siliconecalculator.data.model.Calculation

@Immutable
data class CalculatorUiState(
    val calculation: Calculation = Calculation()
)
