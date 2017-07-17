package ar.com.wolox.wolmo.core.activity;

import org.junit.Before;

public class WolmoActivityTest {

    private WolmoActivity mWolmoActivity;

    @Before
    protected void beforeTests() {
        mWolmoActivity = new WolmoActivity() {
            @Override
            protected int layout() {
                return 0;
            }

            @Override
            protected void init() {}
        };
    }



}
