package ar.com.wolox.wolmo.core.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineTestRuleTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(runOnAllTests = true)

    @Test(expected = Test.None::class /* no exception expected */)
    fun `given a rule with run on all tests true when run coroutine on main then there's no exception thrown`() {
        runBlocking(Dispatchers.Main) {}
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRuleTest2 {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(runOnAllTests = false)

    @Test(expected = IllegalStateException::class)
    fun `given a rule with run on all tests false when run coroutine on main then a IllegalStateException is thrown`() {
        runBlocking(Dispatchers.Main) {}
    }

    @Test(expected = Test.None::class /* no exception expected */)
    @CoroutineTest
    fun `given a rule with run on all tests false and annotated with CoroutineScope when run coroutine on main then there's no exception thrown`() {
        runBlocking(Dispatchers.Main) {}
    }
}