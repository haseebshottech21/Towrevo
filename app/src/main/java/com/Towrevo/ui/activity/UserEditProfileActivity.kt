package com.Towrevo.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.demomvvm.network.ApiClient
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CustomerEditProfileViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import com.Towrevo.util.extension.visible
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_user_edit_profile.*
import kotlinx.android.synthetic.main.activity_user_edit_profile.browseImageIV
import kotlinx.android.synthetic.main.activity_user_edit_profile.countryCodePicker
import kotlinx.android.synthetic.main.activity_user_edit_profile.eaddressET
import kotlinx.android.synthetic.main.activity_user_edit_profile.fNameET
import kotlinx.android.synthetic.main.activity_user_edit_profile.lNameET
import kotlinx.android.synthetic.main.activity_user_edit_profile.noInternetView
import kotlinx.android.synthetic.main.activity_user_edit_profile.pNumberET
import kotlinx.android.synthetic.main.activity_user_edit_profile.passwordET
import kotlinx.android.synthetic.main.activity_user_edit_profile.profileUserImageIV
import kotlinx.android.synthetic.main.titlebar_layout.*
import okhttp3.RequestBody

class UserEditProfileActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {
    private var photoURI: Uri? = null
    private val imgCount = 1
    private var isCamera: Boolean = false
    private lateinit var currentPhotoPath: String
    private var countryCode: String = ""
    var filePath: String? = ""
    var fileName = ""
    private var mobileNo=""


    private val customerEditProfileViewModel: CustomerEditProfileViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory {
                CustomerEditProfileViewModel(
                    this,
                    mainRL,
                    noInternetView
                )
            }).get(
            CustomerEditProfileViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit_profile)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        callViewProfileApi()
        handleApi()
        backIV.setOnClickListener { finish() }

    }

    private fun setData() {
        passwordET.setOnTouchListener(this)
        nextBT.setOnClickListener(this)
        browseImageIV.setOnClickListener(this)
        editUserImageFL.setOnClickListener(this)

      //  countryCode = countryCodePicker.defaultCountryCode
        countryCode = countryCode


        pNumberET.setText("+" + countryCodePicker.defaultCountryCode + " ")

        pNumberET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
/*
                if (!s.toString().startsWith("+" + countryCodePicker.defaultCountryCode + " ")) {
                    pNumberET.setText("+" + countryCodePicker.defaultCountryCode + " ")
                    Selection.setSelection(pNumberET.getText(), pNumberET.getText()!!.length)
                }
*/
            }
        })

    }

    private fun setLabel() {
        titleBarTV.text = LabelUtils.getLabel(this, EDIT_PROFILE, R.string.edit_profile)
        nextBT.text = LabelUtils.getLabel(this, UPDATE, R.string.update)
    }

    private fun handleApi() {
        customerEditProfileViewModel.customereditProfileResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
//                    mainRL.visible()
                    //customer details
                    MyApp.preflogin[PREF_IS_LOGIN] = true
                    MyApp.preflogin[PREF_LOGIN_TYPE] = ""
                    MyApp.preflogin[PREF_FIRST_NAME] = it[0].data[0].firstName
                    MyApp.preflogin[PREF_LAST_NAME] = it[0].data[0].lastName
                    MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                    MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                    MyApp.preflogin[PREF_PROFILE_IMAGE] = it[0].data[0].profileImage
                    MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                    MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile

                    openActivity(CategoryActivity::class.java)
                }
                FAILURE -> {
                    toast(it[0].message)
                }

            }
        })

        customerEditProfileViewModel.viewProfileResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    mainRL.visible()
                    eaddressET.setText(it[0].data[0].email)
                    fNameET.setText(it[0].data[0].firstName)
                    lNameET.setText(it[0].data[0].lastName)

                    pNumberET.setText("+"+it[0].data[0].mobile)

                    Glide.with(this).load(it[0].data[0].profileImage).placeholder(R.drawable.ic_group_image).into(profileUserImageIV)

                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }
        })
    }

    private fun callCustoemerEditProfileApi(

        filePath: String, fileName: String, FirstName: String,
        lastName: String, email: String, mobile: String, deviceType: String) {




        val params: MutableMap<String, RequestBody> = HashMap()
        params.put(PARAM_USER_ID, ApiClient.getRequestBody(PreferenceHelper.getUserID()))
        params.put(PARAM_USER_TYPE, ApiClient.getRequestBody(PreferenceHelper.getUserType()))
        params.put(PARAM_TOKEN, ApiClient.getRequestBody(PreferenceHelper.getToken()))
        params.put(PARAM_FIRST_NAME, ApiClient.getRequestBody(FirstName))
        params.put(PARAM_LAST_NAME, ApiClient.getRequestBody(lastName))
        params.put(PARAM_EMAIL, ApiClient.getRequestBody(email))
        params.put(PARAM_MOBILE, ApiClient.getRequestBody(mobile))
        params.put(PARAM_DEVICE_TYPE, ApiClient.getRequestBody(deviceType))

        customerEditProfileViewModel.callCustomerEditProfileApi(params, Util.getFileToUpload(PARAM_PROFILE_IAMGE, filePath, fileName)
        )

    }



