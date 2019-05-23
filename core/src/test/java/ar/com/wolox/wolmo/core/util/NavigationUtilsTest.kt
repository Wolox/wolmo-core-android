package ar.com.wolox.wolmo.core.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.test.core.app.ApplicationProvider
import ar.com.wolox.wolmo.core.activity.WolmoActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.LOLLIPOP])
class NavigationUtilsTest {

    private var mContextSpy: Context? = null
    private var mActivitySpy: Activity? = null

    @Before
    fun beforeTest() {
        mActivitySpy = spy<Activity>(Robolectric.buildActivity(Activity::class.java).create().start().get())
        mContextSpy = spy<Context>(ApplicationProvider.getApplicationContext<Context>())
    }

    @Test
    fun openBrowserWithUrlShouldStartActivity() {
        mContextSpy?.let { NavigationUtils.openBrowser(it, "http://google.com") }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(mContextSpy, times(1))?.startActivity(intentCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.data?.toString()).isEqualTo("http://google.com")
        assertThat(intent.action).isEqualTo(Intent.ACTION_VIEW)
    }

    @Test
    fun jumpToShouldStartActivity() {
        mContextSpy?.let { NavigationUtils.jumpTo(it, WolmoActivity::class.java) }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(mContextSpy, times(1))?.startActivity(intentCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mContextSpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)
    }

    @Test
    fun jumpToWithExtrasShouldStartActivity() {
        val extra = NavigationUtils.Companion.IntentExtra("Tag", "Value")

        mContextSpy?.let { NavigationUtils.jumpTo(it, WolmoActivity::class.java, extra) }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(mContextSpy, times(1))?.startActivity(intentCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mContextSpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)
        assertThat(intent.getStringExtra("Tag")).isEqualTo("Value")
    }

    @Test
    fun jumpToClearingTaskShouldAddFlags() {
        mContextSpy?.let { NavigationUtils.jumpToClearingTask(it, WolmoActivity::class.java) }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(mContextSpy, times(1))?.startActivity(intentCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mContextSpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)
        assertThat(intent.flags).matches { flags -> flags and Intent.FLAG_ACTIVITY_NEW_TASK == Intent.FLAG_ACTIVITY_NEW_TASK }
        assertThat(intent.flags).matches { flags -> flags and Intent.FLAG_ACTIVITY_CLEAR_TASK == Intent.FLAG_ACTIVITY_CLEAR_TASK }
    }

    @Test
    fun jumpToClearingTaskShouldAddFlagsAndExtras() {
        val extra = NavigationUtils.Companion.IntentExtra("Tag", "Value")

        mContextSpy?.let { NavigationUtils.jumpToClearingTask(it, WolmoActivity::class.java, extra) }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(mContextSpy, times(1))?.startActivity(intentCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mContextSpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)
        assertThat(intent.flags).matches { flags -> flags and Intent.FLAG_ACTIVITY_NEW_TASK == Intent.FLAG_ACTIVITY_NEW_TASK }
        assertThat(intent.flags).matches { flags -> flags and Intent.FLAG_ACTIVITY_CLEAR_TASK == Intent.FLAG_ACTIVITY_CLEAR_TASK }
        assertThat(intent.getStringExtra("Tag")).isEqualTo("Value")
    }

    @Test
    fun jumpToWithAnimationShouldBundleAnimation() {
        val optionsCompatMock = mock(ActivityOptionsCompat::class.java)
        val bundleMock = mock(Bundle::class.java)
        `when`(optionsCompatMock.toBundle()).thenReturn(bundleMock)

        mActivitySpy?.let { NavigationUtils.jumpToWithAnimation(it, WolmoActivity::class.java, optionsCompatMock) }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        val bundleCaptor = ArgumentCaptor.forClass(Bundle::class.java)
        verify(mActivitySpy, times(1))?.startActivity(intentCaptor.capture(), bundleCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mActivitySpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)

        assertThat(bundleCaptor.value).isSameAs(bundleMock)
    }

    @Test
    fun builderShouldPassAllTheConfigWhenJumpingWithoutAnimation() {
        mActivitySpy?.let {
            val builder = NavigationUtils.Companion.Builder(it)

            builder.setClass(WolmoActivity::class.java)
                    .addExtra("ExtraTag", "ExtraObject")
                    .addIntentObjects(NavigationUtils.Companion.IntentExtra("IntentExtra", "IntentObject"),
                            NavigationUtils.Companion.IntentExtra("IntentExtra2", "IntentObject2"))
                    .jump()
        }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        verify(mActivitySpy, times(1))?.startActivity(intentCaptor.capture())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mActivitySpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)
        assertThat(intent.getStringExtra("ExtraTag")).isEqualTo("ExtraObject")
        assertThat(intent.getStringExtra("IntentExtra")).isEqualTo("IntentObject")
        assertThat(intent.getStringExtra("IntentExtra2")).isEqualTo("IntentObject2")
    }

    @Test
    fun builderShouldPassAllTheConfigWhenJumpingWithAnimation() {
        val viewMock = mock(View::class.java)
        mActivitySpy?.let {
            val builder = NavigationUtils.Companion.Builder(it)
            builder.setClass(WolmoActivity::class.java)
                    .addExtra("ExtraTag", "ExtraObject")
                    .addSharedElement(viewMock, "SharedView")
                    .addIntentObjects(NavigationUtils.Companion.IntentExtra("IntentExtra", "IntentObject"),
                            NavigationUtils.Companion.IntentExtra("IntentExtra2", "IntentObject2"))
                    .jump()
        }

        val intentCaptor = ArgumentCaptor.forClass(Intent::class.java)
        // The appCompat library checks if it needs to send the shared views, but in this case
        // the view does not exist in any activity and we get null as Options.
        verify(mActivitySpy, times(1))?.startActivity(intentCaptor.capture(), isNull())

        val intent = intentCaptor.value
        assertThat(intent.component?.packageName).isEqualTo(mActivitySpy?.packageName)
        assertThat(intent.component?.className).isEqualTo(WolmoActivity::class.java.canonicalName)
        assertThat(intent.getStringExtra("ExtraTag")).isEqualTo("ExtraObject")
        assertThat(intent.getStringExtra("IntentExtra")).isEqualTo("IntentObject")
        assertThat(intent.getStringExtra("IntentExtra2")).isEqualTo("IntentObject2")
    }
}