package ar.com.wolox.wolmo.core.di.modules;

import static org.assertj.core.api.Java6Assertions.assertThat;

import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.SparseArray;

import ar.com.wolox.wolmo.core.permission.PermissionListener;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class DefaultModuleTest {

    @Test
    public void provideFragmentsAndTitlesShouldReturnNewArrayList() {
        ArrayList<Pair<Fragment, String>> list1 = DefaultModule.provideFragmentsAndTitles();
        ArrayList<Pair<Fragment, String>> list2 = DefaultModule.provideFragmentsAndTitles();

        assertThat(list1).isNotNull().isNotSameAs(list2);
        assertThat(list1).isEmpty();
    }

    @Test
    public void providePermissionManagerShouldReturnNewArray() {
        SparseArray<PermissionListener> array1 = DefaultModule.providesPermissionManagerArray();
        SparseArray<PermissionListener> array2 = DefaultModule.providesPermissionManagerArray();

        assertThat(array1).isNotNull().isNotSameAs(array2);
    }

    @Test
    public void provideBasePresenterShouldReturnNewInstance() {
        BasePresenter basePresenter = DefaultModule.providesBasePresenter();
        BasePresenter basePresenter2 = DefaultModule.providesBasePresenter();

        assertThat(basePresenter).isNotNull().isNotSameAs(basePresenter2);
    }
}
