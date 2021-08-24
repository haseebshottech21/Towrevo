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
import com.example.demomvvm.network.ApiClient
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.labels.PASSWORD
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.cms.PrivacyPolicyActivity
import com.Towrevo.ui.cms.TermConditionActivity
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.UserRegisterViewModel
import com.Towrevo.util.*
import com.Towrevo.util.Validation.isEmailValid
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.openActivityForResult
import com.Towrevo.util.extension.toast
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import kotlinx.android.synthetic.main.activity_user_sign_up.countryCodePicker
import kotlinx.android.synthetic.main.activity_user_sign_up.eaddressET
import kotlinx.android.synthetic.main.activity_user_sign_up.editImage
import kotlinx.android.synthetic.main.activity_user_sign_up.emailAddressTV
import kotlinx.android.synthetic.main.activity_user_sign_up.mainLL
import kotlinx.android.synthetic.main.activity_user_sign_up.mobileNumberTV
import kotlinx.android.synthetic.main.activity_user_sign_up.noInternetView
import kotlinx.android.synthetic.main.activity_user_sign_up.pNumberET
import kotlinx.android.synthetic.main.activity_user_sign_up.passwordEET
import kotlinx.android.synthetic.main.activity_user_sign_up.passwordTV
import kotlinx.android.synthetic.main.activity_user_sign_up.privacyTV
import kotlinx.android.synthetic.main.activity_user_sign_up.termsTV
import kotlinx.android.synthetic.main.titlebar_layout.*
import okhttp3.RequestBody

class CustomerRegisterActivity : AppCompatActivity(),View.OnTouchListener,View.OnClickListener {
    private var photoURI: Uri? = null
    private val imgCount = 1
    private var isCamera: Boolean = false
    private lateinit var currentPhotoPath: String
    private var countryCode: String = "+1"
    var filePath: String? = ""
    var fileName = ""
    private var mobileNo=""

    private val userregisterViewModel: UserRegisterViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { UserRegisterViewModel(this, mainLL, noInternetView) }).get(
            UserRegisterViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        setContentView(R.layout.activity_user_sign_up)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        onClick()
        setLable()
        handleApi()
        titleBarTV.visibility = View.GONE
        backIV.setOnClickListener { finish() }


//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

    }

    private fun handleApi() {
        userregisterViewModel.userRegisterResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString().trim()
                    MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                    MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                    MyApp.preflogin[PREF_FIRST_NAME] = it[0].data[0].firstName
                    MyApp.preflogin[PREF_LAST_NAME] = it[0].data[0].lastName
                    MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                    MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token
                    MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                    MyApp.preflogin[PREF_PROFILE_IMAGE] = it[0].data[0].profileImage
                    MyApp.preflogin[PREF_COUNTRY_CODE] = it[0].data[0].countryCode
                    openActivity(OtpVerificationActivity::class.java) {
                        putString(OTP_CONTAINER, it[0].data[0].otp)
                        putSerializable(OTP_ENUM_CONTAINER, OtpTypeEnum.OTP_REGISTER)

                    }
                }
                EMAIL_ALLREADY_EXISTS -> {
                    toast(it[0].message)
                }
                FAILURE -> {
                    toast(it[0].message)
                }
                MOBILE_NUMBER_ALLREADY_EXISTS -> {
                    toast(it[0].message)
                }
                USER_NOT_FOUND -> {
                    toast(it[0].message)
                }
            }
        })
                }

    private fun onClick() {
        signUpBTN.setOnClickListener { 
            val msg = isValidDetail()

            if (msg.isEmpty()){

                if (pNumberET.text.toString().trim().contains(" ")) {
                    val numberArray = pNumberET.text.toString().trim().split(" ")

                    mobileNo = countryCode + numberArray[numberArray.lastIndex]
                } else {
                    mobileNo = countryCode + pNumberET.text.toString().trim()
                }

//                if  (tosCheckBox.isChecked){
//                    openActivity(PairDeviceActivity::class.java)
//                }else{
//                    toast("Please Accpet Terms & conditions And Privacy policy.")
//
//                }

                callUserRegisterApi(
                    filePath!!,
                    fileName,
                    fNameET.text.toString().trim(),
                    lNameET.text.toString().trim(),
                    eaddressET.text.toString().trim(),
                    pNumberET.text.toString().trim(),
                    passwordEET.text.toString().trim(),
                    "1",
                    countryCode
                )
            }
            else{
                toast(msg)
            }

//            openActivity(SignInActivity::class.java)
        }
        alreadyHaveAccountTTV.setOnClickListener {
            openActivity(SignInActivity::class.java)
        }

    }

    private fun isValidDetail(): String {
        var msg=""

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
            !isEmailValid(eaddressET.text.toString()) -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_EMAILADDRESS,
                    getString(R.string.email_address_isvalid)
                )

            }
            mobileNo == "" -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PHONE,
                    getString(R.string.phone_can_not_blank)
                )

            }
//            vPNumberET.text.toString().trim().isEmpty() -> {
//                msg = LabelUtils.getLabel(this, PLEASE_ENTER_VEHICLE_PLATE_NUMBER, getString(R.string.vehicle_plate_number_cant_blanck))
//
//            }
            passwordEET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )

            }
            passwordEET.text.toString().trim().length <= 3 ->{
                msg = LabelUtils.getLabel(
                    this, PLEASE_ENTER_MORE_THAN_FOUR_DIGIT_PASSWORD, getString(
                        R.string.password_can_not_less_then_4_blank
                    )
                )
            }
            !tosCheckBox.isChecked ->{
                msg = "Please Accpet Terms & conditions And Privacy policy."
            }


