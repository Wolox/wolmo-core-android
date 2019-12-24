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
package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.Logger;
import ar.com.wolox.wolmo.core.util.ToastFactory;

/**
 * This class is used to separate Wolox Fragments logic so that different subclasses of
 * Fragment can implement MVP without re-writing this.
 */
public final class WolmoFragmentHandler<T extends BasePresenter> {

	private Fragment mFragment;
	private IWolmoFragment mWolmoFragment;

	private boolean mCreated;
	private boolean mMenuVisible;
	private boolean mVisible;

	private @NonNull T mPresenter;
	private ToastFactory mToastFactory;
	private Logger mLogger;

	@Inject
	public WolmoFragmentHandler(@NonNull ToastFactory toastFactory,
	                     @NonNull Logger logger,
	                     @NonNull T presenter) {
		mPresenter = presenter;
		mToastFactory = toastFactory;
		mLogger = logger;
	}

	/**
	 * Sets the fragment for this fragment handler.
	 *
	 * @param wolmoFragment Wolox fragment to attach.
	 */
	private void setFragment(@NonNull IWolmoFragment wolmoFragment) {
		if (!(wolmoFragment instanceof Fragment)) {
			throw new IllegalArgumentException("WolmoFragment should be a Fragment instance");
		}
		mFragment = (Fragment) wolmoFragment;
		mWolmoFragment = wolmoFragment;
	}

	/**
	 * Method called from {@link WolmoFragment#onCreate(Bundle)}, it calls to {@link
	 * WolmoFragment#handleArguments(Bundle)} to check if the fragment has the correct arguments.
	 *
	 */
	void onCreate(@NonNull IWolmoFragment wolmoFragment) {
		mLogger.setTag(WolmoFragmentHandler.class.getSimpleName());
		setFragment(wolmoFragment);
		final @Nullable Boolean handleArgs = mWolmoFragment.handleArguments(mFragment.getArguments());
		if (handleArgs == null || !handleArgs) {
			mLogger.e(mFragment.getClass().getSimpleName() +
					" - The fragment's handleArguments() returned false.");
			mToastFactory.show(R.string.unknown_error);
			mFragment.requireActivity().finish();
		}
	}

	/**
	 * Method called from {@link WolmoFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}, it
	 * creates the view defined in {@link WolmoFragment#layout()}.
	 * Then it calls the following methods and returns the {@link View} created:
	 * <p><ul>
	 * <li>{@link WolmoFragment#setUi(View)}
	 * <li>{@link WolmoFragment#init()}
	 * <li>{@link WolmoFragment#populate()}
	 * <li>{@link WolmoFragment#setListeners()}
	 * </ul><p>
	 */
	View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
		return inflater.inflate(mWolmoFragment.layout(), container, false);
	}

	/**
	 * Method called from {@link WolmoFragment#onViewCreated(View, Bundle)}, it attaches the
	 * fragment to the {@link BasePresenter} calling {@link BasePresenter#onViewAttached()}.
	 */
	@SuppressWarnings("unchecked")
	void onViewCreated(@NonNull View view) {
		mCreated = true;
		mPresenter.attachView(mWolmoFragment);
		mWolmoFragment.setUi(view);
		mWolmoFragment.init();
		mWolmoFragment.populate();
		mWolmoFragment.setListeners();
	}

	/**
	 * Returns the presenter {@link T} for the fragment
	 *
	 * @return presenter
	 */
	@NonNull
	public T getPresenter() {
		return mPresenter;
	}

	private void onVisibilityChanged() {
		if (!mCreated) return;
		if (mFragment.isResumed() && mMenuVisible && !mVisible) {
			mWolmoFragment.onVisible();
			mVisible = true;
		} else if ((!mMenuVisible || !mFragment.isResumed()) && mVisible) {
			mWolmoFragment.onHide();
			mVisible = false;
		}
	}

	/**
	 * Called from {@link WolmoFragment#onResume()}, checks visibility of the fragment
	 * and calls {@link WolmoFragment#onVisible()} or {@link WolmoFragment#onHide()} accordingly.
	 */
	void onResume() {
		onVisibilityChanged();
	}

	/**
	 * Called from {@link WolmoFragment#onPause()}, checks visibility of the fragment
	 * and calls {@link WolmoFragment#onVisible()} or {@link WolmoFragment#onHide()} accordingly.
	 */
	void onPause() {
		onVisibilityChanged();
	}

	/**
	 * Called from {@link WolmoFragment#setMenuVisibility(boolean)}, checks visibility of the
	 * fragment's menu and calls {@link WolmoFragment#onVisible()} or
	 * {@link WolmoFragment#onHide()} accordingly.
	 * For mor info {@see Fragment#setMenuVisibility(boolean)}.
	 */
	void setMenuVisibility(boolean visible) {
		mMenuVisible = visible;
		onVisibilityChanged();
	}

	/**
	 * Called from {@link WolmoFragment#onDestroyView()}, it notifies the {@link BasePresenter} that
	 * the view is destroyed, calling {@link BasePresenter#onViewDetached()}
	 */
	void onDestroyView() {
		getPresenter().detachView();
	}
}