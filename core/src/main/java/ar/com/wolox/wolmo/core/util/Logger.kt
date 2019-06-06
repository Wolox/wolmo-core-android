package ar.com.wolox.wolmo.core.util

import android.util.Log
import javax.inject.Inject

class Logger @Inject constructor() {

    private var tag: String? = null

    /**
     * Sets the tag for this logger.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     */
    fun setTag(tag: String) {
        this.tag = tag
    }

    /**
     * Send a [Log.VERBOSE] log message.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     */
    fun v(msg: String) = v(tag, msg)

    /**
     * Send a [Log.VERBOSE] log message and log the exception.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun v(msg: String, tr: Throwable) = v(tag, msg, tr)

    /**
     * Send a [Log.DEBUG] log message.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     */
    fun d(msg: String) = d(tag, msg)

    /**
     * Send a [Log.DEBUG] log message and log the exception.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun d(msg: String, tr: Throwable) = d(tag, msg, tr)

    /**
     * Send an [Log.INFO] log message.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     */
    fun i(msg: String) = i(tag, msg)

    /**
     * Send a [Log.INFO] log message and log the exception.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun i(msg: String, tr: Throwable) = i(tag, msg, tr)

    /**
     * Send a [Log.WARN] log message.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     */
    fun w(msg: String) = w(tag, msg)

    /**
     * Send a [Log.WARN] log message and log the exception.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun w(msg: String, tr: Throwable) = w(tag, msg, tr)

    /**
     * Send an [Log.ERROR] log message.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     */
    fun e(msg: String) = e(tag, msg)

    /**
     * Send a [Log.ERROR] log message and log the exception.
     * The tag used is the one defined by [.setTag].
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun e(msg: String, tr: Throwable) = e(tag, msg, tr)

    /**
     * Send a [Log.VERBOSE] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun v(tag: String?, msg: String) = Log.v(tag.orEmpty(), msg)

    /**
     * Send a [Log.VERBOSE] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun v(tag: String?, msg: String, tr: Throwable) = Log.v(tag, msg, tr)

    /**
     * Send a [Log.DEBUG] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun d(tag: String?, msg: String) = Log.d(tag, msg)

    /**
     * Send a [Log.DEBUG] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun d(tag: String?, msg: String, tr: Throwable) = Log.d(tag, msg, tr)

    /**
     * Send an [Log.INFO] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun i(tag: String?, msg: String) = Log.i(tag, msg)

    /**
     * Send a [Log.INFO] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun i(tag: String?, msg: String, tr: Throwable) = Log.i(tag, msg, tr)

    /**
     * Send a [Log.WARN] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun w(tag: String?, msg: String) = Log.w(tag, msg)

    /**
     * Send a [Log.WARN] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun w(tag: String?, msg: String, tr: Throwable) = Log.w(tag, msg, tr)

    /**
     * Send an [Log.ERROR] log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    fun e(tag: String?, msg: String) = Log.e(tag, msg)

    /**
     * Send a [Log.ERROR] log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    fun e(tag: String?, msg: String, tr: Throwable) = Log.e(tag, msg, tr)
}