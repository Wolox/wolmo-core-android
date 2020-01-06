package ar.com.wolox.wolmo.core.tests

import android.graphics.Rect
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class WolmoPresenterTestTest : WolmoPresenterTest<WolmoPresenterTestTest.TestView, WolmoPresenterTestTest.TestPresenter>() {

    @Mock
    lateinit var rectMocked: Rect

    override fun getPresenterInstance() = TestPresenter()

    private fun <T> tryToGet(getter: () -> T): T? = try {
        getter()
    } catch (ignored: Exception) {
        null
    }

    @Test
    fun `given a test when getting instances then those are not null`() {

        val presenter = tryToGet { presenter }
        val view = tryToGet { view }

        assertThat(presenter, `is`(notNullValue()))
        assertThat(view, `is`(notNullValue()))
    }

    @Test
    fun `given a annotated mock when using it then it's not null`() {

        val rectMocked = tryToGet { rectMocked }

        assertThat(rectMocked, `is`(notNullValue()))
    }

    @Test
    fun `given a presenter and a view when getting presenter get a call view request then it calls the view`() {
        presenter.onCallViewRequest()
        verify(view, times(1)).doSomething()
    }

    interface TestView {
        fun doSomething()
    }

    class TestPresenter : BasePresenter<TestView>() {
        fun onCallViewRequest() = view?.doSomething()
    }
}