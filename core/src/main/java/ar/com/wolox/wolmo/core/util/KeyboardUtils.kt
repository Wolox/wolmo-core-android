package ar.com.wolox.wolmo.core.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

class KeyboardUtils {

    companion object {
        /**
         * Forces the device's soft keyboard (that means the software keyboard, not a physical one)
         * to show for a specific [EditText]
         *
         * @param context  an instance of [Context]
         * @param editText an instance of an attached [EditText]
         */
        fun showKeyboard(context: Context, editText: EditText) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }

        /**
         * Forces the device's soft keyboard (that means the software keyboard, not a physical one)
         * to hide, meant to be called from inside a [Fragment]
         *
         * @param context an instance of [Context]
         * @param view    an instance of [Fragment]
         */
        fun hideKeyboard(context: Context, view: View) {
            val imm = context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * Forces the device's soft keyboard (that means the software keyboard, not a physical one)
         * to hide, meant to be called from inside an [Activity]
         *
         * @param activity an instance of the currently displayed [Activity]
         */
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            // If no view currently has focus, create a new one,
            // just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}