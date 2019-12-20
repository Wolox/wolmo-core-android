package ar.com.wolox.wolmo.core.fragment

import java.io.File

interface UploadImageView {

    fun selectImage()

    fun openCamera()

    fun openGallery()

    fun showCameraError()

    fun showGalleryError()

    fun showPermissionsError()

    fun closeDialog()

    fun notifyImageDeltedInGallery(file: File)
}
