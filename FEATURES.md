<p align="center">
  <img height="140px" width="400px" src="https://cloud.githubusercontent.com/assets/4109119/25450281/cac5979e-2a94-11e7-9176-8e323df5dab8.png"/>
</p>

# WOLMO features

### BasePresenter
Base implementation of a presenter. It contains a nullable view [V] which is attached and detached given fragment’s lifecycle. It provides some callbacks to be overridden if needed:
- `onViewAttached(): Unit`: invoked when the view is attached.
- `onViewDetached(): Unit`: invoked when the view is detached.

### CoroutineBasePresenter
Coroutine implementation of a `BasePresenter`. It’s a `CoroutineScope` attached to fragment’s lifecycle (it’s cancelled on view detached). It accepts a `CoroutineContext` and uses `MainDispatcher` as default.

### GetImageHelper
Injectable helper class to open gallery and camera to get an image.
- `fun openGallery(fragment: Fragment, code: Int, onPermissionDenied: () -> Unit = {}, onGalleryNotFound: () -> Unit = {})`: Request read permissions and open the gallery for result with the given [code] app if exists.
- `fun openCamera(fragment: Fragment, code: Int, destinationFilename: String, onPermissionDenied: () -> Unit = {}, onCameraNotFound: () -> Unit = {})`: Request camera permissions and open the camera for result with the given [code] app if exists, saving the picture on the [destinationFilename].

### ImageProvider
Singleton injectable utils class to manipulate and retrieve images.
- `addPictureToDeviceGallery(picture: Uri): Unit`: adds the given [picture] to the device images gallery.
- `getImageFromGallery(fragment: Fragment, requestCode: Int): Boolean `: tries to open gallery to retrieve an image. Returns true if successful, false otherwise. Override `onActivityResult` on the given [fragment] and check the given [requestCode] for result.
- `getImageFromCamera(fragment: Fragment, requestCode: Int, file: String): Boolean`: tries to open camera to take a picture to be saved on the given [file]. Returns true if successful, false otherwise. Override `onActivityResult` on the given [fragment] and check the given [requestCode] for result.
- … and more!

### KeyboardManager
Injectable class to manage the Android's soft keyboard.
- `show(editText: EditText): Unit`: forces the soft keyboard to show for a specific [editText].
- `hide(view: View): Unit`: forces the soft keyboard to hide, meant to be called from inside a Fragment's [view].
- `hide(view: View): Unit`: forces the soft keyboard to hide, meant to be called from inside an [activity].

### Logger
Injectable wrapper of [Log] to simplify logs in the same class by reusing the tag and to simplify unit tests.

### NavigationUtils
Provides some context extensions for navigation.
- `Context.jumpTo(clazz: Class<*>, vararg intentExtras: IntentExtra)`: opens a new activity [clazz] sending all the given [intentExtras].
- `Context.jumpToClearingTask(clazz: Class<*>, transition: ActivityOptionsCompat?, vararg intentExtras: IntentExtra)`: opens a new activity [clazz] with a [transition] sending all the given [intentExtras].
- `Context.jumpTo(clazz: Class<*>, vararg intentExtras: IntentExtra)`: opens a new activity [clazz] clearing the current task and sending all the given [intentExtras]. 
- `Context.openBrowser(url: String?)`: opens browser with the given [url] (if it's null, open a blank page). 
- `Context.makeCall(phone: String)`: make a call to the given [phone]. It needs the [CALL_PHONE] permission.
- `Context.openDial(phone: String)`: open the dial with the given [phone]. It doesn't need any extra permission.

### PermissionManager
Singleton injectable helper class to handler Android’s runtime permissions.
- `requestPermission(fragment: Fragment, listener: PermissionListener?, vararg permissions: String): Unit`: request one or more [permissions] from a [fragment] and invokes the [listener] on complete. 
- `requestPermission(activity: Activity, listener: PermissionListener?, vararg permissions: String): Unit`: request one or more [permissions] from an [activity] and invokes the [listener] on complete. 
- `onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray): Unit`: should be invoked on fragment/activity onRequestPermissionsResult. It’s already implemented on `WolmoActivity` so, since `PermissionManager` is a singleton, by using `WolmoActivity` no importance should be given to this.

### SharedPreferencesManager
Singleton injectable utility class to query and store values in [SharedPreferences].

### SimpleFragmentPagerAdapterBase 
Base implementation of a `FragmentStatePagerAdapter` for a `ViewPager` that allows, simply, to add fragments:
- `addFragment(fragment: Fragment, title: String): Unit`: adds a single fragment with its titile to the adapter.
- `addFragments(fragments: List<Pair<Fragment, String>>): Unit`: adds multiple fragments with theirs titles to the adapter.

Note: The fragments given to this should be injected.

### ToastFactory
Singleton injectable utility class to simplify work with Android's [Toast] messages.

### WolmoActivity
Base implementation of a `DaggerActivity` that provides these extra features:
- `replaceFragment(@IdRes resId: Int, fragment: Fragment): Unit`: replace the [resId] with the given [fragment].
- `requireArgument(key: String): T`: get the argument from the intent extras by the given [key] and returns it as a non-null [T].

### WolmoFileProvider
Singleton injectable utils class for managing [File]s.
- `getNewCachePictureFilename(name: String, imageType: ImageType): String`: returns a new picture filename inside the app cache. The filename will be [name]_[System.nanoTime()].
- `getNewCacheVideoFilename(name: String): String`: returns a new video filename inside the app cache. The filename will be [name]_[System.nanoTime()].
- `getNewPictureName(name: String, imageType: ImageType): String`: returns a new picture filename inside the DCIM folder. The filename will be [name]_[System.nanoTime()].
- `getNewVideoName(name: String): String`: returns a new video filename inside the Videos folder. The filename will be [name]_[System.nanoTime()].
- … and more!

### WolmoFragment
Base implementation of a `DaggerFragment` that is MVP-ready (you can access the presenter [T] by `presenter`) and provides some extra callbacks to be overridden if needed:
- `init(): Unit`: invoked on view created and used to initialize everything necessary.
- `setListeners(): Unit`: invoked on view created and used to set listeners like `onClick`.
- `handleArguments(arguments: Bundle?): Boolean`: invoked on create and used to verify if arguments contains what fragment requires. Returning null or false will end the execution.

It also provides this extra feature:
- `requireArgument(key: String): T`: get argument by the given [key] and returns it as a non-null [T].
