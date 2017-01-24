package ar.com.wolox.wolmo.core.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

public abstract class WoloxFragment<T extends BasePresenter> extends Fragment
        implements IWoloxFragment<T> {

    private WoloxFragmentHandler<T> mFragmentHandler = new WoloxFragmentHandler<T>(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mFragmentHandler.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        mFragmentHandler.setMenuVisibility(visible);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentHandler.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFragmentHandler.onPause();
    }

    @Override
    public void onDestroyView() {
        mFragmentHandler.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance()
                .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean handleArguments(Bundle arguments) {
        return true;
    }

    public void setUi(View v) {
    }

    public void setListeners() {
    }

    @Override
    public void populate() {
    }

    protected T getPresenter() {
        return mFragmentHandler.getPresenter();
    }

    @Override
    public void onVisible() {
    }

    @Override
    public void onHide() {
    }

    /**
     * @see IWoloxFragment#onBackPressed()
     *
     * Beware, when overriding, that returning 'true' will prevent default navigation behaviour such
     * as {@link FragmentManager#popBackStackImmediate()} or {@link Activity#finish()}, but not
     * dismissing the keyboard, for example.
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }
}