package ar.com.wolox.wolmo.core.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import javax.inject.Inject

/**
 * An utility class to work with Android's {@link Toast} messages
 */
@ApplicationScope
class ToastFactory @Inject constructor(private val mContext: Context){

    /**
     * Displays a text message from a resource ID inside a [Toast], briefly
     *
     * @param resId A resource ID from a [String] with the message to be displayed
     */
    fun show(@StringRes resId: Int) = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show()

    /**
     * Displays a given [String] inside a [Toast], briefly
     *
     * @param message An [String] with the message to be displayed
     */
    fun show(message: String) = Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

    /**
     * Displays a text message from a resource ID inside a toast, during a longer than usual
     * interval
     *
     * @param resId A resource ID from a [String] with the message to be displayed
     */
    fun showLong(@StringRes resId: Int) = Toast.makeText(mContext, resId, Toast.LENGTH_LONG).show()

    /**
     * Displays a given [String] inside a [Toast], during a longer than usual
     * interval
     *
     * @param message An [String] with the message to be displayed
     */
    fun showLong(message: String) = Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
}