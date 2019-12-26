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

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Utils class for managing [File]s.
 */
@ApplicationScope
class WolmoFileProvider @Inject constructor(private val context: Context) {

    private val appName: String by lazy {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        if (stringId != 0) context.getString(stringId) else applicationInfo.nonLocalizedLabel?.toString() ?: ""
    }

    private var cacheFolder: String = getTmpDirectory(context).absolutePath

    private fun getTmpDirectory(context: Context) = File(context.cacheDir, appName).apply {
        if (!exists()) {
            mkdir()
        }
    }

    private fun getEnvironmentFilename(folder: String, name: String, extension: String, mime: String): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, FILENAME_FORMAT.format(name, System.nanoTime()))
                put(MediaStore.MediaColumns.MIME_TYPE, mime)
                put(MediaStore.MediaColumns.RELATIVE_PATH, folder)
            }

            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.toFile()?.absolutePath
        } else {
            val file = File(Environment.getExternalStoragePublicDirectory(folder), appName)
            if (!file.exists()) {
                file.mkdirs()
            }
            File(file.absolutePath, FILE_FORMAT.format(name, System.nanoTime(), extension)).absolutePath
        }
    }

    /** Returns a new picture filename inside the app cache. */
    fun getNewCachePictureFilename(name: String, imageType: ImageType = ImageType.PNG): String {
        return cacheFolder + FILE_FORMAT.format(name, System.nanoTime(), imageType.extension)
    }

    /** Returns a new a video filename inside the app cache. */
    fun getNewCacheVideoFilename(name: String): String {
        return cacheFolder + FILE_FORMAT.format(name, System.nanoTime(), MP4_EXTENSION)
    }

    /** Returns a new picture filename inside the Pictures folder. */
    fun getNewImageName(name: String, imageType: ImageType = ImageType.PNG): String? {
        return getEnvironmentFilename(Environment.DIRECTORY_PICTURES, name, imageType.extension, imageType.mime)
    }

    /** Returns a new video filename inside the Videos folder. */
    fun getNewVideoName(name: String): String? {
        return getEnvironmentFilename(Environment.DIRECTORY_MOVIES, name, MP4_EXTENSION, MP4_MIME)
    }

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

    enum class ImageType(val extension: String, val mime: String) {
        PNG(".png", "image/png"),
        JPG(".jpg", "image/jpg")
    }

    companion object {
        private const val FILE_FORMAT = "/%s_%s%s"
        private const val FILENAME_FORMAT = "%s_%s"
        private const val MP4_EXTENSION = ".mp4"
        private const val MP4_MIME = "video/mp4"
    }
}
