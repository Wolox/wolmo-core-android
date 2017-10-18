package ar.com.wolox.wolmo.core.di.modules;

import static org.assertj.core.api.Java6Assertions.assertThat;

import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DefaultModuleTest {

    private DefaultModule mDefaultModule;

    @Before
    public void beforeTest() {
        mDefaultModule = new DefaultModule();
    }

    @Test
    public void provideFragmentsAndTitlesShouldReturnNewArrayList() {
        ArrayList<Pair<Fragment, String>> list1 = mDefaultModule.provideFragmentsAndTitles();
        ArrayList<Pair<Fragment, String>> list2 = mDefaultModule.provideFragmentsAndTitles();

        assertThat(list1).isNotSameAs(list2);
        assertThat(list1).hasSize(0);
    }

}
