package ar.com.wolox.wolmo.core.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ImageUtils {

    @Retention(SOURCE)
    @StringDef({
            PNG,
            JPG
    })
    public @interface ImageFormat {
    }

    public static final String PNG = "png";
    public static final String JPG = "jpg";

    private ImageUtils() {
    }

    /**
     * Triggers an intent to go to the device's image gallery and returns an URI with the file
     * <p>
     * <p/>
     * Override the onActivityResult method in your fragment and specify behaviour
     * for the provided request code. The selected image URI will be
     * returned in the data variable of the activity result.
     *
     * @param fragment A fragment where the get image intent is going to be called
     * @author juanignaciomolina
     */
    public static void getImageFromGallery(Fragment fragment, int requestCode, @StringRes int errorResId) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        // Ensure that there's a gallery app to handle the intent
        if (i.resolveActivity(ContextUtils.getAppContext().getPackageManager()) != null)
            fragment.startActivityForResult(i, requestCode);
        else {
            ToastUtils.showToast(errorResId);
        }
    }

    /**
     * Adds a given picture to the device images gallery
     *
     * @param imageUri The {@link Uri} of the image
     */
    public static void addPictureToDeviceGallery(Uri imageUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        ContextUtils.getAppContext().sendBroadcast(mediaScanIntent);
    }

    /**
     * Triggers an intent to go to the device's camera app, stores the image as 'filename'.'format',
     * and returns its {@link Uri}
     * <p>
     * <p/>
     * Override the onActivityResult method in your fragment and specify behaviour
     * for the provided request code. This method returns a File object that
     * contains the picture taken. You can get the returned image as an Uri using
     * Uri.fromFile(getImageFromCamera(fragment));
     *
     * @param fragment    A fragment where the get image intent is going to be called
     * @param requestCode A request code for the intent
     * @param filename    Filename for the future stored image
     * @param format      Format (extension) for the future image
     * @param errorResId  Resource id of the error string
     * @return {@link Uri} of the newly stored image
     */
    @Nullable
    public static Uri getImageFromCamera(Fragment fragment, int requestCode, String filename,
                                         @ImageFormat String format, @StringRes int errorResId) {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera app to handle the intent
        if (i.resolveActivity(ContextUtils.getAppContext().getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = FileUtils.createFile(filename, format);
            } catch (IOException ex) {
                ToastUtils.showToast(errorResId);
            }

            Uri photoFileUri = Uri.fromFile(photoFile);

            if (photoFile != null) {
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);
                fragment.startActivityForResult(i, requestCode);
            }

            return photoFileUri;
        } else {
            ToastUtils.showToast(errorResId);
        }

        return null;
    }

    /**
     * Get PNG representation from a {@link Bitmap}.
     *
     * @param bitmap target Bitmap
     *
     * @return byte array with the PNG information of the Bitmap
     */
    public static byte[] generatePngByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, bytes);

        return bytes.toByteArray();
    }

    /**
     * Get PNG from an image file, represented by its {@link Uri}
     *
     * @param imageFileUri target image file URI
     *
     * @return byte array with the PNG information of the image file.
     */
    public static byte[] generatePngByteArray(Uri imageFileUri) {
        Bitmap imageBitmap = BitmapFactory.decodeFile(FileUtils.getRealPathFromUri(imageFileUri));

        return generatePngByteArray(imageBitmap);
    }

}
