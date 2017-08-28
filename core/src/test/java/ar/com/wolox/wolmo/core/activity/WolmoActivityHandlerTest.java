package ar.com.wolox.wolmo.core.activity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ar.com.wolox.wolmo.core.util.provider.ToastProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ButterKnife.class})
public class WolmoActivityHandlerTest {

    private ToastProvider mToastProvider;
    private WolmoActivity mWolmoActivity;
    private WolmoActivityHandler mWolmoActivityHandler;

    @Before
    public void beforeTest() {
        mWolmoActivity = mock(WolmoActivity.class);
        mToastProvider = mock(ToastProvider.class);

        mWolmoActivityHandler = new WolmoActivityHandler(mToastProvider);

        // Mock calls to android.util.Log
        PowerMockito.mockStatic(ButterKnife.class);
    }

    @Test
    public void onCreateShouldCallWolmoMethods() {
        when(mWolmoActivity.handleArguments(isNull(Bundle.class))).thenReturn(true);
        when(mWolmoActivity.getIntent()).thenReturn(mock(Intent.class));

        mWolmoActivityHandler.onCreate(mWolmoActivity, null);

        // Verify that the methods in wolmoFragment are called in order
        InOrder inOrder = Mockito.inOrder(mWolmoActivity);

        inOrder.verify(mWolmoActivity, times(1)).handleArguments(null);
        inOrder.verify(mWolmoActivity, times(1)).setUi();
        inOrder.verify(mWolmoActivity, times(1)).init();
        inOrder.verify(mWolmoActivity, times(1)).populate();
        inOrder.verify(mWolmoActivity, times(1)).setListeners();
    }

    @Test
    public void onDestroyShouldCallUnbind() {
        Unbinder unbinderMock = mock(Unbinder.class);
        when(ButterKnife.bind(any(Activity.class))).thenReturn(unbinderMock);
        when(mWolmoActivity.handleArguments(isNull(Bundle.class))).thenReturn(true);
        when(mWolmoActivity.getIntent()).thenReturn(mock(Intent.class));

        mWolmoActivityHandler.onCreate(mWolmoActivity, null);
        mWolmoActivityHandler.onDestroy();

        verify(unbinderMock, times(1)).unbind();
    }
}
