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

import org.assertj.core.api.Java6Assertions.assertThat
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

import org.junit.Before
import org.junit.Test

import androidx.core.util.Consumer

class BasePresenterTest {

    private lateinit var basePresenter: BasePresenter<Any>

    @Before
    fun beforeTest() {
        basePresenter = spy(BasePresenter<Any>()::class.java)
    }

    @Test
    fun viewAttachedUpdates() {
        basePresenter.run {
            assertThat(isViewAttached()).isEqualTo(false)

            attachView(Any())
            assertThat(isViewAttached()).isEqualTo(true)

            detachView()
            assertThat(isViewAttached()).isEqualTo(false)
        }
    }

    @Test
    fun presenterNotifiesChildClasses() {
        // Attach and detach the view

        basePresenter.run {
            attachView(Any())
            verify(this, times(1)).onViewAttached()
            verify(this, times(0)).onViewDetached()

            detachView()
            verify(this, times(1)).onViewDetached()
        }
    }

    @Test
    fun presenterRunIfViewAttachedRunnable() {
        val runnableMock = mock(Runnable::class.java)

        basePresenter.run {
            runIfViewAttached(runnableMock)
            verify(runnableMock, times(0)).run()

            // Now the view is attached
            attachView(Any())
            runIfViewAttached(runnableMock)
            verify(runnableMock, times(1)).run()
        }
    }

    @Test
    fun presenterRunIfViewAttachedConsumer() {
        val consumerMock = mock(Consumer::class.java) as Consumer<Any>

        basePresenter.run {

            runIfViewAttached(consumerMock)
            verify(consumerMock, times(0)).accept(any(Any::class.java))

            // Now the view is attached
            attachView(Any())
            runIfViewAttached(consumerMock)
            verify(consumerMock, times(1)).accept(any(Any::class.java))
        }
    }

}
