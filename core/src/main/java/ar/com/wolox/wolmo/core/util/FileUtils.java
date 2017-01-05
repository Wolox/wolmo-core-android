package ar.com.wolox.wolmo.core.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private FileUtils() {}

    /**
     * Creates a file in the external public directory to store data.
     *
     * The file ends up being stored as:
     *  filename + "." + extension
     *
     * @param filename File name, used as described above
     * @param extension ImageFormat of the file, used as described above
     */
    public static File createFile(String filename, String extension) throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        return File.createTempFile(
                filename,                           /* name */
                extension,                          /* extension */
                storageDir                          /* directory */
        );
    }

    /**
     * Get the physical path to a stored File by providing a URI of a content provider
     *
     * @param fileUri A URI of a content provider pointing to an image resource
     *
     * @return A path to the real file location
     */
    public static String getRealPathFromUri(Uri fileUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = ContextUtils
                    .getAppContext()
                    .getContentResolver()
                    .query(fileUri, proj, null, null, null);
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
