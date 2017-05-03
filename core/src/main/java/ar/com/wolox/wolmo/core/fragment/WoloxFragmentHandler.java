package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.ToastUtils;

import butterknife.ButterKnife;

/**
 * This class is used to separate Wolox Fragments logic so that different subclasses of
 * Fragment can implement MVP without re-writing this.
 */
class WoloxFragmentHandler<T extends BasePresenter> {

    private static final String TAG = "WoloxFragmentHandler";

    private final Fragment mFragment;
    private final IWoloxFragment<T> mWoloxFragment;
    private boolean mCreated;
    private T mPresenter;
    private boolean mMenuVisible;
    private boolean mVisible;

    WoloxFragmentHandler(@NonNull IWoloxFragment<T> woloxFragment) {
        if (!(woloxFragment instanceof Fragment)) {
            throw new IllegalArgumentException("WoloxFragment should be a Fragment instance");
        }
        mFragment = (Fragment) woloxFragment;
        mWoloxFragment = woloxFragment;
    }

    /**
     * Method called from {@link WoloxFragment#onCreate(Bundle)}, it calls to {@link
     * WoloxFragment#handleArguments(Bundle)}
     * to check if the fragment has the correct arguments and creates a presenter calling {@link
     * WoloxFragment#createPresenter()}.
     *
     * @param savedInstanceState Saved instance state
     */
    void onCreate(@Nullable Bundle savedInstanceState) {
        if (mWoloxFragment.handleArguments(mFragment.getArguments())) {
            mPresenter = mWoloxFragment.createPresenter();
        } else {
            Log.e(TAG, mFragment.getClass().getSimpleName() +
                " - The fragment's handleArguments returned false.");
            ToastUtils.showToast(R.string.unknown_error);
            mFragment.getActivity().finish();
        }
    }

    /**
     * Method called from {@link WoloxFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}, it
     * creates the view defined in
     * {@link WoloxFragment#layout()} and binds it with the fragment with {@link ButterKnife}.
     * Then it calls the following methods and returns the {@link View} created:
     * <p><ul>
     * <li>{@link WoloxFragment#setUi(View)}
     * <li>{@link WoloxFragment#init()}
     * <li>{@link WoloxFragment#populate()}
     * <li>{@link WoloxFragment#setListeners()}
     * </ul><p>
     */
    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(mWoloxFragment.layout(), container, false);
        ButterKnife.bind(mFragment, v);
        mWoloxFragment.setUi(v);
        mWoloxFragment.init();
        mWoloxFragment.populate();
        mWoloxFragment.setListeners();

        mCreated = true;
        return v;
    }

    /**
     * Method called from {@link WoloxFragment#onViewCreated(View, Bundle)}, it notifies the {@link
     * BasePresenter} that the view was created calling {@link BasePresenter#onViewCreated()}.
     */
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getPresenter() != null) {
            getPresenter().onViewCreated();
        }
    }

    /**
     * Returns the presenter {@link T} for this fragment
     *
     * @return presenter
     */
    @Nullable
    T getPresenter() {
        return mPresenter;
    }

    private void onVisibilityChanged() {
        if (!mCreated) return;
        if (mFragment.isResumed() && mMenuVisible && !mVisible) {
            mWoloxFragment.onVisible();
            mVisible = true;
        } else if ((!mMenuVisible || !mFragment.isResumed()) && mVisible) {
            mWoloxFragment.onHide();
            mVisible = false;
        }
    }

    /**
     * Called from {@link WoloxFragment#onResume()}, checks visibility of the fragment
     * and calls {@link WoloxFragment#onVisible()} or {@link WoloxFragment#onHide()} accordingly.
     */
    void onResume() {
        onVisibilityChanged();
    }

    /**
     * Called from {@link WoloxFragment#onPause()}, checks visibility of the fragment
     * and calls {@link WoloxFragment#onVisible()} or {@link WoloxFragment#onHide()} accordingly.
     */
    void onPause() {
        onVisibilityChanged();
    }

    /**
     * Called from {@link WoloxFragment#setMenuVisibility(boolean)}, checks visibility of the
     * fragment's menu and calls {@link WoloxFragment#onVisible()} or
     * {@link WoloxFragment#onHide()} accordingly.
     * For mor info {@see Fragment#setMenuVisibility(boolean)}.
     */
    void setMenuVisibility(boolean visible) {
        mMenuVisible = visible;
        onVisibilityChanged();
    }

    /**
     * Called from {@link WoloxFragment#onDestroyView()}, it notifies the {@link BasePresenter} that
     * the view is destroyed, calling {@link BasePresenter#onViewDestroyed()}
     */
    void onDestroyView() {
        if (getPresenter() != null) {
            getPresenter().onViewDestroyed();
        }
    }

    /**
     * Called from {@link WoloxFragment#onDestroy()}. It calls {@link
     * BasePresenter#detachView()}
     */
    void onDestroy() {
        if (getPresenter() != null) getPresenter().detachView();
    }
}
