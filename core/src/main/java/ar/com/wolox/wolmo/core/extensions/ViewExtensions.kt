package ar.com.wolox.wolmo.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible

/** Inflate a [layoutRes] inside a [ViewGroup]. */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

/**
 * Sets a [newText] to this [TextView]. If the text is null or empty the [TextView.isVisible] will be set to false.
 * Otherwise it will be set to true.
 */
fun TextView.setTextOrGone(newText: String?) {
    isVisible = !newText.isNullOrEmpty()
    text = newText
}