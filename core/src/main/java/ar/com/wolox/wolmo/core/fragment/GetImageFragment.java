package ar.com.wolox.wolmo.core.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import ar.com.wolox.wolmo.core.permission.PermissionListener;
import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.ImageUtils;

public abstract class GetImageFragment<T extends BasePresenter> extends WoloxFragment<T> {

    private static final String[] CAMERA_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final String[] GALLERY_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int INTENT_CODE_IMAGE_GALLERY = 9000;
    private static final int INTENT_CODE_IMAGE_CAMERA = 9001;

    private Uri mPictureTakenUri;
    private OnImageReturnCallback mImageCallback;

    /* Error types */
    protected enum Error {
        USER_CANCELED,      // Image selection canceled by the user
        ERROR_DATA,         // Image URI was no returned
        ERROR_UNKNOWN,      // Unknown error
        PERMISSION_DENIED   // Gallery/Camera Permission denied
    }

    /* Callback for image selection */
    public interface OnImageReturnCallback {

        /**
         * Method for when the image retrieval was a success, exposing the {@link Uri} of the image
         *
         * @param imageUri retrieved image
         */
        void success(Uri imageUri);

        /**
         * Method for when the image retrieval was a failure, exposing the correponding
         * {@link Error} instance
         *
         * @param error describing the failure reason
         */
        void error(Error error);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case INTENT_CODE_IMAGE_GALLERY:
                    if (data != null) {
                        mImageCallback.success(data.getData());
                    } else {
                        notifyError(Error.ERROR_DATA);
                    }
                    break;

                case INTENT_CODE_IMAGE_CAMERA:
                    ImageUtils.addPictureToDeviceGallery(mPictureTakenUri);
                    mImageCallback.success(mPictureTakenUri);
                    break;

                default:
                    notifyError(Error.ERROR_UNKNOWN);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case INTENT_CODE_IMAGE_GALLERY:
                case INTENT_CODE_IMAGE_CAMERA:
                    notifyError(Error.USER_CANCELED);
                    break;

                default:
                    notifyError(Error.ERROR_UNKNOWN);
            }

        }

        clearCallbacks();
    }

    private void clearCallbacks() {
        mImageCallback = null;
    }

    private void notifyError(Error error) {
        if (mImageCallback != null) {
            mImageCallback.error(error);
        }
    }

    @StringRes
    protected abstract int galleryErrorResId();

    @StringRes
    protected abstract int cameraErrorResId();

    @NonNull
    protected abstract String pictureTakenFilename();

    /**
     * Start a request for an image from Gallery
     *
     * @param onImageReturnCallback callback for request result
     */
    protected void selectImageFromGallery(final OnImageReturnCallback onImageReturnCallback) {
        PermissionManager.getInstance().requirePermission(
                this,
                new PermissionListener() {
                    @Override
                    public void onPermissionsGranted() {
                        mImageCallback = onImageReturnCallback;

                        ImageUtils.getImageFromGallery(
                                GetImageFragment.this,
                                INTENT_CODE_IMAGE_GALLERY,
                                galleryErrorResId());
                    }

                    @Override
                    public void onPermissionsDenied(String[] deniedPermissions) {
                        onImageReturnCallback.error(Error.PERMISSION_DENIED);
                    }
                },
                GALLERY_PERMISSIONS);
    }

    /**
     * Start a request for an image from Camera
     *
     * @param onImageReturnCallback callback for request result
     */
    protected void takePicture(final OnImageReturnCallback onImageReturnCallback) {
        PermissionManager.getInstance().requirePermission(
                this,
                new PermissionListener() {
                    @Override
                    public void onPermissionsGranted() {
                        mImageCallback = onImageReturnCallback;

                        mPictureTakenUri =
                                ImageUtils.getImageFromCamera(
                                        GetImageFragment.this,
                                        INTENT_CODE_IMAGE_CAMERA,
                                        pictureTakenFilename(),
                                        ImageUtils.PNG,
                                        cameraErrorResId());
                    }

                    @Override
                    public void onPermissionsDenied(String[] deniedPermissions) {
                        onImageReturnCallback.error(Error.PERMISSION_DENIED);
                    }
                },
                CAMERA_PERMISSIONS);
    }

}
