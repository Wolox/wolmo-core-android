/*
 * Copyright (c) Wolox S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
@file:JvmName("NavigationUtilsKt")

package ar.com.wolox.wolmo.core.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.Serializable

/** An utility class to pair [Intent] extras with their corresponding keys. */
typealias IntentExtra = Pair<String, Serializable>

private const val BLANK_PAGE = "about:blank"

private fun Intent.setNewTaskIfNecessary(context: Context): Intent {
    if (context !is Activity) {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    return this
}

/**
 * Opens the browser with a given [url]. If the [url] is null or empty,
 * the browser will be opened with a blank page.
 */
fun Context.openBrowser(url: String?) {
    val finalUrl = when {
        url.isNullOrEmpty() -> BLANK_PAGE
        url.startsWith(BASE_HTTP) || url.startsWith(BASE_HTTPS) -> url
        else -> "${BASE_HTTP}$url"
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, finalUrl.toUri()).setNewTaskIfNecessary(this)
    ContextCompat.startActivity(this, browserIntent, null)
}

private const val BASE_HTTP = "http://"
private const val BASE_HTTPS = "https://"

/** Opens the dial with a given [phone]. */
fun Context.openDial(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri()).setNewTaskIfNecessary(this)
    ContextCompat.startActivity(this, intent, null)
}

/**
 * Makes a call to the given [phone].
 * Android Manifest should contains CALL_PHONE permission.
 */
@RequiresPermission(value = Manifest.permission.CALL_PHONE)
fun Context.makeCall(phone: String) {
    val intent = Intent(Intent.ACTION_CALL, "tel:$phone".toUri()).setNewTaskIfNecessary(this)
    ContextCompat.startActivity(this, intent, null)
}

/**
 * Sends an intent to start an [Activity] for the provided [clazz] from a [context]
 * with a variable number of instances of [intentExtras] that will be sent as extras.
 * It accepts a [transition] that defines the animation behaviour.
 */
@SafeVarargs
fun Context.jumpTo(
    clazz: Class<*>,
    vararg intentExtras: IntentExtra
) = jumpTo(clazz, null, *intentExtras)

/**
 * Sends an intent to start an [Activity] for the provided [clazz] from a [context]
 * with a variable number of instances of [intentExtras] that will be sent as extras.
 * It accepts a [transition] that defines the animation behaviour.
 */
@SafeVarargs
fun Context.jumpTo(
    clazz: Class<*>,
    transition: ActivityOptionsCompat?,
    vararg intentExtras: IntentExtra
) {
    val intent = Intent(this, clazz).apply {
        intentExtras.forEach { putExtra(it.first, it.second) }
        setNewTaskIfNecessary(this@jumpTo)
    }
    ActivityCompat.startActivity(this, intent, transition?.toBundle())
}

/**
 * Sends an intent to start an [Activity] for the provided [clazz] from a [context] but clearing
 * the current task and starting a new one with a variable number of instances of
 * [intentExtras] that will be sent as extras.
 */
@SafeVarargs
fun Context.jumpToClearingTask(clazz: Class<*>, vararg intentExtras: IntentExtra) {
    startActivity(Intent(this, clazz).apply {
        intentExtras.forEach { putExtra(it.first, it.second) }
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    })
}

@Deprecated("Use Context extensions.")
object NavigationUtils {

    /**
     * Opens the browser from a [context] with a given [url]. If the [url] is null or empty,
     * the browser will be opened with a blank page.
     */
    @JvmStatic
    fun openBrowser(context: Context, url: String?) = context.openBrowser(url)

    /**
     * Sends an intent to start an [Activity] for the provided [clazz] from a [context]
     * with a variable number of instances of [intentExtras] that will be sent as extras.
     * It accepts a [transition] that defines the animation behaviour.
     */
    @JvmStatic
    @JvmOverloads
    @SafeVarargs
    fun jumpTo(
        context: Context,
        clazz: Class<*>,
        transition: ActivityOptionsCompat? = null,
        vararg intentExtras: IntentExtra
    ) = context.jumpTo(clazz, transition, *intentExtras)

    /**
     * Sends an intent to start an [Activity] for the provided [clazz] from a [context] but clearing
     * the current task and starting a new one with a variable number of instances of
     * [intentExtras] that will be sent as extras.
     */
    @SafeVarargs
    @JvmStatic
    fun jumpToClearingTask(
        context: Context, clazz: Class<*>,
        vararg intentExtras: IntentExtra
    ) = context.jumpToClearingTask(clazz, *intentExtras)

    /**
     * Returns an instance of [ActivityOptionsCompat] constructed from an instance of the
     * [activity] where the intent will be send and a number of [pairs]
     * representing the shared view in the transition.
     */
    @SafeVarargs
    @JvmStatic
    fun buildActivityOptions(activity: Activity, vararg pairs: Pair<View, String>): ActivityOptionsCompat {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.map {
            androidx.core.util.Pair(it.first, it.second)
        }.toTypedArray())
    }

    @Deprecated("Use normal methods.")
    class Builder(private val activity: Activity) {

        private val sharedElements: MutableList<Pair<View, String>> = mutableListOf()
        private val mIntentExtras: MutableList<IntentExtra> = mutableListOf()
        private var clazz: Class<*>? = null

        fun setClass(clazz: Class<*>): Builder {
            this.clazz = clazz
            return this
        }

        fun addSharedElement(sharedView: View, sharedString: String): Builder {
            sharedElements.add(sharedView to sharedString)
            return this
        }

        @SafeVarargs
        fun addIntentObjects(vararg intentExtras: IntentExtra): Builder {
            mIntentExtras.addAll(intentExtras)
            return this
        }

        fun addExtra(reference: String, `object`: Serializable): Builder {
            mIntentExtras.add(IntentExtra(reference, `object`))
            return this
        }

        fun jump() {
            val transition =
                if (sharedElements.isNotEmpty())
                    buildActivityOptions(activity, *sharedElements.toTypedArray())
                else
                    null
            jumpTo(activity, clazz!!, transition, *mIntentExtras.toTypedArray())
        }
    }
}