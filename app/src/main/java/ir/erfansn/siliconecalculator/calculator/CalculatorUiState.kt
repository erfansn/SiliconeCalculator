package ir.erfansn.siliconecalculator.calculator

import androidx.compose.runtime.Immutable
import ir.erfansn.siliconecalculator.data.model.Computation

@Immutable
data class CalculatorUiState(
    val computation: Computation = Computation()
)
