package ir.erfansn.siliconecalculator.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalBaselineProfilesApi
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startup() =
        baselineProfileRule.collectBaselineProfile(packageName = "ir.erfansn.siliconecalculator") {
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

            device.findObject(By.descContains("Calculations history")).click()
            device.waitForWindowUpdate(packageName, 1000)

            device.findObject(By.res("history:items")).swipe(Direction.DOWN, 1.0f, 400)
            device.waitForIdle()
            device.findObject(By.res("history:items")).swipe(Direction.UP, 1.0f, 400)
            device.waitForIdle()

            device.findObject(By.descContains("Clear history")).click()
            device.waitForWindowUpdate(packageName, 1000)
            device.findObject(By.res("history:clear")).click()
            device.waitForWindowUpdate(packageName, 1000)
        }
}
