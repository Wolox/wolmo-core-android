package ar.com.wolox.wolmo.core.util


import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import java.io.PrintStream

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoggerTest {

    private var mLogger: Logger? = null
    private var mPrintStreamMock: PrintStream? = null
    private var mExceptionMock: Throwable? = null


    @Before
    fun beforeTest() {
        mLogger = Logger()
        mPrintStreamMock = mock<PrintStream>(PrintStream::class.java)
        ShadowLog.stream = mPrintStreamMock

        mExceptionMock = mock<Throwable>(Throwable::class.java)
        doAnswer { invocation ->
            val stream = invocation.getArgument<PrintStream>(0)
            stream.println("Stacktrace")
            false
        }.`when`(mExceptionMock)?.printStackTrace(any<PrintStream>(PrintStream::class.java))
    }

    @Test
    fun logVerboseShouldCallAndroidLog() {
        mLogger?.setTag("TagVerbose")
        mLogger?.v("Log")
        mLogger?.v("Tag2", "Log2")
        mExceptionMock?.let {
            mLogger?.v("ErrMsg", it)
            mLogger?.v("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(mPrintStreamMock)

        inOrder.verify(mPrintStreamMock)?.println(eq("V/TagVerbose: Log"))
        inOrder.verify(mPrintStreamMock)?.println(eq("V/Tag2: Log2"))
        inOrder.verify(mPrintStreamMock)?.println(eq("V/TagVerbose: ErrMsg"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(mPrintStreamMock)?.println(eq("V/TagErr: Error"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logDebugShouldCallAndroidLog() {
        mLogger?.setTag("TagDebug")
        mLogger?.d("Log")
        mLogger?.d("Tag2", "Log2")
        mExceptionMock?.let {
            mLogger?.d("ErrMsg", it)
            mLogger?.d("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(mPrintStreamMock)

        inOrder.verify(mPrintStreamMock)?.println(eq("D/TagDebug: Log"))
        inOrder.verify(mPrintStreamMock)?.println(eq("D/Tag2: Log2"))
        inOrder.verify(mPrintStreamMock)?.println(eq("D/TagDebug: ErrMsg"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(mPrintStreamMock)?.println(eq("D/TagErr: Error"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logInfoShouldCallAndroidLog() {
        mLogger?.setTag("TagInfo")
        mLogger?.i("Log")
        mLogger?.i("Tag2", "Log2")
        mExceptionMock?.let {
            mLogger?.i("ErrMsg", it)
            mLogger?.i("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(mPrintStreamMock)

        inOrder.verify(mPrintStreamMock)?.println(eq("I/TagInfo: Log"))
        inOrder.verify(mPrintStreamMock)?.println(eq("I/Tag2: Log2"))
        inOrder.verify(mPrintStreamMock)?.println(eq("I/TagInfo: ErrMsg"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(mPrintStreamMock)?.println(eq("I/TagErr: Error"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logWarningShouldCallAndroidLog() {
        mLogger?.setTag("TagWarning")
        mLogger?.w("Log")
        mLogger?.w("Tag2", "Log2")
        mExceptionMock?.let {
            mLogger?.w("ErrMsg", it)
            mLogger?.w("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(mPrintStreamMock)

        inOrder.verify(mPrintStreamMock)?.println(eq("W/TagWarning: Log"))
        inOrder.verify(mPrintStreamMock)?.println(eq("W/Tag2: Log2"))
        inOrder.verify(mPrintStreamMock)?.println(eq("W/TagWarning: ErrMsg"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(mPrintStreamMock)?.println(eq("W/TagErr: Error"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logErrorShouldCallAndroidLog() {
        mLogger?.setTag("TagError")
        mLogger?.e("Log")
        mLogger?.e("Tag2", "Log2")
        mExceptionMock?.let {
            mLogger?.e("ErrMsg", it)
            mLogger?.e("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(mPrintStreamMock)

        inOrder.verify(mPrintStreamMock)?.println(eq("E/TagError: Log"))
        inOrder.verify(mPrintStreamMock)?.println(eq("E/Tag2: Log2"))
        inOrder.verify(mPrintStreamMock)?.println(eq("E/TagError: ErrMsg"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(mPrintStreamMock)?.println(eq("E/TagErr: Error"))
        inOrder.verify(mPrintStreamMock)?.println(eq("Stacktrace"))
    }
}