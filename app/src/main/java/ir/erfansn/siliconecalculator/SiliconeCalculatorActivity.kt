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

package ir.erfansn.siliconecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.erfansn.siliconecalculator.navigation.SiliconeCalculatorNavHost
import ir.erfansn.siliconecalculator.ui.animation.CircularReveal
import ir.erfansn.siliconecalculator.ui.theme.SiliconeCalculatorTheme

@ExperimentalComposeUiApi
@AndroidEntryPoint
class SiliconeCalculatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isSystemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(isSystemDark) }
            val onThemeToggle = { darkTheme = !darkTheme }

            val navController = rememberNavController()
            CircularReveal(
                targetState = darkTheme,
                animationSpec = tween(500)
            ) { isDark ->
                SiliconeCalculatorTheme(darkTheme = isDark) {
                    Surface(
                        modifier = Modifier.semantics { testTagsAsResourceId = true },
                        color = MaterialTheme.colors.background
                    ) {
                        SiliconeCalculatorNavHost(
                            navController = navController,
                            onThemeToggle = onThemeToggle
                        )
                    }
                }
            }
        }
    }
}
