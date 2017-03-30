package ar.com.wolox.wolmo.core.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

import butterknife.ButterKnife;

public abstract class WoloxFragment<T extends BasePresenter> extends Fragment
    implements IWoloxFragment<T> {

    private WoloxFragmentHandler<T> mFragmentHandler = new WoloxFragmentHandler<T>(this);

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentHandler.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return mFragmentHandler.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentHandler.onViewCreated(view, savedInstanceState);
    }

    @Override
    @CallSuper
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        mFragmentHandler.setMenuVisibility(visible);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        mFragmentHandler.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        super.onPause();
        mFragmentHandler.onPause();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        mFragmentHandler.onDestroyView();
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance()
            .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean handleArguments(@Nullable Bundle arguments) {
        return true;
    }

    @Override
    public void setUi(View v) {}

    @Override
    public void setListeners() {}

    @Override
    public void populate() {}

    protected T getPresenter() {
        return mFragmentHandler.getPresenter();
    }

    @Override
    public void onVisible() {}

    @Override
    public void onHide() {}

    /**
     * @see IWoloxFragment#onBackPressed()
     * <p>
     * Beware, when overriding, that returning 'true' will prevent default navigation behaviour
     * such
     * as {@link FragmentManager#popBackStackImmediate()} or {@link Activity#finish()}, but not
     * dismissing the keyboard, for example.
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }
}