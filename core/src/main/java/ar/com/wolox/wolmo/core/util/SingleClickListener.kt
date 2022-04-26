package ar.com.wolox.wolmo.core.util

import android.view.View

class SingleClickListener(
    private val click: ((View) -> Unit)
) : View.OnClickListener {

    override fun onClick(view: View) {
        if (enabled) {
            enabled = false
            view.postDelayed(ENABLE_AGAIN, INTERVAL_MILLIS)
            click(view)
        }
    }

    companion object {
        private const val INTERVAL_MILLIS = 1000L
        @JvmStatic
        var enabled = true
        private val ENABLE_AGAIN =
            Runnable { enabled = true }
    }
}
