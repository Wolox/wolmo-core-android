package ar.com.wolox.wolmo.core.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarFactory {

    /** Displays a [message] from a string in a [Snackbar], inside the given [View] ViewGroup, briefly.
     * It also adds an action button with the [actionMessage] message with the given [action] */
    fun show(view: View, message: String, actionMessage: String, action: () -> Unit) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).apply {
            setAction(actionMessage) { action() }
            show()
        }

    /** Displays a [message] from a string in a [Snackbar], inside the given [View] ViewGroup, briefly. */
    fun show(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

    /** Displays a [message] from a string in a [Snackbar], inside the given [View] ViewGroup in a longer interval.
     * It also adds an action button with the [actionMessage] message with the given [action] */
    fun showLong(view: View, message: String, actionMessage: String, action: () -> Unit) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
            setAction(actionMessage) { action() }
            show()
        }

    /** Displays a [message] from a string in a [Snackbar], inside the given [View] ViewGroup in a longer interval. */
    fun showLong(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    /** Displays a [message] from a string in a [Snackbar], inside the given [View] ViewGroup, indefinitely.
     * It also adds an action button with the [actionMessage] message with the given [action] */
    fun showIndefinitely(view: View, message: String, actionMessage: String, action: () -> Unit) =
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(actionMessage) { action() }
            show()
        }

    /** Displays a [message] from a string in a [Snackbar], inside the given [View] ViewGroup, indefinitely. */
    fun showIndefinitely(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show()
}
