package ar.com.wolox.wolmo.core

import ar.com.wolox.wolmo.core.presenter.BasePresenter
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.reflect.ParameterizedType

abstract class WolmoPresenterTest<V : Any, P : BasePresenter<V>> {

    protected lateinit var presenter: P

    lateinit var view: V

    @Suppress("UNCHECKED_CAST")
    private fun getViewClass(): Class<V> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<V>
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = getPresenterInstance()
        view = Mockito.mock(getViewClass())
        presenter.attachView(view)
    }

    abstract fun getPresenterInstance(): P
}