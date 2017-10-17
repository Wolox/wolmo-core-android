package ar.com.wolox.wolmo.core.di.modules;



import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.SparseArray;

import net.bytebuddy.matcher.ModifierMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
