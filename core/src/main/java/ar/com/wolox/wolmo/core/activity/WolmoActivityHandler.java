package ar.com.wolox.wolmo.core.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.util.ToastFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WolmoActivityHandler {

    private ToastFactory mToastFactory;
    private WolmoActivity mWolmoActivity;
    private Unbinder mUnbinder;

    @Inject
    public WolmoActivityHandler(ToastFactory toastFactory) {
        mToastFactory = toastFactory;
    }

    /**
     * Handles the custom lifecycle of Wolmo's Activity. It provides a set of callbacks to structure
     * the different aspects of the Activities initialization.
     * Also, it provides initialization for Butterknife.
     *
     * @param savedInstanceState a {@link Bundle} provided by Android's lifecycle.
     * @param activity the {@link WolmoActivity} to be managed by this handler.
     */
    protected void onCreate(@NonNull WolmoActivity activity, @Nullable Bundle savedInstanceState) {
        mWolmoActivity = activity;
        mWolmoActivity.setContentView(mWolmoActivity.layout());
        mUnbinder = ButterKnife.bind(mWolmoActivity);
        if (mWolmoActivity.handleArguments(mWolmoActivity.getIntent().getExtras())) {
            mWolmoActivity.setUi();
            mWolmoActivity.init();
            mWolmoActivity.populate();
            mWolmoActivity.setListeners();
        } else {
            mToastFactory.show(R.string.unknown_error);
            mWolmoActivity.finish();
        }
    }


    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
