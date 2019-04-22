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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;

import android.view.View;

import ar.com.wolox.wolmo.core.activity.WolmoActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class NavigationUtilsTest {

    private Context mContextSpy;
    private Activity mActivitySpy;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mActivitySpy = spy(Robolectric.buildActivity(Activity.class).create().start().get());
        mContextSpy = spy(RuntimeEnvironment.application);
    }

    @Test
    public void openBrowserWithUrlShouldStartActivity() {
        NavigationUtils.openBrowser(mContextSpy, "http://google.com");

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getData().toString()).isEqualTo("http://google.com");
        assertThat(intent.getAction()).isEqualTo(Intent.ACTION_VIEW);
    }

    @Test
    public void jumpToShouldStartActivity() {
        NavigationUtils.jumpTo(mContextSpy, WolmoActivity.class);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mContextSpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());
    }

    @Test
    public void jumpToWithExtrasShouldStartActivity() {
        NavigationUtils.IntentExtra extra = new NavigationUtils.IntentExtra("Tag", "Value");

        NavigationUtils.jumpTo(mContextSpy, WolmoActivity.class, extra);

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextSpy, times(1)).startActivity(intentCaptor.capture());

        Intent intent = intentCaptor.getValue();
        assertThat(intent.getComponent().getPackageName()).isEqualTo(mContextSpy.getPackageName());
        assertThat(intent.getComponent().getClassName())
                .isEqualTo(WolmoActivity.class.getCanonicalName());
        assertThat(intent.getStringExtra("Tag")).isEqualTo("Value");
    }

    @Test
    public void jumpToClearingTaskShouldAddFlags() {
        NavigationUtils.jumpToClearingTask(mContextSpy, WolmoActivity.class);

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
        NavigationUtils.IntentExtra extra = new NavigationUtils.IntentExtra("Tag", "Value");

        NavigationUtils.jumpToClearingTask(mContextSpy, WolmoActivity.class, extra);

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

        NavigationUtils.jumpToWithAnimation(mActivitySpy, WolmoActivity.class, optionsCompatMock);

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
                .addIntentObjects(new NavigationUtils.IntentExtra("IntentExtra", "IntentObject"),
                        new NavigationUtils.IntentExtra("IntentExtra2", "IntentObject2"))
                .jump();

        ArgumentCaptor<Intent> intentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mActivitySpy, times(1)).startActivity(intentCaptor.capture());

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
                .addIntentObjects(new NavigationUtils.IntentExtra("IntentExtra", "IntentObject"),
                        new NavigationUtils.IntentExtra("IntentExtra2", "IntentObject2"))
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
