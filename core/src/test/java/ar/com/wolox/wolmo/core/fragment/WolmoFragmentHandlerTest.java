package ar.com.wolox.wolmo.core.fragment;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import ar.com.wolox.wolmo.core.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class WolmoFragmentHandlerTest {

    @Rule public final ExpectedException exception = ExpectedException.none();

    private WolmoFragmentHandler<BasePresenter> mWolmoFragmentHandler;
    private WolmoFragment mWolmoFragment;
    private ToastUtils mToastUtils;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTests() {
        // Mock calls to android.util.Log
        PowerMockito.mockStatic(Log.class);

        mToastUtils = mock(ToastUtils.class);
        mWolmoFragment = mock(WolmoFragment.class);

        mWolmoFragmentHandler = new WolmoFragmentHandler<>();
        mWolmoFragmentHandler.mToastUtils = mToastUtils;
    }

    @Test
    public void setWolmoFragmentNotExtendingFragment() {
        IWolmoFragment wolmoFragment = mock(IWolmoFragment.class);
        exception.expect(IllegalArgumentException.class);
        mWolmoFragmentHandler.onCreate(wolmoFragment, null);
    }

    @Test
    public void handlerFinishActivityIfWrongArguments() {
        FragmentActivity activity = mock(FragmentActivity.class);

        when(mWolmoFragment.handleArguments(nullable(Bundle.class))).thenReturn(false);
        when(mWolmoFragment.getActivity()).thenReturn(activity);

        mWolmoFragmentHandler.onCreate(mWolmoFragment, new Bundle());
        verify(mToastUtils, times(1)).show(R.string.unknown_error);
        verify(activity, times(1)).finish();
    }

    @Test
    public void onViewCreatedCallsWolmoFragmentMethods() {
        View mockView = mock(View.class);
        when(mWolmoFragment.handleArguments(nullable(Bundle.class))).thenReturn(true);

        mWolmoFragmentHandler.onCreate(mWolmoFragment, new Bundle());
        mWolmoFragmentHandler.onViewCreated(mockView, null);

        // Verify that the methods in wolmoFragment are called in order
        InOrder inOrder = inOrder(mWolmoFragment);

        inOrder.verify(mWolmoFragment, times(1)).setUi(mockView);
        inOrder.verify(mWolmoFragment, times(1)).init();
        inOrder.verify(mWolmoFragment, times(1)).populate();
        inOrder.verify(mWolmoFragment, times(1)).setListeners();
    }

    @Test
    public void notifyFragmentOnVisibilityChanged() {
        mWolmoFragmentHandler.setMenuVisibility(true);

        when(mWolmoFragment.isResumed()).thenReturn(true);
        when(mWolmoFragment.handleArguments(nullable(Bundle.class))).thenReturn(true);

        mWolmoFragmentHandler.onCreate(mWolmoFragment, new Bundle());
        mWolmoFragmentHandler.onViewCreated(mock(View.class), null);
        mWolmoFragmentHandler.onResume();
        verify(mWolmoFragment, times(1)).onVisible();
        verify(mWolmoFragment, times(0)).onHide();

        // Hide the fragment
        when(mWolmoFragment.isResumed()).thenReturn(false);
        mWolmoFragmentHandler.onPause();
        verify(mWolmoFragment, times(1)).onHide();
    }

    @Test
    public void detachesPresenterOnDestroyView() {
        BasePresenter presenter = mock(BasePresenter.class);

        mWolmoFragmentHandler.mPresenter = presenter;

        mWolmoFragmentHandler.onDestroyView();
        verify(presenter, times(1)).detachView();
    }

}
