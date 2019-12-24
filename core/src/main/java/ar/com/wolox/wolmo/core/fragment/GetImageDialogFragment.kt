package ar.com.wolox.wolmo.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ar.com.wolox.wolmo.core.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_upload_image.*

class GetImageDialogFragment : BottomSheetDialogFragment() {

    lateinit var callback: AddPhotoCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_upload_image, container, false).apply {
            vUploadImageCamera.setOnClickListener {
                dismiss()
                callback.onCameraRequested()
            }
            vUploadImageGallery.setOnClickListener {
                dismiss()
                callback.onGalleryRequested()
            }
            arguments?.run {
                getString(TITLE_KEY)?.let { vUploadImageTitle.text = it }
                getString(CAMERA_TEXT_KEY)?.let { vUploadImageCamera.text = it }
                getString(GALLERY_TEXT_KEY)?.let { vUploadImageGallery.text = it }
            }
        }
    }

    interface AddPhotoCallback {

        fun onCameraRequested()

        fun onGalleryRequested()
    }

    companion object {

        fun newInstance(title: String?, cameraText: String?, galleryText: String?) = GetImageDialogFragment().apply {
            arguments = bundleOf(
                TITLE_KEY to title,
                CAMERA_TEXT_KEY to cameraText,
                GALLERY_TEXT_KEY to galleryText
            )
        }

        private const val TITLE_KEY = "TITLE_KEY"
        private const val CAMERA_TEXT_KEY = "CAMERA_TEXT_KEY"
        private const val GALLERY_TEXT_KEY = "GALLERY_TEXT_KEY"
    }
}