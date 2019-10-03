package ar.com.wolox.wolmo.core.presenter

import androidx.core.util.Consumer
import javax.inject.Inject

/**
 * Base presenter that provides the view to the specific presenters.
 */
open class BasePresenter<V> @Inject constructor() {

    private var view: V? = null

    /**
     * Returns the [view] attached this presenter.
     * **NOTE: ** You should check if the view is attached calling the
     * method [before calling any method of the view.][BasePresenter.isViewAttached]
     */
    private fun getView(): V? = view

    /**
     * Returns **true** if the [view] is attached, **false** otherwise.
     */
    fun isViewAttached(): Boolean = view != null

    /**
     * Call this method to attach a given [viewInstance] to this presenter.
     * The attached view should be ready to interact with it. This method calls [onViewAttached]
     * to notify subclasses that a view was attached.
     */
    fun attachView(viewInstance: V) {
        view = viewInstance
        onViewAttached()
    }

    /**
     * Method called to detach the [view] of this presenter.
     * This method calls [onViewDetached] to notify subclasses.
     */
    fun detachView() {
        view = null
        onViewDetached()
    }

    /**
     * Listener called when a view is attached and it's safe to interact with it.
     */
    fun onViewAttached() {}

    /**
     * Method called when the view is destroyed and it's no longer safe for the presenter to
     * interact with it.
     */
    fun onViewDetached() {}

    /**
     * Runs the [method] if the [view] is attached to the presenter.
     * If the [view] is not attached, the expression will not run.
     * The consumer receives the view instance as argument.
     */
    fun runIfViewAttached(method: Consumer<V>) {
        if (isViewAttached()) {
            method.accept(view)
        }
    }

    /**
     * Runs the [method] if the [view] is attached to the presenter.
     * If the [view] is not attached, the expression will not run.
     * The runnable will not run in another thread.
     */
    fun runIfViewAttached(method: Runnable) {
        if (isViewAttached()) {
            method.run()
        }
    }
}
