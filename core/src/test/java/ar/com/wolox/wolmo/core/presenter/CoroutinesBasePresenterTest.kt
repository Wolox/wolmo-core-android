/*
 * Copyright (c) Wolox S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ar.com.wolox.wolmo.core.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class CoroutinesBasePresenterTest {

    private interface ExampleView

    private lateinit var basePresenter: CoroutineBasePresenter<ExampleView>
    private lateinit var mockedView: ExampleView

    @Before
    fun beforeTest() {
        mockedView = mock(ExampleView::class.java)
        basePresenter = CoroutineBasePresenter<ExampleView>(Dispatchers.Default).apply {
            attachView(mockedView)
        }
    }

    @Test
    fun `given a coroutines presenter when view is detached then presenter scope is not active`() {
        assertThat(basePresenter.isActive, `is`(true))
        basePresenter.onViewDetached()

        assertThat(basePresenter.isActive, `is`(false))
    }

    @Test
    fun `given a coroutines presenter when view is detached then all coroutines are cancelled`() = runBlocking {
        val countToCancel = 5
        val timeToCancel: Long = 4_200
        val timeToDelay: Long = 1_000

        var countExecuting = 0
        basePresenter.launch {
            while (isActive) {
                countExecuting++
                delay(timeToDelay)
            }
        }

        delay(timeToCancel)
        basePresenter.onViewDetached()

        assertThat(countExecuting, `is`(countToCancel))
    }
}