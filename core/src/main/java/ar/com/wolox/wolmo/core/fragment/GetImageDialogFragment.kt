package ar.com.wolox.wolmo.core.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ar.com.wolox.wolmo.core.R
import ar.com.wolox.wolmo.core.extensions.setTextOrGone
import ar.com.wolox.wolmo.core.util.GetImageHelper
import ar.com.wolox.wolmo.core.util.WolmoFileProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_upload_image.*
import javax.inject.Inject

class GetImageDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var getImageHelper: GetImageHelper

    @Inject
    lateinit var fileProvider: WolmoFileProvider

    private val configuration: GetImageConfiguration by lazy {
        requireArguments().get(CONFIGURATION_KEY) as GetImageConfiguration
    }

    private val newCameraPictureFilename: String?
        get() = when (configuration.cameraSavingPlace) {
            SavingPicturePlace.CACHE -> fileProvider.getNewCachePictureFilename(configuration.imageName)
            SavingPicturePlace.GALLERY -> fileProvider.getNewImageName(configuration.imageName)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_upload_image, container, false).apply {
            vUploadImageCamera.setOnClickListener {
                dismiss()
                openCamera()
            }
            vUploadImageGallery.setOnClickListener {
                dismiss()
                openGallery()
            }
            arguments?.run {
                vUploadImageTitle.setTextOrGone(getString(configuration.title))
                vUploadImageCamera.setTextOrGone(getString(configuration.cameraText))
                vUploadImageGallery.setTextOrGone(getString(configuration.galleryText))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INTENT_CODE_IMAGE_GALLERY -> configuration.callback.onGalleryResponse(data?.data)
                INTENT_CODE_IMAGE_CAMERA -> configuration.callback.onCameraResponse(data?.data)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            when (requestCode) {
                INTENT_CODE_IMAGE_GALLERY, INTENT_CODE_IMAGE_CAMERA -> configuration.callback.onUserCancelled()
            }
        }
    }

    private fun openCamera() =
        newCameraPictureFilename?.let {
            getImageHelper.openCamera(
                fragment = this,
                code = INTENT_CODE_IMAGE_CAMERA,
                destinationFilename = it,
                onPermissionDenied = configuration.callback::onPermissionDenied,
                onError = configuration.callback::onUnexpectedError)
        } ?: run {
            configuration.callback.onUnexpectedError()
        }

    private fun openGallery() = getImageHelper.openGallery(
        fragment = this,
        code = INTENT_CODE_IMAGE_GALLERY,
        onPermissionDenied = configuration.callback::onPermissionDenied,
        onError = configuration.callback::onUnexpectedError)

    companion object {

        fun newInstance(configuration: GetImageConfiguration) = GetImageDialogFragment().apply {
            arguments = bundleOf(CONFIGURATION_KEY to configuration)
        }

        private const val CONFIGURATION_KEY = "CONFIGURATION_KEY"
        private const val INTENT_CODE_IMAGE_GALLERY = 9000
        private const val INTENT_CODE_IMAGE_CAMERA = 9001
    }
}