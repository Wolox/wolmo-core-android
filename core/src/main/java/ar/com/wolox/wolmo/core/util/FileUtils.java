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

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * Utils class for managing {@link File}s.
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Creates a file in the external public directory to store data.
     * <p>
     * The file ends up being stored as:
     * filename + "." + extension
     *
     * @param filename  File name, used as described above
     * @param extension ImageFormat of the file, used as described above
     * @return {@link File} result of the creation
     * @throws IOException If a file could not be created
     */
    public static File createFile(
            @NonNull String filename, @NonNull String extension) throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        //The suffix will be appended as it is, we need to add the dot manually
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        return File.createTempFile(filename, extension, storageDir);
    }

    /**
     * Get the physical path to a stored File by providing a URI of a content provider.
     *
     * @param fileUri A URI of a content provider pointing to an image resource
     * @return A path to the real file location, or null if it can't find it
     */
    @Nullable
    public static String getRealPathFromUri(@NonNull Uri fileUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = ContextUtils
                    .getAppContext()
                    .getContentResolver()
                    .query(fileUri, proj, null, null, null);

            if (cursor == null) return null;

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