//
//            val password = passwordET.text.toString().trim()
//
//        val map: HashMap<String, Any> = HashMap()
//        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
//        map[PARAM_FIRST_NAME] = fNameET.text.toString().trim()
//        map[PARAM_LAST_NAME] = lNameET.text.toString().trim()
//        map[PARAM_EMAIL] = eaddressET.text.toString().trim()
//        map[PARAM_MOBILE] = pNumberET.text.toString().trim()
//        map[PARAM_PASSWORD] = PreferenceHelper.getPassword()
//        map[PARAM_DEVICE_TYPE] = "1"
//        map[PARAM_TOKEN] = PreferenceHelper.getToken()
//        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
//        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
//        if (password.isEmpty()) {
//            map[PARAM_PASSWORD] = PreferenceHelper.getPassword()
//        } else {
//            map[PARAM_PASSWORD] = password
//
//        }
//
//        customerEditProfileViewModel.callCustomerEditProfileApi(map)
//
//    }


    private fun callViewProfileApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
        map[PARAM_DEVICE_TYPE] = "1"

        customerEditProfileViewModel.callViewProfileApi(map)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.passwordET -> Validation.TansformPassword(event, passwordET)
        }
        return false
    }

    private fun isValidDetail(): String {
        var msg = ""

        when {
            fNameET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_FNAME,
                    getString(R.string.firstname_can_not_blank)
                )

            }
            lNameET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_LNAME,
                    getString(R.string.lastname_can_not_blank)
                )

            }
            eaddressET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_EMAILADDRESS,
                    getString(R.string.emailaddress_cant_blank)
                )

            }
            !Validation.isEmailValid(eaddressET.text.toString()) -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_EMAILADDRESS,
                    getString(R.string.email_address_isvalid)
                )

            }
            pNumberET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PHONE,
                    getString(R.string.phone_can_not_blank)
                )

            }

/*
            passwordET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )

            }
*/
        }
        return msg
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nextBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {

                    if (pNumberET.text.toString().trim().contains(" ")) {
                        val numberArray = pNumberET.text.toString().trim().split(" ")

                        mobileNo = countryCode + numberArray[numberArray.lastIndex]
                    } else {
                        mobileNo = countryCode + pNumberET.text.toString().trim()
                    }


                    callCustoemerEditProfileApi(filePath!!, fileName, fNameET.text.toString().trim(), lNameET.text.toString().trim(),
                        eaddressET.text.toString().trim(), mobileNo,
                       "1"
                    )
                } else {
                    toast(msg)
                }
            }
            R.id.editUserImageFL -> {
                PermissionUtils.checkForPermission(this@UserEditProfileActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,


                    myCallBack = {
                        if (it) showPictureDialog()
                    })
            }
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(
            LabelUtils.getLabel(
                this@UserEditProfileActivity,
                SELECT_OPTION,
                R.string.select_option
            )
        )
        val pictureDialogItems = arrayOf(
            LabelUtils.getLabel(this@UserEditProfileActivity, GALLERY, R.string.gallery),
            LabelUtils.getLabel(this@UserEditProfileActivity, CAMERA, R.string.camera)
        )
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> {
                    Util.openGalleryPicker(this, imgCount, imageCallback = {
                        // get if multiple image selected
                        photoURI = Uri.fromFile(it)
                    })
                }
                1 -> {
                    Util.openCameraIntent(this, mediaCallback = {
                        currentPhotoPath = it!!.absolutePath
                        isCamera = true
                    }, mediaUriCallback = { photoURI = it })
                }
            }
        }
        pictureDialog.show()
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICKER_CODE && resultCode == Activity.RESULT_OK) {

            val obtainResult = Matisse.obtainResult(data)
            Glide.with(this).load(obtainResult[0]).into(profileUserImageIV)
            photoURI = obtainResult[0]
            filePath = Util.getRealPathFromURI_API19(applicationContext, photoURI!!)
            fileName = Util.generateImageName()
            profileUserImageIV.setImageURI(obtainResult[0])
            Log.e("filePath", filePath.toString())
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
//            Glide.with(this).load(photoURI).into(profileUserImageIV)
            profileUserImageIV.setImageURI(photoURI)
            filePath = currentPhotoPath
            Log.e("filePath", filePath.toString())
            fileName = Util.generateImageName()
        }
    }
}