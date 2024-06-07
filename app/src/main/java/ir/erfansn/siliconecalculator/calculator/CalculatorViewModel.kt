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

package ir.erfansn.siliconecalculator.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.erfansn.siliconecalculator.calculator.button.CalculatorButton
import ir.erfansn.siliconecalculator.calculator.button.calculatorButtonsInOrderAllClear
import ir.erfansn.siliconecalculator.calculator.button.calculatorButtonsInOrderClear
import ir.erfansn.siliconecalculator.calculator.button.common.AllClear
import ir.erfansn.siliconecalculator.calculator.button.common.Decimal
import ir.erfansn.siliconecalculator.calculator.button.common.Digit
import ir.erfansn.siliconecalculator.calculator.button.function.Equals
import ir.erfansn.siliconecalculator.calculator.button.function.NumSign
import ir.erfansn.siliconecalculator.calculator.button.function.Percent
import ir.erfansn.siliconecalculator.data.model.Calculation
import ir.erfansn.siliconecalculator.data.repository.HistoryRepository
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.EXPRESSION_ARG
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorDestinationsArg.RESULT_ARG
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val historyRepository: HistoryRepository,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _calculation = MutableStateFlow(Calculation())
    private val currentCalculation get() = _calculation.value

    private var previousExpression = currentCalculation.expression

    private val _calculatorButtons = MutableStateFlow(listOf<CalculatorButton>())
    val calculatorButtons = _calculatorButtons.asStateFlow()

    val uiState = _calculation
        .map(::CalculatorUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CalculatorUiState()
        )

    init {
        updateCalculatorDisplay(
            expression = savedStateHandle[EXPRESSION_ARG],
            result = savedStateHandle[RESULT_ARG]
        )

        _calculation
            .onEach { calculation ->
                _calculatorButtons.update {
                    if (!calculation.isNotEvaluated || calculation.resultIsInvalid) {
                        calculatorButtonsInOrderAllClear
                    } else {
                        calculatorButtonsInOrderClear
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateCalculatorDisplay(expression: String?, result: String?) {
        if (expression == null || result == null) return

        _calculation.update {
            it.copy(expression = expression, result = result)
        }

        previousExpression = expression
    }

    fun performCalculatorButton(calculatorButton: CalculatorButton): Boolean {
        if (currentCalculation.resultIsInvalid && calculatorButton != AllClear) return false
        if (!currentCalculation.isNotEvaluated && (calculatorButton is Digit || calculatorButton in listOf(Decimal, NumSign, Percent))) return false

        viewModelScope.launch(defaultDispatcher) {
            _calculation.update {
                calculatorButton.perform(it).also { result ->
                    if (calculatorButton == Equals) saveCalculationInHistory(result)
                }
            }
        }
        return true
    }

    private suspend fun saveCalculationInHistory(calculation: Calculation) {
        if (calculation.expression == previousExpression || calculation.isNotEvaluated || calculation.resultIsInvalid) return

        historyRepository.saveCalculation(calculation)

        previousExpression = calculation.expression
    }

    private val Calculation.isNotEvaluated
        get() = expression.endsWith(lastOperator) || expression.isEmpty()
}
