package ar.com.wolox.wolmo.core.filesystem

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

abstract class FileFactory {
    abstract fun createWithWriter(
        componentActivity: ComponentActivity ,
        outputStreamWriter: (FileOutputStream) -> Unit ,
        intentDecorator: (Intent) -> Intent
    )

    protected open fun alterDocument(uri: Uri, componentActivity: ComponentActivity, outputStreamWriter: (FileOutputStream) -> Unit) {
        try {
            componentActivity.contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                    outputStreamWriter(fileOutputStream)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val PLAIN_TEXT = "text/plain"
    }
}
