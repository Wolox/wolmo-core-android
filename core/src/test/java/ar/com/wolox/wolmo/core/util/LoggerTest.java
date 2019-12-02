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
package ar.com.wolox.wolmo.core.util;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import androidx.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.PrintStream;

import ar.com.wolox.wolmo.core.util.Logger;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LoggerTest {

    private Logger mLogger;
    private PrintStream mPrintStreamMock;
    private Exception mExceptionMock;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mLogger = new Logger();
        mPrintStreamMock = mock(PrintStream.class);
        ShadowLog.stream = mPrintStreamMock;

        mExceptionMock = mock(Exception.class);
        doAnswer(invocation -> {
            PrintStream stream = invocation.getArgument(0);
            stream.println("Stacktrace");
            return false;
        }).when(mExceptionMock).printStackTrace(any(PrintStream.class));
    }

    @Test
    public void logVerboseShouldCallAndroidLog() {
        mLogger.setTag("TagVerbose");
        mLogger.v("Log");
        mLogger.v("Tag2", "Log2");
        mLogger.v("ErrMsg", mExceptionMock);
        mLogger.v("TagErr", "Error", mExceptionMock);

        InOrder inOrder = Mockito.inOrder(mPrintStreamMock);

        inOrder.verify(mPrintStreamMock).println(eq("V/TagVerbose: Log"));
        inOrder.verify(mPrintStreamMock).println(eq("V/Tag2: Log2"));
        inOrder.verify(mPrintStreamMock).println(eq("V/TagVerbose: ErrMsg"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
        inOrder.verify(mPrintStreamMock).println(eq("V/TagErr: Error"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
    }

    @Test
    public void logDebugShouldCallAndroidLog() {
        mLogger.setTag("TagDebug");
        mLogger.d("Log");
        mLogger.d("Tag2", "Log2");
        mLogger.d("ErrMsg", mExceptionMock);
        mLogger.d("TagErr", "Error", mExceptionMock);

        InOrder inOrder = Mockito.inOrder(mPrintStreamMock);

        inOrder.verify(mPrintStreamMock).println(eq("D/TagDebug: Log"));
        inOrder.verify(mPrintStreamMock).println(eq("D/Tag2: Log2"));
        inOrder.verify(mPrintStreamMock).println(eq("D/TagDebug: ErrMsg"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
        inOrder.verify(mPrintStreamMock).println(eq("D/TagErr: Error"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
    }

    @Test
    public void logInfoShouldCallAndroidLog() {
        mLogger.setTag("TagInfo");
        mLogger.i("Log");
        mLogger.i("Tag2", "Log2");
        mLogger.i("ErrMsg", mExceptionMock);
        mLogger.i("TagErr", "Error", mExceptionMock);

        InOrder inOrder = Mockito.inOrder(mPrintStreamMock);

        inOrder.verify(mPrintStreamMock).println(eq("I/TagInfo: Log"));
        inOrder.verify(mPrintStreamMock).println(eq("I/Tag2: Log2"));
        inOrder.verify(mPrintStreamMock).println(eq("I/TagInfo: ErrMsg"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
        inOrder.verify(mPrintStreamMock).println(eq("I/TagErr: Error"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
    }

    @Test
    public void logWarningShouldCallAndroidLog() {
        mLogger.setTag("TagWarning");
        mLogger.w("Log");
        mLogger.w("Tag2", "Log2");
        mLogger.w("ErrMsg", mExceptionMock);
        mLogger.w("TagErr", "Error", mExceptionMock);

        InOrder inOrder = Mockito.inOrder(mPrintStreamMock);

        inOrder.verify(mPrintStreamMock).println(eq("W/TagWarning: Log"));
        inOrder.verify(mPrintStreamMock).println(eq("W/Tag2: Log2"));
        inOrder.verify(mPrintStreamMock).println(eq("W/TagWarning: ErrMsg"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
        inOrder.verify(mPrintStreamMock).println(eq("W/TagErr: Error"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
    }

    @Test
    public void logErrorShouldCallAndroidLog() {
        mLogger.setTag("TagError");
        mLogger.e("Log");
        mLogger.e("Tag2", "Log2");
        mLogger.e("ErrMsg", mExceptionMock);
        mLogger.e("TagErr", "Error", mExceptionMock);

        InOrder inOrder = Mockito.inOrder(mPrintStreamMock);

        inOrder.verify(mPrintStreamMock).println(eq("E/TagError: Log"));
        inOrder.verify(mPrintStreamMock).println(eq("E/Tag2: Log2"));
        inOrder.verify(mPrintStreamMock).println(eq("E/TagError: ErrMsg"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
        inOrder.verify(mPrintStreamMock).println(eq("E/TagErr: Error"));
        inOrder.verify(mPrintStreamMock).println(eq("Stacktrace"));
    }
}
