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
package ar.com.wolox.wolmo.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ar.com.wolox.wolmo.core.activity.WolmoActivity;
import kotlin.Pair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class NavigationUtilsTest {

    private Context mContextSpy;
    private Activity mActivitySpy;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mActivitySpy = spy(Robolectric.buildActivity(Activity.class).create().start().get());
        mContextSpy = spy(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void openBrowserWithUrlShouldStartActivity() {
        NavigationUtilsKt.openBrowser(mContextSpy, "http://google.com");

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture(), any());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getData().toString()).isEqualTo("http://google.com");
        assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);
    }

    @Test
    public void openDialWithPhoneShouldStartActivity() {
        NavigationUtilsKt.openDial(mContextSpy, "41235678");

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture(), any());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getData().toString()).isEqualTo("tel:41235678");
        assertThat(intent.getAction()).isEqualTo(Intent.ACTION_DIAL);
    }

    @Test
    public void makeCallWithPhoneShouldStartActivity() {
        NavigationUtilsKt.makeCall(mContextSpy, "41235678");

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture(), any());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getData().toString()).isEqualTo("tel:41235678");
        assertThat(intent.getAction()).isEqualTo(Intent.ACTION_CALL);
    }

    @Test
    public void jumpToShouldStartActivity() {
        NavigationUtilsKt.jumpTo(mContextSpy, WolmoActivity.class);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture(), any());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mContextSpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());
    }

    @Test
    public void jumpToWithExtrasShouldStartActivity() {
        Pair<String, String> extra = new Pair<>("Tag", "Value");

        NavigationUtilsKt.jumpTo(mContextSpy, WolmoActivity.class, extra);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture(), any());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mContextSpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());
        assertThat(intent.getStringExtra("Tag")).isEqualTo("Value");
    }

    @Test
    public void jumpToClearingTaskShouldAddFlags() {
        NavigationUtilsKt.jumpToClearingTask(mContextSpy, WolmoActivity.class);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mContextSpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());
        assertThat(intent.getFlags()).matches(
                flags -> (flags & Intent.FLAG_ACTIVITY_NEW_TASK) == Intent.FLAG_ACTIVITY_NEW_TASK);
        assertThat(intent.getFlags()).matches(flags -> (flags & Intent.FLAG_ACTIVITY_CLEAR_TASK)
                == Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    @Test
    public void jumpToClearingTaskShouldAddFlagsAndExtras() {
        Pair<String, String> extra = new Pair<>("Tag", "Value");

        NavigationUtilsKt.jumpToClearingTask(mContextSpy, WolmoActivity.class, extra);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mContextSpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());
        assertThat(intent.getFlags()).matches(
                flags -> (flags & Intent.FLAG_ACTIVITY_NEW_TASK) == Intent.FLAG_ACTIVITY_NEW_TASK);
        assertThat(intent.getFlags()).matches(flags -> (flags & Intent.FLAG_ACTIVITY_CLEAR_TASK)
                == Intent.FLAG_ACTIVITY_CLEAR_TASK);
        assertThat(intent.getStringExtra("Tag")).isEqualTo("Value");
    }

    @Test
    public void jumpToWithAnimationShouldBundleAnimation() {
        ActivityOptionsCompat optionsCompatMock = mock(ActivityOptionsCompat.class);
        Bundle bundleMock = mock(Bundle.class);
        when(optionsCompatMock.toBundle()).thenReturn(bundleMock);

        NavigationUtilsKt.jumpTo(mActivitySpy, WolmoActivity.class, optionsCompatMock);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        ArgumentCaptor<Bundle> bundleCaptor = ArgumentCaptor.forClass(Bundle.class);
        verify(mActivitySpy, times(1))
                .startActivity(intentCaptor.capture(), bundleCaptor.capture());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mActivitySpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());

        assertThat(bundleCaptor.getValue()).isSameAs(bundleMock);
    }

    @Test
    public void BuilderShouldPassAllTheConfigWhenJumpingWithoutAnimation() {
        NavigationUtils.Builder builder = new NavigationUtils.Builder(mActivitySpy);

        builder.setClass(WolmoActivity.class)
                .addExtra("ExtraTag", "ExtraObject")
                .addIntentObjects(new Pair<>("IntentExtra", "IntentObject"),
                        new Pair<>("IntentExtra2", "IntentObject2"))
                .jump();

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mActivitySpy, times(1)).startActivity(intentCaptor.capture(), any());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mActivitySpy.getPackageName());
        assertThat(intent.getComponent().getClassName()).isEqualTo(WolmoActivity.class.getCanonicalName());
        assertThat(intent.getStringExtra("ExtraTag")).isEqualTo("ExtraObject");
        assertThat(intent.getStringExtra("IntentExtra")).isEqualTo("IntentObject");
        assertThat(intent.getStringExtra("IntentExtra2")).isEqualTo("IntentObject2");
    }

    @Test
    public void builderShouldPassAllTheConfigWhenJumpingWithAnimation() {
        NavigationUtils.Builder builder = new NavigationUtils.Builder(mActivitySpy);
        View viewMock = mock(View.class);

        builder.setClass(WolmoActivity.class)
                .addExtra("ExtraTag", "ExtraObject")
                .addSharedElement(viewMock, "SharedView")
                .addIntentObjects(
                        new Pair<>("IntentExtra", "IntentObject"),
                        new Pair<>("IntentExtra2", "IntentObject2"))
                .jump();

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        // The appCompat library checks if it needs to send the shared views, but in this case
        // the view does not exist in any activity and we get null as Options.
        verify(mActivitySpy, times(1)).startActivity(intentCaptor.capture(), isNull());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mActivitySpy.getPackageName());
        assertThat(intent.getComponent().getClassName()).isEqualTo(WolmoActivity.class.getCanonicalName());
        assertThat(intent.getStringExtra("ExtraTag")).isEqualTo("ExtraObject");
        assertThat(intent.getStringExtra("IntentExtra")).isEqualTo("IntentObject");
        assertThat(intent.getStringExtra("IntentExtra2")).isEqualTo("IntentObject2");
    }
}
