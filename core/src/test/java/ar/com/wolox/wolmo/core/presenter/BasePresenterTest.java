package ar.com.wolox.wolmo.core.presenter;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class BasePresenterTest {

    private BasePresenter<Object> mBasePresenter;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTests() {
        mBasePresenter = spy(BasePresenter.class);
    }


    @Test
    public void viewAttachedUpdates() {
        assertThat(mBasePresenter.isViewAttached()).isEqualTo(false);

        mBasePresenter.attachView(new Object());
        assertThat(mBasePresenter.isViewAttached()).isEqualTo(true);

        mBasePresenter.detachView();
        assertThat(mBasePresenter.isViewAttached()).isEqualTo(false);
    }

    @Test
    public void presenterNotifiesChildClasses() {
        // Attach and detach the view
        mBasePresenter.attachView(new Object());
        verify(mBasePresenter, times(1)).onViewAttached();
        verify(mBasePresenter, times(0)).onViewDetached();

        mBasePresenter.detachView();
        verify(mBasePresenter, times(1)).onViewDetached();
    }

    @Test
    public void presenterRunIfViewAttachedRunnable() {
        Runnable runnableMock = mock(Runnable.class);

        mBasePresenter.runIfViewAttached(runnableMock);
        verify(runnableMock, times(0)).run();

        // Now the view is attached
        mBasePresenter.attachView(new Object());
        mBasePresenter.runIfViewAttached(runnableMock);
        verify(runnableMock, times(1)).run();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void presenterRunIfViewAttachedConsumer() {
        BasePresenter.Consumer<Object> consumerMock = mock(BasePresenter.Consumer.class);

        mBasePresenter.runIfViewAttached(consumerMock);
        verify(consumerMock, times(0)).accept(any(Object.class));

        // Now the view is attached
        mBasePresenter.attachView(new Object());
        mBasePresenter.runIfViewAttached(consumerMock);
        verify(consumerMock, times(1)).accept(any(Object.class));
    }

}
