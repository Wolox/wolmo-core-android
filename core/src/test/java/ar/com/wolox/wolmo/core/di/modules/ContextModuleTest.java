package ar.com.wolox.wolmo.core.di.modules;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

public class ContextModuleTest {

    private ContextModule mContextModule;

    @Before
    public void beforeTest() {
        mContextModule = new ContextModule();
    }

    @Test
    public void provideContextShouldGetApplicationContext() {
        Application app = mock(Application.class);
        Context context = mock(Context.class);
        when(app.getApplicationContext()).thenReturn(context);

        assertThat(mContextModule.provideContext(app)).isEqualTo(context);
    }

    @Test
    public void provideSharedPreferencesShouldOpenThemInPrivateModeWithGivenName() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);

        assertThat(mContextModule.provideSharedPreferences("Name", context)).isEqualTo(
            sharedPreferences);
        verify(context, times(1)).getSharedPreferences(eq("Name"), eq(Activity.MODE_PRIVATE));
    }

}
