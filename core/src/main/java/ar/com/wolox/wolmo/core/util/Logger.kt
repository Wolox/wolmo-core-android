package ar.com.wolox.wolmo.core.util

import android.util.Log

import javax.inject.Inject

/**
 * Wrapper of [Log] to simplify logs in the same class by reusing the tag and
 * to simplify unit tests.
 */
class Logger @Inject constructor() {

    /** [tag] used to identify the source of a log message. */
    var tag: String? = null

    /** Send a [Log.VERBOSE] log a [message] and an [exception] with a [tag] to identify the source of it. */
    @JvmOverloads
    fun v(tag: String?, message: String, exception: Throwable? = null) = Log.v(tag, message, exception)

    /** Send a [Log.VERBOSE] log a [message] and an [exception] using the predefined [tag]. */
    @JvmOverloads
    fun v(message: String, exception: Throwable? = null) = v(tag, message, exception)

    /** Send a [Log.DEBUG] log a [message] and an [exception] with a [tag] to identify the source of it. */
    @JvmOverloads
    fun d(tag: String?, message: String, exception: Throwable? = null) = Log.d(tag, message, exception)

    /** Send a [Log.DEBUG] log a [message] and an [exception] using the predefined [tag]. */
    @JvmOverloads
    fun d(message: String, exception: Throwable? = null) = d(tag, message, exception)

    /** Send a [Log.INFO] log a [message] and an [exception] with a [tag] to identify the source of it. */
    @JvmOverloads
    fun i(tag: String?, message: String, exception: Throwable? = null) = Log.i(tag, message, exception)

    /** Send a [Log.INFO] log a [message] and an [exception] using the predefined [tag]. */
    @JvmOverloads
    fun i(message: String, exception: Throwable? = null) = i(tag, message, exception)

    /** Send a [Log.WARN] log a [message] and an [exception] with a [tag] to identify the source of it. */
    @JvmOverloads
    fun w(tag: String?, message: String, exception: Throwable? = null) = Log.w(tag, message, exception)

    /** Send a [Log.WARN] log a [message] and an [exception] using the predefined [tag]. */
    @JvmOverloads
    fun w(message: String, exception: Throwable? = null) = w(tag, message, exception)

    /** Send a [Log.ERROR] log a [message] and an [exception] with a [tag] to identify the source of it. */
    @JvmOverloads
    fun e(tag: String?, message: String, exception: Throwable? = null) = Log.e(tag, message, exception)

    /** Send a [Log.ERROR] log a [message] and an [exception] using the predefined [tag]. */
    @JvmOverloads
    fun e(message: String, exception: Throwable? = null) = e(tag, message, exception)
}