//            if ((password.getText().toString().length() < 8) || (reEnteredPassword.getText().toString().length() < 8)) {
//// less than 8 characters error message
//            }
        }
        return msg
    }
    private fun setData() {
        passwordEET.setOnTouchListener(this)
        editImage.setOnClickListener(this)
        alreadyHaveAccountTTV.setOnClickListener(this)
        termsTV.setOnClickListener(this)
        privacyTV.setOnClickListener(this)

       // countryCode = countryCodePicker.selectedCountryCodeWithPlus
//        val countryCode: String = pNumberET.resources.getConfiguration().locale.getCountry()

      //  pNumberET.setText("$countryCode ")

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
                if (!s.toString().startsWith(countryCodePicker.selectedCountryCodeWithPlus + " ")) {
//                    pNumberET.setText(countryCodePicker.selectedCountryCodeWithPlus + " ")
//                    Selection.setSelection(pNumberET.getText(), pNumberET.getText()!!.length)
                }
                mobileNo = s.toString()
            }
        })




    }

    private fun setLable() {
        signUpLable.text = LabelUtils.getLabel(
            this,
            USER_REGISTRATION,
            getString(R.string.user_registration)
        )
        firstNameTV.text = LabelUtils.getLabel(this, FIRST_NAME, getString(R.string.first_name))
        lastNameTV.text = LabelUtils.getLabel(this, LASTNAME, getString(R.string.last_name))
        emailAddressTV.text = LabelUtils.getLabel(
            this,
            EMAIL_ADDRESS,
            getString(R.string.email_address)
        )
        mobileNumberTV.text = LabelUtils.getLabel(
            this,
            PHONE_NUMBER,
            getString(R.string.phone_number)
        )
//        vPNumberLT.hint = LabelUtils.getLabel(this, VEHICLE_PLATE_NUMBER,getString(R.string.vehicle_plate_number))
        passwordTV.text = LabelUtils.getLabel(this, PASSWORD, getString(R.string.password))
        signUpBTN.text = LabelUtils.getLabel(this, SUBMIT, getString(R.string.submit))
//        dontHaveAnAccountTV.text = LabelUtils.getLabel(this, ALREADY_HAVE_ACCOUNT,getString(R.string.already_have_account))

    }
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(
            LabelUtils.getLabel(
                this@CustomerRegisterActivity,
                SELECT_OPTION,
                R.string.select_option
            )
        )
        val pictureDialogItems = arrayOf(
            LabelUtils.getLabel(this@CustomerRegisterActivity, GALLERY, R.string.gallery),
            LabelUtils.getLabel(this@CustomerRegisterActivity, CAMERA, R.string.camera)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.editImage -> {
                PermissionUtils.checkForPermission(this@CustomerRegisterActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,


                    myCallBack = {
                        if (it) showPictureDialog()
                    })
            }
            R.id.termsTV -> {
                openActivityForResult(TermConditionActivity::class.java, LOCATION_PICKER_CODE)
            }
            R.id.privacyTV -> {
                openActivityForResult(PrivacyPolicyActivity::class.java, LOCATION_PICKER_CODE)
            }
        }
    }

    private fun callUserRegisterApi(

//        val map: HashMap<String, Any> = HashMap()
//
//        map[PARAM_EMAIL] = eaddressET.text.toString().trim()
//        map[PARAM_PASSWORD] = passwordEET.text.toString().trim()
//        map[PARAM_FIRST_NAME] = fNameET.text.toString().trim()
//        map[PARAM_LAST_NAME] = lNameET.text.toString().trim()
//        map[PARAM_MOBILE] = pNumberET.text.toString().trim()
//        map[PARAM_DEVICE_TYPE] = "1"
//
//        userregisterViewModel.CallUserRegisterApi(map)

        filePath: String,
        fileName: String,
        FirstName: String,
        lastName: String,
        email: String,
        mobile: String,
        password: String,
        deviceType: String,
        countrycode: String
    ){

        val params: MutableMap<String, RequestBody> = HashMap()
            params.put(PARAM_FIRST_NAME, ApiClient.getRequestBody(FirstName))
            params.put(PARAM_LAST_NAME, ApiClient.getRequestBody(lastName))
            params.put(PARAM_EMAIL, ApiClient.getRequestBody(email))
            params.put(PARAM_MOBILE, ApiClient.getRequestBody(mobile))
            params.put(PARAM_PASSWORD, ApiClient.getRequestBody(password))
            params.put(PARAM_DEVICE_TYPE, ApiClient.getRequestBody(deviceType))
       // params.put(PARAM_COUNTRY_CODE, ApiClient.getRequestBody(countryCode))


        userregisterViewModel.CallUserRegisterApi(params, Util.getFileToUpload(PARAM_PROFILE_IAMGE, filePath, fileName
            )
        )



        }



    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.backIV -> finish()

            R.id.passwordEET -> Validation.TansformPassword(event, passwordEET)
        }
        return false
    }
}
