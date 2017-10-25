package ar.com.wolox.wolmo.core.util;

import android.util.Log;

import javax.inject.Inject;

/**
 * Wrapper of {@link Log} to simplify logs in the same class by reusing the tag and
 * to simplify unit tests.
 */
public class Logger {

    private String mTag;

    @Inject
    Logger() {}

    /**
     * Sets the tag for this logger.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     */
    public void setTag(String tag) {
        mTag = tag;
    }

    /**
     * Send a {@link Log#VERBOSE} log message.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     */
    public int v(String msg) {
        return v(mTag, msg);
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int v(String msg, Throwable tr) {
        return v(mTag, msg, tr);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     */
    public int d(String msg) {
        return d(mTag, msg);
    }

    /**
     * Send a {@link Log#DEBUG} log message and log the exception.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int d(String msg, Throwable tr) {
        return d(mTag, msg, tr);
    }

    /**
     * Send an {@link Log#INFO} log message.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     */
    public int i(String msg) {
        return i(mTag, msg);
    }

    /**
     * Send a {@link Log#INFO} log message and log the exception.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int i(String msg, Throwable tr) {
        return i(mTag, msg, tr);
    }

    /**
     * Send a {@link Log#WARN} log message.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     */
    public int w(String msg) {
        return w(mTag, msg);
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int w(String msg, Throwable tr) {
        return w(mTag, msg, tr);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     */
    public int e(String msg) {
        return e(mTag, msg);
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     * The tag used is the one defined by {@link #setTag(String)}.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int e(String msg, Throwable tr) {
        return e(mTag, msg, tr);
    }

    /**
     * Send a {@link Log#VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public int v(String tag, String msg) {
        return Log.v(tag, msg);
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int v(String tag, String msg, Throwable tr) {
        return Log.v(tag, msg, tr);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public int d(String tag, String msg) {
        return Log.d(tag, msg);
    }

    /**
     * Send a {@link Log#DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int d(String tag, String msg, Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    /**
     * Send an {@link Log#INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public int i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    /**
     * Send a {@link Log#INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int i(String tag, String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    /**
     * Send a {@link Log#WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public int e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public int e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }
}
