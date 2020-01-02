package ar.com.wolox.wolmo.core.extensions

import android.os.Build
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
class ViewExtensionsTest {

    @Test
    fun `given a visible TextView when set text or gone an empty text then the TextView turns invisible`() {
        val textView = TextView(ApplicationProvider.getApplicationContext())
        assertThat(textView.isVisible, `is`(true))

        textView.setTextOrGone("")

        assertThat(textView.isVisible, `is`(false))
        assertThat(textView.text.toString(), `is`(""))
    }

    @Test
    fun `given a visible TextView when set text or gone a non empty text then the TextView shows text`() {
        val textView = TextView(ApplicationProvider.getApplicationContext())
        assertThat(textView.isVisible,       `is`(true))

        textView.setTextOrGone("test")

        assertThat(textView.isVisible, `is`(true))
        assertThat(textView.text.toString(), `is`("test"))
    }
}