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

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Utils class for managing {@link File}s.
 */
@ApplicationScope
public class WolmoFileProvider {

    private Context mContext;

    @Inject
    public WolmoFileProvider(Context context) {
        mContext = context;
    }

    /**
     * Creates a file in the external public directory to store data.
     * <p>
     * The file ends up being stored as:
     * filename + "." + extension
     * Use {@link Environment} to see the available directories available to create the file
     *
     * @param suffix File name, used as described above
     * @param extension ImageFormat of the file, used as described above
     * @param dirType Directory Type of where the file will be located, use {@see Environment#DIRECTORY_*}
     *
     * @return {@link File} result of the creation
     * @throws IOException If a file could not be created
     */
    public File createTempFile(@NonNull String suffix, @NonNull String extension, @NonNull String dirType) throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(dirType);

        // The suffix will be appended as it is, we need to add the dot manually
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        return File.createTempFile(suffix, extension, storageDir);
    }

    /**
     * Returns the {@link Uri} of the given file using a file provider.
     * This method wraps {@link FileProvider#getUriForFile(Context, String, File)} building the
     * provider name provided with wolmo and the application context.
     *
     * @param file A {@link File} pointing to the filename for which you want a {@link Uri}
     * @return A content URI for the file.
     */
    public Uri getUriForFile(@NonNull File file) {
        return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
    }

    /**
     * Get the physical path to a stored File by providing a URI of a content provider.
     *
     * @param fileUri A URI of a content provider pointing to an image resource
     *
     * @return A path to the real file location, or null if it can't find it
     */
    @Nullable
    public String getRealPathFromUri(@NonNull Uri fileUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = mContext.getContentResolver().query(fileUri, null, null, null, null);

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
