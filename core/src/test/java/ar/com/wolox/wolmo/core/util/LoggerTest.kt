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

    private var logger: Logger? = null
    private var printStreamMock: PrintStream? = null
    private var exceptionMock: Throwable? = null


    @Before
    fun beforeTest() {
        logger = Logger()
        printStreamMock = mock<PrintStream>(PrintStream::class.java)
        ShadowLog.stream = printStreamMock

        exceptionMock = mock<Throwable>(Throwable::class.java)
        doAnswer { invocation ->
            val stream = invocation.getArgument<PrintStream>(0)
            stream.println("Stacktrace")
            false
        }.`when`(exceptionMock)?.printStackTrace(any<PrintStream>(PrintStream::class.java))
    }

    @Test
    fun logVerboseShouldCallAndroidLog() {
        logger?.setTag("TagVerbose")
        logger?.v("Log")
        logger?.v("Tag2", "Log2")
        exceptionMock?.let {
            logger?.v("ErrMsg", it)
            logger?.v("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(printStreamMock)

        inOrder.verify(printStreamMock)?.println(eq("V/TagVerbose: Log"))
        inOrder.verify(printStreamMock)?.println(eq("V/Tag2: Log2"))
        inOrder.verify(printStreamMock)?.println(eq("V/TagVerbose: ErrMsg"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(printStreamMock)?.println(eq("V/TagErr: Error"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logDebugShouldCallAndroidLog() {
        logger?.setTag("TagDebug")
        logger?.d("Log")
        logger?.d("Tag2", "Log2")
        exceptionMock?.let {
            logger?.d("ErrMsg", it)
            logger?.d("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(printStreamMock)

        inOrder.verify(printStreamMock)?.println(eq("D/TagDebug: Log"))
        inOrder.verify(printStreamMock)?.println(eq("D/Tag2: Log2"))
        inOrder.verify(printStreamMock)?.println(eq("D/TagDebug: ErrMsg"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(printStreamMock)?.println(eq("D/TagErr: Error"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logInfoShouldCallAndroidLog() {
        logger?.setTag("TagInfo")
        logger?.i("Log")
        logger?.i("Tag2", "Log2")
        exceptionMock?.let {
            logger?.i("ErrMsg", it)
            logger?.i("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(printStreamMock)

        inOrder.verify(printStreamMock)?.println(eq("I/TagInfo: Log"))
        inOrder.verify(printStreamMock)?.println(eq("I/Tag2: Log2"))
        inOrder.verify(printStreamMock)?.println(eq("I/TagInfo: ErrMsg"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(printStreamMock)?.println(eq("I/TagErr: Error"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logWarningShouldCallAndroidLog() {
        logger?.setTag("TagWarning")
        logger?.w("Log")
        logger?.w("Tag2", "Log2")
        exceptionMock?.let {
            logger?.w("ErrMsg", it)
            logger?.w("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(printStreamMock)

        inOrder.verify(printStreamMock)?.println(eq("W/TagWarning: Log"))
        inOrder.verify(printStreamMock)?.println(eq("W/Tag2: Log2"))
        inOrder.verify(printStreamMock)?.println(eq("W/TagWarning: ErrMsg"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(printStreamMock)?.println(eq("W/TagErr: Error"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
    }

    @Test
    fun logErrorShouldCallAndroidLog() {
        logger?.setTag("TagError")
        logger?.e("Log")
        logger?.e("Tag2", "Log2")
        exceptionMock?.let {
            logger?.e("ErrMsg", it)
            logger?.e("TagErr", "Error", it)
        }

        val inOrder = Mockito.inOrder(printStreamMock)

        inOrder.verify(printStreamMock)?.println(eq("E/TagError: Log"))
        inOrder.verify(printStreamMock)?.println(eq("E/Tag2: Log2"))
        inOrder.verify(printStreamMock)?.println(eq("E/TagError: ErrMsg"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
        inOrder.verify(printStreamMock)?.println(eq("E/TagErr: Error"))
        inOrder.verify(printStreamMock)?.println(eq("Stacktrace"))
    }
}