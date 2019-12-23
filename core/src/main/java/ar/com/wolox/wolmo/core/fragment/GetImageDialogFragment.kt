package ar.com.wolox.wolmo.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        }
    }

    interface AddPhotoCallback {

        fun onCameraRequested()

        fun onGalleryRequested()
    }

    companion object {

        fun newInstance() = GetImageDialogFragment()
    }
}