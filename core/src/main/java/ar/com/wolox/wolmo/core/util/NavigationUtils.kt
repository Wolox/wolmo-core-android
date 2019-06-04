package ar.com.wolox.wolmo.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import java.io.Serializable
import java.util.ArrayList

class NavigationUtils {

    companion object {
        private const val BLANK_PAGE = "about:blank"


        /**
         * Opens the browser with a given URL.
         *
         * @param context An instance of [Context]. Can't be null.
         * @param url     The URL that the browser should open. If the URL is null or empty,
         * the browser will be opened with a blank page.
         */
        fun openBrowser(context: Context, url: String?) {
            var urlValue = url
            if (TextUtils.isEmpty(urlValue)) urlValue = BLANK_PAGE

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlValue))
            if (context !is Activity) {
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        }

        /**
         * Sends an intent to start an [Activity] for the provided [Class]
         *
         * @param context An instance of [Context]. Can't be null.
         * @param clazz   The [Class] of the [Activity] that will be opened. Can't be null.
         */
        fun jumpTo(context: Context, clazz: Class<*>) {
            val intent = Intent(context, clazz)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        /**
         * Sends an intent to start an [Activity] for the provided [Class]
         *
         * @param context      An instance of [Context]. Can't be null.
         * @param clazz        The [Class] of the [Activity] that will be opened.
         * Can't be null.
         * @param intentExtras Variable number of instances of [IntentExtra] that will be sent
         * as extras to the started [Activity]
         */
        fun jumpTo(context: Context,
                   clazz: Class<*>,
                   vararg intentExtras: IntentExtra) {
            val intent = Intent(context, clazz)
            for (intentExtra in intentExtras) {
                intent.putExtra(intentExtra.reference, intentExtra.`object`)
            }
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        /**
         * Sends an intent to start an [Activity] for the provided [Class] but clearing
         * the current task and starting a new one.
         *
         * @param context An instance of [Context]. Can't be null.
         * @param clazz   The [Class] of the [Activity] that will be opened. Can't be null.
         */
        fun jumpToClearingTask(context: Context, clazz: Class<*>) {
            val intent = Intent(context, clazz)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        /**
         * Sends an intent to start an [Activity] for the provided [Class] but clearing
         * the current task and starting a new one.
         *
         * @param context      An instance of [Context]. Can't be null.
         * @param clazz        The [Class] of the [Activity] that will be opened.
         * Can't be null.
         * @param intentExtras Variable number of instances of [IntentExtra] that will be sent
         * as extras to the started [Activity]
         */
        fun jumpToClearingTask(context: Context,
                               clazz: Class<*>,
                               vararg intentExtras: IntentExtra) {
            val intent = Intent(context, clazz)
            for (intentExtra in intentExtras) {
                intent.putExtra(intentExtra.reference, intentExtra.`object`)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        /**
         * Sends an intent to start an [Activity] for the provided [Class] with an
         * animation defined by [ActivityOptionsCompat]
         *
         * @param activity   The [Activity] where the intent will be sent from.
         * Can't be null.
         * @param clazz      The [Class] of the [Activity] that will
         * be opened. Can't be null.
         * @param transition An instance of [ActivityOptionsCompat] that defines
         * the animation behaviour.
         */
        fun jumpToWithAnimation(activity: Activity,
                                clazz: Class<*>,
                                transition: ActivityOptionsCompat) {
            ActivityCompat.startActivity(activity, Intent(activity, clazz), transition.toBundle())
        }

        /**
         * Sends an intent to start an [Activity] for the provided [Class] with an
         * animation defined by [ActivityOptionsCompat]
         *
         * @param activity     The [Activity] where the intent will be sent from.
         * Can't be null.
         * @param clazz        The [Class] of the [Activity] that will
         * be opened. Can't be null.
         * @param transition   An instance of [ActivityOptionsCompat] that defines
         * the animation behaviour.
         * @param intentExtras Variable number of instances of [IntentExtra] that
         * will be sent as extras to the started [Activity]
         */
        fun jumpToWithAnimation(activity: Activity,
                                clazz: Class<*>,
                                transition: ActivityOptionsCompat,
                                vararg intentExtras: IntentExtra) {
            val intent = Intent(activity, clazz)
            for (intentExtra in intentExtras) {
                intent.putExtra(intentExtra.reference, intentExtra.`object`)
            }
            ActivityCompat.startActivity(activity, intent, transition.toBundle())
        }

        /**
         * Returns an instance of [ActivityOptionsCompat] constructed from an instance of the
         * [Activity] where the intent will be send and a number of pairs of [View] and
         * [String] representing the shared view in the transition.
         *
         * @param activity The [Activity] where the intent is being started.
         * @param pairs    Paris of [View] and [String] with the shared elements.
         * @return A non null instance of @[ActivityOptionsCompat]
         */
        @SafeVarargs
        fun buildActivityOptions(
                activity: Activity, vararg pairs: Pair<View, String>): ActivityOptionsCompat {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs)
        }

        class Builder(private val activity: Activity) {
            private var clazz: Class<*>? = null
            private val sharedElements: ArrayList<Pair<View, String>> = ArrayList()
            private val intentExtras: ArrayList<IntentExtra> = ArrayList()

            fun setClass(clazz: Class<*>): Builder {
                this.clazz = clazz
                return this
            }

            fun addSharedElement(sharedView: View, sharedString: String): Builder {
                sharedElements.add(Pair(sharedView, sharedString))
                return this
            }

            fun addIntentObjects(vararg intentExtras: IntentExtra): Builder {
                for (intentExtra in intentExtras) {
                    addIntentObject(intentExtra)
                }
                return this
            }

            private fun addIntentObject(intentExtra: IntentExtra): Builder {
                this.intentExtras.add(intentExtra)
                return this
            }

            fun addExtra(reference: String, `object`: Serializable): Builder {
                addIntentObject(IntentExtra(reference, `object`))
                return this
            }

            fun jump() {
                clazz?.let {
                    if (sharedElements.isEmpty()) {
                        jumpTo(activity, it,
                                *intentExtras.toTypedArray())
                    } else {
                        jumpToWithAnimation(activity, it, buildActivityOptions(activity,
                                *sharedElements.toTypedArray()),
                                *intentExtras.toTypedArray())
                    }
                }
            }
        }

        /**
         * An utility class to pair [Intent] extras with their corresponding references.
         */
        class IntentExtra
        /**
         * Constructor that binds an [Intent] extra with its corresponding reference.
         *
         * @param reference A reference for an [Intent] extra. Can't be null.
         * @param object    An instance of an [Intent] extra. Can't be null.
         */
        (internal val reference: String, internal val `object`: Serializable)
    }

}