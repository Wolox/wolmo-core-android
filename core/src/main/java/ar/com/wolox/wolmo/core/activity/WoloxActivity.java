package ar.com.wolox.wolmo.core.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.Toast;

import ar.com.wolox.wolmo.core.R;
import butterknife.ButterKnife;

public abstract class WoloxActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout());
        ButterKnife.bind(this);
        if (handleArguments(getIntent().getExtras())) {
            setUi();
            init();
            populate();
            setListeners();
        } else {
            showToast(R.string.unknown_error);
            finish();
        }
    }

    protected abstract int layout();

    /**
     * Reads arguments sent as a Bundle and saves them as appropriate.
     *
     * @param args The bundle obtainable by the getExtras method of the intent.
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    protected boolean handleArguments(Bundle args) {
        return true;
    }

    /**
     * Loads the view elements for the activity
     */
    protected void setUi() {
        // Do nothing, ButterKnife does this for us now!
    }

    /**
     * Initializes any variables that the activity needs
     */
    protected abstract void init();

    /**
     * Populates the view elements of the activity
     */
    protected void populate() {
        // Do nothing, override if needed!
    }

    /**
     * Sets the listeners for the views of the activity
     */
    protected void setListeners() {
        // Do nothing, override if needed!
    }

    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    protected void replaceFragment(int resId, Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, f)
                .commit();
    }

    protected void replaceFragment(int resId, Fragment f, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, f, tag)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}