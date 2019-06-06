package ar.com.wolox.wolmo.core.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.IntRange
import androidx.annotation.StringDef
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Utils class to manipulate images, through {@link Bitmap}s or their corresponding {@link Uri}, and
 * for retrieving pictures from gallery/taking them from the camera.
 */
@ApplicationScope
class ImageProvider @Inject constructor(val context: Context, val toastFactory: ToastFactory, val wolmoFileProvider: WolmoFileProvider) {

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(PNG, JPG)
    annotation class ImageFormat

    /*
     * Image compression formats supported.
     */

    companion object {
        const val PNG = "png"
        const val JPG = "jpg"

        /**
         * Prevents quality from being outside 0...100 range.
         *
         * @param quality target quality
         * @return if below 0, returns 0, if above 100, return 100, else returns `quality` param
         */
        private fun sanitizeQuality(quality: Int) =  Math.max(0, Math.min(quality, 100))

        /**
         * Re-sizes the image, represented as a [Bitmap], to fit the boundaries and keeping its
         * aspect ratio.
         *
         * @param image     [Bitmap] to scale
         * @param maxWidth  max width it can occupy
         * @param maxHeight max height it can occupy
         * @return If the re-scaling was necessary, the scaled [Bitmap]. Else, it returns the
         * target one.
         */
        fun fit(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
            val width = image.width
            val height = image.height

            if (maxWidth <= 0 || maxHeight <= 0 || width <= maxWidth && height <= maxHeight) {
                return image
            }

            val ratioImage = width.toFloat() / height.toFloat()

            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioImage > 1) {
                finalHeight = (finalWidth.toFloat() / ratioImage).toInt()
            } else {
                finalWidth = (finalHeight.toFloat() * ratioImage).toInt()
            }

            return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
        }

        /**
         * Get [byte[]] from an image [File]
         *
         * @param file      target image file
         * @param format    image compress format
         * @param quality   compress quality, between 0 and 100
         * @param maxWidth  max width of the target image
         * @param maxHeight max height of the target image
         * @return byte array with the formatted information of the image file, if the image exceeded
         * boundaries, it's re scaled.
         */
        fun getImageAsByteArray(
                file: File,
                format: Bitmap.CompressFormat,
                @IntRange(from = 0, to = 100) quality: Int,
                maxWidth: Int,
                maxHeight: Int) = getImageAsByteArray(
                       BitmapFactory.decodeFile(file.path),
                       format,
                       quality,
                       maxWidth,
                       maxHeight)

        /**
         * Get [byte[]] representation from a [Bitmap], with boundaries.
         *
         * @param bitmap    target Bitmap
         * @param format    image compress format
         * @param quality   compress quality, between 0 and 100
         * @param maxHeight max height of the target image
         * @param maxWidth  max width of the target image
         * @return byte array with the formatted information of the Bitmap, if the image exceeded
         * boundaries, it's re scaled.
         */
        fun getImageAsByteArray(
                bitmap: Bitmap,
                format: Bitmap.CompressFormat,
                @IntRange(from = 0, to = 100) quality: Int,
                maxWidth: Int,
                maxHeight: Int): ByteArray {

            val targetBitmap = fit(bitmap, maxWidth, maxHeight)

            val bytes = ByteArrayOutputStream()
            targetBitmap.compress(format, sanitizeQuality(quality), bytes)

            return bytes.toByteArray()
        }
    }

    /**
     * Triggers an intent to go to the device's image gallery and returns an URI with the file.
     * Override the onActivityResult method in your fragment and specify behaviour
     * for the provided request code. The selected image URI will be
     * returned in the data variable of the activity result.
     *
     * @param fragment    A fragment where the get image intent is going to be called
     * @param requestCode of the result call to be identified with
     * @param errorResId  [StringRes] to be displayed in case of error
     */
    fun getImageFromGallery(fragment: Fragment, requestCode: Int, @StringRes errorResId: Int) {
        val imageGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        imageGalleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

        // Ensure that there's a gallery app to handle the intent
        if (imageGalleryIntent.resolveActivity(context.packageManager) != null) {
            fragment.startActivityForResult(imageGalleryIntent, requestCode)
        } else {
            toastFactory.show(errorResId)
        }
    }

    /**
     * Adds a given picture to the device images gallery.
     *
     * @param imageUri The [Uri] of the image
     */
    fun addPictureToDeviceGallery(imageUri: Uri) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = imageUri
        context.sendBroadcast(mediaScanIntent)
    }

    /**
     * Triggers an intent to go to the device's camera app, stores the image as 'filename'.'format',
     * and returns its [Uri].
     * Override the onActivityResult method in your fragment and specify behaviour
     * for the provided request code. This method returns a File object that
     * contains the picture taken. You can get the returned image as an [Uri] using
     * [Uri.fromFile].
     *
     * @param fragment    A fragment where the get image intent is going to be called
     * @param requestCode A request code for the intent
     * @param filename    Filename for the future stored image
     * @param format      Format (extension) for the future image
     * @param errorResId  Resource id of the error string
     * @return [Uri] of the newly stored image
     */
    fun getImageFromCamera(fragment: Fragment, requestCode: Int, filename: String,
                           @ImageFormat format: String, @StringRes errorResId: Int): File? {

        val imageCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Ensure that there's a camera app to handle the intent
        if (imageCaptureIntent.resolveActivity(context.packageManager) == null) {
            toastFactory.show(errorResId)
            return null
        }

        val photoFile: File?
        try {
            photoFile = wolmoFileProvider.createTempFile(filename, format, Environment.DIRECTORY_DCIM)
        } catch (ex: IOException) {
            toastFactory.show(errorResId)
            return null
        }

        val photoFileUri = wolmoFileProvider.getUriForFile(photoFile)

        imageCaptureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
        fragment.startActivityForResult(imageCaptureIntent, requestCode)

        return photoFile
    }

    /**
     * Get [byte[]] from an image file, represented by its [Uri].
     *
     * @param imageFileUri target image file URI
     * @param format       image compress format
     * @param quality      compress quality, between 0 and 100
     * @param maxWidth     max width of the target image
     * @param maxHeight    max height of the target image
     * @return byte array with the formatted information of the image file, if the image exceeded
     * boundaries, it's re scaled.
     */
    fun getImageAsByteArray(
            imageFileUri: Uri,
            format: Bitmap.CompressFormat,
            @IntRange(from = 0, to = 100) quality: Int,
            maxWidth: Int,
            maxHeight: Int) = getImageAsByteArray(
                   BitmapFactory.decodeFile(wolmoFileProvider.getRealPathFromUri(imageFileUri)),
                   format,
                   quality,
                   maxWidth,
                   maxHeight)
}