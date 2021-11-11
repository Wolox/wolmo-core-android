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
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
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
import javax.inject.Inject

/**
 * Utils class to manipulate images, through [Bitmap]s or their corresponding [Uri], and
 * for retrieving pictures from gallery/taking them from the camera.
 */
class ImageProvider @Inject constructor(private val context: Context, private val wolmoFileProvider: WolmoFileProvider) {

    /** Image compression formats supported. */
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(PNG, JPG)
    annotation class ImageFormat

    @Deprecated(
        "Use getImageFromGallery(fragment, requestCode): Boolean instead",
        ReplaceWith("getImageFromGallery(fragment, requestCode)"))
    fun getImageFromGallery(fragment: Fragment, requestCode: Int, @StringRes errorRes: Int) {
        getImageFromGallery(fragment, requestCode)
    }

    /**
     * Tries to trigger an intent to go to the device's image gallery from a [fragment] and
     * returns true if it it's successful, false otherwise.
     *
     * Override the onActivityResult method in your fragment and specify behaviour
     * for the provided [requestCode]. The selected image URI will be
     * returned in the data variable of the activity result.
     */
    fun getImageFromGallery(fragment: Fragment, requestCode: Int): Boolean {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).apply {
            putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }
        // Ensure that there's a gallery app to handle the intent
        return if (intent.resolveActivity(context.packageManager) != null) {
            fragment.startActivityForResult(intent, requestCode)
            true
        } else {
            false
        }
    }

    /** Adds a given [picture] to the device images gallery. */
    fun addPictureToDeviceGallery(picture: Uri) {
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            data = picture
        })
    }

    @Deprecated(
        "Use getImageFromCamera(fragment, requestCode, file): Boolean instead",
        ReplaceWith("getImageFromCamera(fragment, requestCode, file)"))
    fun getImageFromCamera(fragment: Fragment, requestCode: Int, filename: String,
                           @ImageFormat format: String, @StringRes errorResId: Int): File? {
        val file = wolmoFileProvider.createTempFile(filename, format, Environment.DIRECTORY_DCIM).absolutePath
        getImageFromCamera(fragment, requestCode, file)
        return File(file)
    }

    /**
     * Tries to trigger an intent to go to the device's camera from a [fragment] and
     * returns true if it it's successful, false otherwise. It stores the image taken on the
     * given [file].
     *
     * Override the onActivityResult method in your fragment and specify behaviour
     * for the provided [requestCode].
     */
    fun getImageFromCamera(fragment: Fragment, requestCode: Int, file: String): Boolean {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera app to handle the intent
        if (intent.resolveActivity(context.packageManager) == null) {
            return false
        }

        val photoFile = File(file)
        val photoFileUri = wolmoFileProvider.getUriForFile(photoFile)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri)
        fragment.startActivityForResult(intent, requestCode)
        return true
    }

    /**
     * Get [ByteArray] from an [image] file, represented by its [Uri]. It'll be formatted with
     * a [format], a [quality] and a [maxWidth] and [maxHeight].
     */
    fun getImageAsByteArray(
        image: Uri,
        format: CompressFormat,
        @IntRange(from = 0, to = 100) quality: Int,
        maxWidth: Int,
        maxHeight: Int
    ) = getImageAsByteArray(
        BitmapFactory.decodeFile(wolmoFileProvider.getRealPathFromUri(image)),
        format,
        quality,
        maxWidth,
        maxHeight)

    companion object {

        const val PNG = "png"
        const val JPG = "jpg"

        /**
         * Get [ByteArray] from a [bitmap], represented by its [Uri]. It'll be formatted with
         * a [format], a [quality] and a [maxWidth] and [maxHeight].
         */
        @JvmStatic
        fun getImageAsByteArray(
            bitmap: Bitmap,
            format: CompressFormat?,
            @IntRange(from = 0, to = 100) quality: Int,
            maxWidth: Int,
            maxHeight: Int
        ) = ByteArrayOutputStream().also {
            fit(bitmap, maxWidth, maxHeight).compress(format, sanitizeQuality(quality), it)
        }.toByteArray()

        /**
         * Get [ByteArray] from a [file], represented by its [Uri]. It'll be formatted with
         * a [format], a [quality] and a [maxWidth] and [maxHeight].
         */
        @JvmStatic
        fun getImageAsByteArray(
            file: File,
            format: CompressFormat,
            @IntRange(from = 0, to = 100) quality: Int,
            maxWidth: Int,
            maxHeight: Int
        ) = getImageAsByteArray(
            BitmapFactory.decodeFile(file.path),
            format,
            quality,
            maxWidth,
            maxHeight)

        /** Prevents [quality] from being outside 0...100 range. */
        private fun sanitizeQuality(quality: Int) = quality.coerceAtLeast(0).coerceAtMost(100)

        /** Re-sizes the [image] to fit a [maxWidth], [maxHeight] and keeping its aspect ratio. */
        @JvmStatic
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
    }
}