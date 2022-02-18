package ar.com.wolox.wolmo.core.util

import android.util.Log
import ar.com.wolox.wolmo.core.extensions.orElse
import ar.com.wolox.wolmo.core.extensions.toStringSafely

object WolmoLogger {

    private const val WOLMO_NAME = "WolmoCoreAndroid"
    var tag: String? = null

    fun resetTag() {
        this.tag = null
    }

    fun log(exception: Throwable? = null, message : () -> Any?) {
        Log.d(tag.orElse(WOLMO_NAME), message.toStringSafely(), exception)
    }

    fun warn(exception: Throwable? = null, message : () -> Any?) {
        Log.w(tag.orElse(WOLMO_NAME), message.toStringSafely(), exception)
    }

    fun info(exception: Throwable? = null, message : () -> Any?) {
        Log.i(tag.orElse(WOLMO_NAME), message.toStringSafely(), exception)
    }

    fun verbose(exception: Throwable? = null, message : () -> Any?) {
        Log.v(tag.orElse(WOLMO_NAME), message.toStringSafely(), exception)
    }

    fun error(exception: Throwable? = null, message : () -> Any?) {
        Log.e(tag.orElse(WOLMO_NAME), message.toStringSafely(), exception)
    }

    fun terribleFailure(exception: Throwable? = null, message : () -> Any?) {
        Log.wtf(tag.orElse(WOLMO_NAME), message.toStringSafely(), exception)
    }
}
