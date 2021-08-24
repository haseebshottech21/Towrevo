package com.Towrevo.util


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.media.MediaBrowserServiceCompat.RESULT_OK
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.network.*
import com.Towrevo.preference.PREF_IS_LOGIN
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.activity.SignInActivity
import com.Towrevo.util.RealPathUtil.getDataColumn
import com.Towrevo.util.RealPathUtil.isDownloadsDocument
import com.Towrevo.util.RealPathUtil.isExternalStorageDocument
import com.Towrevo.util.RealPathUtil.isGooglePhotosUri
import com.Towrevo.util.RealPathUtil.isMediaDocument
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.Options
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Util {

    private var dialog: AlertDialog? = null


    fun showProgressDialog(context: Context) {
        hideProgressDialog()
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.layout_loading_dialog, null)
        builder.setView(v)
        dialog = builder.create()
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog!!.show()
    }

    fun hideProgressDialog() {
        if (isProgressDialogShown())
            dialog?.dismiss()
    }

    fun isProgressDialogShown(): Boolean {
        if (dialog != null)
            return dialog!!.isShowing
        else
            return false
    }

    fun logOut(context: Context) {
        if (MyApp.preflogin.getBoolean(PREF_IS_LOGIN, false)) {
            MyApp.preflogin[PREF_IS_LOGIN] = false
            PreferenceHelper.clearLoginPref((context as Activity))

            val i = Intent(context, SignInActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)
        }
    }

    fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val key = "key=" + "AIzaSyDJXaXxSGTPbOThZcYMoOdrgYfOkV57v6Y"
        val params = "$origin&$dest&$sensor&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    fun getUserCountryInfo(activity: Activity?): Country? {
//        https://github.com/mukeshsolanki/country-picker-android
        return Country.getCountryFromSIM(activity)
    }


    fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }

    @SuppressLint("MissingPermission")
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities != null) {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            } else {
                return false
            }
        } else {
            connectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI || connectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE

        }
    }


    fun showSnackBar(view: View, message: String) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */


    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources
            .displayMetrics
            .density
        return (dp.toFloat() * density).roundToInt()
    }

    fun noTrailingwhiteLines(text: CharSequence): CharSequence {
        var text = text
        if (text.length > 1) {
            while (text[text.length - 1] == '\n') {
                text = text.subSequence(0, text.length - 1)
            }
        }
        return text
    }


    fun getRegisterDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fail", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                MyApp.preflogin[PREF_DEVICE_TOKEN] = token
                // Log and toast
                //val msg = getString(R.string.msg_token_fmt, token)
                Log.d("token registerd", token)
                //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
    }

    /*
    create file from image
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat(DATE_FORMAT_8).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Image_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    /*open gallery */
    @SuppressLint("RestrictedApi", "CheckResult")
    fun openGalleryPicker(context: Context, count: Int, imageCallback: (File) -> Unit) {
        if (count >= 1) {
            // it will pass result in respective activity's onActivityResult method.
            Matisse.from(context as Activity)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .countable(false)
                .maxSelectable(count)
                .gridExpectedSize(context.resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(Glide4Engine())
                .forResult(GALLERY_PICKER_CODE)

        } else {
            val options = Options()
            options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            options.setAspectRatio(25f, 50f)
            // it will pass result in activity's imageCallback.
            RxPaparazzo.single(context as Activity)
                .crop() //can pass option
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response ->

                    if (response.resultCode() == RESULT_OK) {
                        imageCallback(response.data().file)
                    }

                }
        }
    }

    fun openCameraIntent(
        context: Context,
        action: String = MediaStore.ACTION_IMAGE_CAPTURE,
        mediaCallback: (File?) -> Unit,
        mediaUriCallback: (Uri?) -> Unit
    ) {

        if (action == MediaStore.ACTION_IMAGE_CAPTURE) {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFile(context)
            } catch (ex: IOException) {
                null
            }
            val packagename = context.packageName
            val photoURI: Uri
            photoFile?.also {
                photoURI = FileProvider.getUriForFile(
                    context,
                    "$packagename.provider",
                    it
                )
                mediaCallback(photoFile)
                mediaUriCallback(photoURI)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                (context as Activity).startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE)


            }

        } else if (action == MediaStore.ACTION_VIDEO_CAPTURE) {
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            val videoFile: File? = try {
                createVideoFile(context)
            } catch (ex: IOException) {
                null
            }
            val packagename = context.packageName
            val videoURI: Uri
            videoFile?.also {
                videoURI = FileProvider.getUriForFile(
                    context,
                    "$packagename.provider",
                    it
                )
                mediaCallback(videoFile)
                mediaUriCallback(videoURI)
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
                (context as Activity).startActivityForResult(takeVideoIntent, IMAGE_CAPTURE_CODE)


            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createVideoFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat(DATE_FORMAT_8).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Video_${timeStamp}_",
            ".mp4",
            storageDir
        )
    }


    /**
     * handle camera picture
     */
    fun openCameraIntent(
        context: Context,
        imageCallback: (File?) -> Unit,
        imageUriCallback: (Uri?) -> Unit
    ) {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile(context)
        } catch (ex: IOException) {
            null
        }
        val packagename = context.packageName
        val photoURI: Uri
        photoFile?.also {
            photoURI = FileProvider.getUriForFile(
                context,
                "$packagename.provider",
                it
            )
            imageCallback(photoFile)
            imageUriCallback(photoURI)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            (context as Activity).startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE)


        }

    }

    /**
     * upload image to s3bucket
     */
   /* fun UploadImage(
        list: MutableList<Uri>,
        isCamera: Boolean,
        currentPhotoPath: String,
        context: Context,
        showProgress: Boolean,
        uploadCompleteCallback: (String) -> Unit
    ) {

        val listOfFile = mutableListOf<File>()
        if (showProgress) showProgressDialog(context)
        if (isCamera) {
            val file = File(currentPhotoPath)
            listOfFile.add(file)

        } else {
            for (uri in list) {
                val filePath = getRealPathFromURI_API19(context, uri)
                val file = File(filePath!!)  fun generateImageName(): String {

        return "android" + System.currentTimeMillis().toString() + ".jpeg"
    }
                listOfFile.add(file)
            }
        }

        AmazoneUploadUtil(
            context = context,
            filelist = listOfFile,
            awsAmazoneUploadUtil = object :
                AmazoneUploadUtil.AwsUploadCompleteListner {
                override fun onComplete(imageName: String) {
                    uploadCompleteCallback(imageName)
                    if (showProgress) hideProgressDialog()
                }

            }).initialize()
    }*/


    fun addDefaultParams(hashMap: HashMap<String, Any>): HashMap<String, Any> {
        hashMap[PARAM_TOKEN] = PreferenceHelper.getToken()
        hashMap[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
        hashMap[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        hashMap[PARAM_USER_ID] = PreferenceHelper.getUserID()
        return hashMap
    }
    fun generateImageName(): String {

        return "android" + System.currentTimeMillis().toString() + ".jpeg"
    }
    fun getFileToUpload(filename: String, path: String, name: String): MultipartBody.Part {
        lateinit var fileToUpload: MultipartBody.Part
        var requestFile: RequestBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
        if (path.isNotBlank()) {
            val file = File(path)
            requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            fileToUpload =
                MultipartBody.Part.createFormData(filename, name, requestFile)
        } else {
            fileToUpload = MultipartBody.Part.createFormData(filename, "", requestFile)
        }
        return fileToUpload
    }
}