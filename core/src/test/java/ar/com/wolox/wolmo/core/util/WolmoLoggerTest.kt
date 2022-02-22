package ar.com.wolox.wolmo.core.util

import android.os.Build
import ar.com.wolox.wolmo.core.util.WolmoLogger.error
import ar.com.wolox.wolmo.core.util.WolmoLogger.info
import ar.com.wolox.wolmo.core.util.WolmoLogger.log
import ar.com.wolox.wolmo.core.util.WolmoLogger.terribleFailure
import ar.com.wolox.wolmo.core.util.WolmoLogger.verbose
import ar.com.wolox.wolmo.core.util.WolmoLogger.warn
import java.io.PrintStream
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
class WolmoLoggerTest {

    private lateinit var printStreamMock: PrintStream
    private lateinit var exceptionMock: Exception

    @Before
    fun setUp() {
        WolmoLogger.resetTag()
        printStreamMock = Mockito.mock(PrintStream::class.java)
        ShadowLog.stream = printStreamMock
        exceptionMock = Mockito.mock(Exception::class.java)
        Mockito.doAnswer { invocation: InvocationOnMock ->
            val stream = invocation.getArgument<PrintStream>(0)
            stream.println("Stacktrace")
            return@doAnswer false
        }.`when`(exceptionMock).printStackTrace(
            ArgumentMatchers.any(
                PrintStream::class.java
            )
        )
    }

    @Test
    fun logInDebugLevelShouldCallAndroidLog() {
        log { "First string" }
        WolmoLogger.tag = "TestTag"
        log { "Second string" }
        val inOrder = Mockito.inOrder(printStreamMock)
        inOrder.verify(printStreamMock).println(eq("D/WolmoCoreAndroid: First string"))
        inOrder.verify(printStreamMock).println(eq("D/TestTag: Second string"))
    }

    @Test
    fun logInInfoLevelShouldCallAndroidLog() {
        info { "First string" }
        WolmoLogger.tag = "TestTag"
        info { "Second string" }
        val inOrder = Mockito.inOrder(printStreamMock)
        inOrder.verify(printStreamMock).println(eq("I/WolmoCoreAndroid: First string"))
        inOrder.verify(printStreamMock).println(eq("I/TestTag: Second string"))
    }

    @Test
    fun logInWarningLevelShouldCallAndroidLog() {
        warn { "First string" }
        WolmoLogger.tag = "TestTag"
        warn { "Second string" }
        val inOrder = Mockito.inOrder(printStreamMock)
        inOrder.verify(printStreamMock).println(eq("W/WolmoCoreAndroid: First string"))
        inOrder.verify(printStreamMock).println(eq("W/TestTag: Second string"))
    }

    @Test
    fun logInErrorLevelShouldCallAndroidLog() {
        error { "First string" }
        WolmoLogger.tag = "TestTag"
        error { "Second string" }
        val inOrder = Mockito.inOrder(printStreamMock)
        inOrder.verify(printStreamMock).println(eq("E/WolmoCoreAndroid: First string"))
        inOrder.verify(printStreamMock).println(eq("E/TestTag: Second string"))
    }

    @Test
    fun logInVerboseLevelShouldCallAndroidLog() {
        verbose { "First string" }
        WolmoLogger.tag = "TestTag"
        verbose { "Second string" }
        val inOrder = Mockito.inOrder(printStreamMock)
        inOrder.verify(printStreamMock).println(eq("V/WolmoCoreAndroid: First string"))
        inOrder.verify(printStreamMock).println(eq("V/TestTag: Second string"))
    }

    @Test
    fun logInTerribleFailureLevelShouldCallAndroidLog() {
        terribleFailure { "First string" }
        WolmoLogger.tag = "TestTag"
        terribleFailure { "Second string" }
        val inOrder = Mockito.inOrder(printStreamMock)
        inOrder.verify(printStreamMock).println(eq("A/WolmoCoreAndroid: First string"))
        inOrder.verify(printStreamMock).println(eq("A/TestTag: Second string"))
    }
}
