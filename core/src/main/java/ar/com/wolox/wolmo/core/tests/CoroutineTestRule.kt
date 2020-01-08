package ar.com.wolox.wolmo.core.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.Executors

/**
 * If a test method is annotated with this, then the [CoroutineTestRule] will be executed.
 * Use it when [CoroutineTestRule.runOnAllTests] is false.
 */
annotation class CoroutineTest

/**
 * A Junit Test Rule that allows to use Coroutines main dispatcher on a test.
 * If [runOnAllTests] is false then all tests will have this configuration,
 * otherwise just those that have [CoroutineTest] annotation.
 */
@ExperimentalCoroutinesApi
class CoroutineTestRule(private val runOnAllTests: Boolean = false) : TestWatcher() {

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private fun isCoroutineTest(description: Description?): Boolean {
        return description?.annotations?.filterIsInstance<CoroutineTest>()?.isNotEmpty() == true
    }

    private fun shouldRunRule(description: Description?): Boolean {
        return runOnAllTests || isCoroutineTest(description)
    }

    override fun starting(description: Description?) {
        if (shouldRunRule(description)) {
            Dispatchers.setMain(mainThreadSurrogate)
        }
    }

    override fun finished(description: Description?) {
        if (shouldRunRule(description)) {
            Dispatchers.resetMain()
            mainThreadSurrogate.close()
        }
    }
}
