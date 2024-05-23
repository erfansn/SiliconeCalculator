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

package ir.erfansn.siliconecalculator.profiler

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import ir.erfansn.siliconecalculator.TARGET_PACKAGE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(packageName = TARGET_PACKAGE) {
        pressHome()
        startActivityAndWait()

        repeat(2) {
            for (digit in '1'..'9') {
                device.findObject(By.res("calculator:$digit")).click()
            }
        }
        device.waitForIdle()
        device.findObject(By.res("calculator:result")).swipe(Direction.RIGHT, 1.0f)
        device.waitForIdle()

        device.findObject(By.res("calculator:+")).click()
        device.findObject(By.res("calculator:1")).click()
        device.findObject(By.res("calculator:=")).click()
        device.waitForIdle()

        device.findObject(By.descContains("Theme changer")).click()
        device.waitForWindowUpdate(packageName, 1000)

        device.findObject(By.res("calculator:AC")).click()
        device.findObject(By.res("calculator:3")).click()
        repeat(10) {
            device.findObject(By.res("calculator:-")).click()
            device.findObject(By.res("calculator:2")).click()
            device.findObject(By.res("calculator:=")).click()
            device.waitForIdle()
        }
    }
}