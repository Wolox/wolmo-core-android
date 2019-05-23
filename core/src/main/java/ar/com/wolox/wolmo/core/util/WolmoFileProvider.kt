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
 * Utils class for managing {@link File}s.
 */
@ApplicationScope
class WolmoFileProvider @Inject constructor(private val mContext: Context) {

    /**
     * Creates a file in the external public directory to store data.
     *
     *
     * The file ends up being stored as:
     * filename + "." + extension
     * Use [Environment] to see the available directories available to create the file
     *
     * @param suffix File name, used as described above
     * @param extension ImageFormat of the file, used as described above
     * @param dirType Directory Type of where the file will be located, use {@see Environment#DIRECTORY_*}
     *
     * @return [File] result of the creation
     * @throws IOException If a file could not be created
     */
    @Throws(IOException::class)
    fun createTempFile(suffix: String, extension: String, dirType: String): File {
        var extensionValue = extension
        val storageDir = Environment.getExternalStoragePublicDirectory(dirType)

        // The suffix will be appended as it is, we need to add the dot manually
        if (!extensionValue.startsWith(".")) {
            extensionValue = ".$extensionValue"
        }

        return File.createTempFile(suffix, extensionValue, storageDir)
    }

    /**
     * Returns the [Uri] of the given file using a file provider.
     * This method wraps [FileProvider.getUriForFile] building the
     * provider name provided with wolmo and the application context.
     *
     * @param file A [File] pointing to the filename for which you want a [Uri]
     * @return A content URI for the file.
     */
    fun getUriForFile(file: File): Uri = FileProvider.getUriForFile(mContext, mContext.packageName + ".provider", file)

    /**
     * Get the physical path to a stored File by providing a URI of a content provider.
     *
     * @param fileUri A URI of a content provider pointing to an image resource
     *
     * @return A path to the real file location, or null if it can't find it
     */
    fun getRealPathFromUri(fileUri: Uri): String? {
        var cursor: Cursor? = null
        try {
            cursor = mContext.contentResolver.query(fileUri, null, null, null, null)

            if (cursor == null) return null

            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }
}