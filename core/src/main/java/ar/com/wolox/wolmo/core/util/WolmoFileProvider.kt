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
package ar.com.wolox.wolmo.core.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Utils class for managing [File]s.
 */
@ApplicationScope
class WolmoFileProvider @Inject constructor(private val context: Context) {

    /**
     * Returns a new empty file named [name].[extension]. The given [extension] could
     * end with a dot or not. If not it'll be added.
     * The new file will be saved on a [directoryType]. Use [Environment] to see the available
     * directories.
     * It'll throws an [IOException] if the file could not be created.
     */
    @Throws(IOException::class)
    fun createTempFile(name: String, extension: String, directoryType: String): File {
        val storageDir = Environment.getExternalStoragePublicDirectory(directoryType)
        val ext = if (extension.startsWith(".")) extension else ".$extension"
        return File.createTempFile(name, ext, storageDir)
    }

    /**
     * Returns the [Uri] of the given [file] using a file provider.
     * This method wraps [FileProvider.getUriForFile] building the provider name provided with
     * Wolmo and the application context.
     */
    fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }

    /** Returns the physical path to a stored File by providing a [uri] of a content provider. */
    fun getRealPathFromUri(uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, null, null, null, null)
            return cursor?.run {
                val columnIndex = getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                moveToFirst()
                getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
    }
}
