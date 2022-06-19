package ir.erfansn.siliconecalculator.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule : TestWatcher() {

    private val testScope = TestScope()
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(testScope.testScheduler)

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
