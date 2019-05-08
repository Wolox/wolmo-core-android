package ar.com.wolox.wolmo.core.tests

import ar.com.wolox.wolmo.core.presenter.BasePresenter
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.reflect.ParameterizedType

/**
 * A base that setups the environment for a Wolmo's [BasePresenter] test.
 */
abstract class WolmoPresenterTest<V : Any, P : BasePresenter<V>> {

    /**
     * The presenter to be tested.
     */
    protected lateinit var presenter: P

    /**
     * The mocked view that the presenter will use.
     */
    lateinit var view: V

    @Suppress("UNCHECKED_CAST")
    private fun getViewClass(): Class<V> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<V>
    }

    /**
     * Setup the environment.
     */
    @Before
    fun setupWolmoPresenterTest() {
        MockitoAnnotations.initMocks(this)
        presenter = getPresenterInstance()
        view = Mockito.mock(getViewClass())
        presenter.attachView(view)
    }

    /**
     * This method provides the presenter instance that will be tested.
     * If the presenter should be spied, it's possible to return the presenter spied with [Mockito.spy].
     *
     * @return the [BasePresenter] instance.
     */
    abstract fun getPresenterInstance(): P
}w