package com.Towrevo.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demomvvm.network.ApiClient
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.Towrevo.R
import com.Towrevo.application.MyApp
import com.Towrevo.labels.*
import com.Towrevo.labels.PASSWORD
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.adapter.AdapterCategory
import com.Towrevo.ui.cms.PrivacyPolicyActivity
import com.Towrevo.ui.cms.TermConditionActivity
import com.Towrevo.ui.datamodel.CategoryDataModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CompanyRegisterViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.openActivityForResult
import com.Towrevo.util.extension.toast
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_company_registeration.*
import kotlinx.android.synthetic.main.activity_company_registeration.countryCodePicker
import kotlinx.android.synthetic.main.activity_company_registeration.eaddressET
import kotlinx.android.synthetic.main.activity_company_registeration.editImage
import kotlinx.android.synthetic.main.activity_company_registeration.emailAddressTV
import kotlinx.android.synthetic.main.activity_company_registeration.mainLL
import kotlinx.android.synthetic.main.activity_company_registeration.mobileNumberTV
import kotlinx.android.synthetic.main.activity_company_registeration.noInternetView
import kotlinx.android.synthetic.main.activity_company_registeration.pNumberET
import kotlinx.android.synthetic.main.activity_company_registeration.passwordTV
import kotlinx.android.synthetic.main.popup_category.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import okhttp3.RequestBody


class CompanyRegisterationActivity : AppCompatActivity(), View.OnClickListener,
    View.OnTouchListener {

    private var categoryList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var selectedList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var tempCategoryList: MutableList<CategoryDataModel.Data> = arrayListOf()

    private var photoURI: Uri? = null
    private val imgCount = 1
    private var isCamera: Boolean = false
    private lateinit var currentPhotoPath: String
    private var centerLatLng: LatLng? = null
    private var currentLat: String = ""
    private var currentLong: String = ""
    private var currentAddress = ""
    private var currentPlace = ""
    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var place_name = ""
    private var countryCode: String = "+1"
    var filePath: String? = ""
    var fileName = ""
    private var mobileNo = ""
    private var categoryId = ArrayList<String>()
    private var categoryIdParam = ArrayList<String>()
    private var paramCategoryId = ""
    private var selectCategory = ""
    var country: Country? = null
    var selectedCategory = ""


    private val companyregisterViewModel: CompanyRegisterViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { CompanyRegisterViewModel(this, mainLL, noInternetView) }).get(
            CompanyRegisterViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_registeration)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        callCategoryApi()
        handleApi()
        backIV.setOnClickListener { finish() }
    }

    private fun setScanList() {

        val scanListAdapter = AdapterCategory(
            this,
            categoryList,
            onItemCheckedCallback = { i: Int, data: CategoryDataModel.Data ->

            })
        categoryREC.adapter = scanListAdapter
    }


    private fun handleApi() {
        companyregisterViewModel.companyRegisterResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    toast(it[0].message)
                    MyApp.preflogin[PREF_PASSWORD] = passwordEET.text.toString().trim()
                    MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                    MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                    MyApp.preflogin[PREF_COMPANY_NAME] = it[0].data[0].companyName
                    MyApp.preflogin[PREF_ADDRESS] = it[0].data[0].address
                    MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                    MyApp.preflogin[PREF_WORKING_HOUR] = it[0].data[0].workingHour
                    MyApp.preflogin[PREF_ABOUT] = it[0].data[0].about
                    MyApp.preflogin[PREF_IS_LOGIN] = true
                    //  MyApp.preflogin[PREF_CATEGORY_ID] = it[0].data[0].category
                    MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                    MyApp.preflogin[PREF_USER_TOKEN] = it[0].data[0].token
                    MyApp.preflogin[PREF_PROFILE_IMAGE] = it[0].data[0].profileImage
                    MyApp.preflogin[PREF_COUNTRY_CODE] = it[0].data[0].countryCode
                    MyApp.preflogin[PARAM_ABOUT] = it[0].code
                    openActivity(OtpVerificationActivity::class.java) {
                        putString(OTP_CONTAINER, it[0].data[0].otp)
                        putSerializable(OTP_ENUM_CONTAINER, OtpTypeEnum.OTP_REGISTER)
                    }
                }
                EMAIL_ALLREADY_EXISTS -> {
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
        companyregisterViewModel.categoryDataResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    categoryList.addAll(it[0].data)

                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }
        })
    }


    private fun callCompanyRegisterApi(
        filePath: String,
        fileName: String,
        companyName: String,
        email: String,
        pNumber: String,
        address: String,
        aboutUs: String,
        workingHour: String,
        lat: String,
        long: String,
        deviceType: String,
        category_id: String,
        countrycode: String,
        password: String

    ) {

        val params: MutableMap<String, RequestBody> = HashMap()
        params.put(PARAM_COMPANY_NAME, ApiClient.getRequestBody(companyName))
        params.put(PARAM_EMAIL, ApiClient.getRequestBody(email))
        params.put(PARAM_MOBILE, ApiClient.getRequestBody(pNumber))
        params.put(PARAM_ADDRESS, ApiClient.getRequestBody(address))
        params.put(PARAM_ABOUT, ApiClient.getRequestBody(aboutUs))
        params.put(PARAM_WORKING_HOUR, ApiClient.getRequestBody(workingHour))
        params.put(PARAM_LATITTUDE, ApiClient.getRequestBody(lat))
        params.put(PARAM_LONGITUDE, ApiClient.getRequestBody(long))
        params.put(PARAM_DEVICE_TYPE, ApiClient.getRequestBody(deviceType))
        params.put(PARAM_CATEGORY_ID, ApiClient.getRequestBody(category_id))
        params.put(PARAM_PASSWORD, ApiClient.getRequestBody(password))
        params.put(PARAM_COUNTRY_CODE, ApiClient.getRequestBody(countryCode))

        companyregisterViewModel.callCreateUserApi(
            params, Util.getFileToUpload(
                PARAM_PROFILE_IAMGE,
                filePath,
                fileName
            )
        )

    }

    private fun setData() {
        editImage.setOnClickListener(this)
        backIV.setOnClickListener(this)
        registerBT.setOnClickListener(this)
        addressET.setOnClickListener(this)
        categoryTV.setOnClickListener(this)
        signUpLoginTV.setOnClickListener(this)
        passwordEET.setOnTouchListener(this)
        alreadyHaveAccountTV.setOnClickListener(this)
        termsTV.setOnClickListener(this)
        privacyTV.setOnClickListener(this)

//        countryCode = countryCodePicker.defaultCountryCode

//        country = Util.getUserCountryInfo(this)
//        if (country != null){
//            countryCode=country!!.dialCode
//            pNumberET.setText(country!!.dialCode+" -")
////            if (countryCode.equals(COUNTRY_CODE_KUWAIT)) {
////                mobileLimit = 9
////            } else if (countryCode.equals(COUNTRY_CODE_SAUDI)) {
////                mobileLimit = 9
////            }
//
//        }
//
////        pNumberET.setText("+" + countryCodePicker.defaultCountryCode + " ")
//
//        pNumberET.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(
//                s: CharSequence,
//                start: Int,
//                before: Int,
//                count: Int
//            ) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int, count: Int,
//                after: Int
//            ) {
//                // TODO Auto-generated method stub
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                if (!s.toString().startsWith(countryCode + " ")) {
//                    pNumberET.setText(countryCode + " ")
//                    Selection.setSelection(pNumberET.getText(), pNumberET.getText()!!.length)
//                }
//                mobileNo = s.toString()
//            }
//        })

        //    countryCode = countryCodePicker.selectedCountryCodeWithPlus
//        val countryCode: String = pNumberET.resources.getConfiguration().locale.getCountry()

        //     pNumberET.setText("$countryCode ")

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
                    //  Selection.setSelection(pNumberET.getText(), pNumberET.getText()!!.length)
                }
                mobileNo = s.toString()
            }
        })


    }

    private fun setLabel() {
        titleBarTV.text =
            LabelUtils.getLabel(this, COMPANY_REGISTRATION, R.string.company_registration)
        companyNameTV.text = LabelUtils.getLabel(this, COMPANY_NAME, R.string.company_name)
        aboutUSTV.text = LabelUtils.getLabel(this, ABOUT, R.string.about)
        workingHourTV.text = LabelUtils.getLabel(this, WORKING_HOUR, R.string.working_hour)
        addressTV.text = LabelUtils.getLabel(this, ADDRESS, R.string.address)
        emailAddressTV.text = LabelUtils.getLabel(this, EMAIL_ADDRESS, R.string.email_address)
        mobileNumberTV.text = LabelUtils.getLabel(this, MOBILE_NUMBER, R.string.phone_number)
        passwordTV.text = LabelUtils.getLabel(this, PASSWORD, R.string.password)
        registerBT.text = LabelUtils.getLabel(this, REGISTER, R.string.register)

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(
            LabelUtils.getLabel(
                this@CompanyRegisterationActivity,
                SELECT_OPTION,
                R.string.select_option
            )
        )
        val pictureDialogItems = arrayOf(
            LabelUtils.getLabel(this@CompanyRegisterationActivity, GALLERY, R.string.gallery),
            LabelUtils.getLabel(this@CompanyRegisterationActivity, CAMERA, R.string.camera)
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

    private fun showDialog2() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
//        window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.setContentView(R.layout.popup_category)

        val scanListAdapter = AdapterCategory(
            this,
            categoryList,
            onItemCheckedCallback = { i: Int, data: CategoryDataModel.Data ->


            })
        dialog.categoryREC.adapter = scanListAdapter
//        dialog.yesBtn1.setOnClickListener {
//            openActivity(DeliveredSuccessfulActivity::class.java)
//            dialog.dismiss()
//        }
//        dialog.noBtn1.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun callCategoryApi() {

        val map: java.util.HashMap<String, Any> = java.util.HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        companyregisterViewModel.callCategoryDataApi(map)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICKER_CODE && resultCode == Activity.RESULT_OK) {

            // get if single image selected
            val obtainResult = Matisse.obtainResult(data)
            photoURI = obtainResult[0]
            filePath = Util.getRealPathFromURI_API19(applicationContext, photoURI!!)
            fileName = Util.generateImageName()


            profileImageIV.setImageURI(obtainResult[0])
            Log.e("filePath", filePath.toString())
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {

            profileImageIV.setImageURI(photoURI)
            filePath = currentPhotoPath
            Log.e("filePath", filePath.toString())
            fileName = Util.generateImageName()
            //  UploadImage(mutableListOf(photoURI!!),true,currentPhotoPath,this,true,uploadCompleteCallback = {Log.d("upload name--",it)})
        }

        if (requestCode == LOCATION_PICKER_CODE && resultCode == RESULT_OK && data != null) {

            address = data.getSerializableExtra(ADDRESS_CONTAINER) as String
            sourceLat = data.getSerializableExtra(LAT_CONTAINER) as String
            sourceLong = data.getSerializableExtra(LONG_CONTAINER) as String
            place_name = address

            addressET.setText(address)

//            filePath = currentPhotoPath
            //          Log.e("filePath", filePath.toString())
            //        fileName = Util.generateImageName()
        }
    }

    private fun isValidDetail(): String {
        var msg = ""

        when {
            companyNameET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_COMPANY_NAME,
                    getString(R.string.companyname_can_not_blank)
                )

            }
            aboutET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_ABOUT_US,
                    getString(R.string.aboutus_can_not_blank)
                )

            }
            workingHourET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_WORKINGHOURS,
                    getString(R.string.working_hours_cant_be_blank)
                )

            }
            addressET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_ADDRESS,
                    getString(R.string.address_can_not_blank)
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
            mobileNo == "" -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PHONE,
                    getString(R.string.phone_can_not_blank)
                )

            }

            passwordEET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )

            }
            passwordEET.text.toString().trim().length <= 3 -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_MORE_THAN_FOUR_DIGIT_PASSWORD,
                    getString(R.string.password_can_not_less_then_4_blank)
                )
            }


            categoryIdTV.text.isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_SELECT_CATEGORY,
                    R.string.please_select_category
                )
            }
            passwordEET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )
            }
        }
        return msg
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.backIV -> finish()

            R.id.passwordEET -> Validation.TansformPassword(event, passwordEET)
        }
        return false
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.registerBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {

                    if (pNumberET.text.toString().trim().contains(" ")) {
                        val numberArray = pNumberET.text.toString().trim().split(" ")

                        mobileNo = numberArray[numberArray.lastIndex]
                    } else {
                        mobileNo = pNumberET.text.toString().trim()
                    }

                    selectedCategory = categoryIdParam.toString()


                    //    callCompanyRegisterApi()
                    callCompanyRegisterApi(
                        filePath!!,
                        fileName,
                        companyNameET.text.toString().trim(),
                        eaddressET.text.toString().trim(),
                        pNumberET.text.toString().trim(),
                        addressET.text.toString().trim(),
                        aboutET.text.toString().trim(),
                        workingHourET.text.toString().trim(),
                        sourceLat,
                        sourceLong,
                        "1",
                        selectedCategory
                            .substring(1, selectedCategory.length - 1),
                        countryCode,
                        passwordEET.text.toString().trim()
                    )
                } else {
                    toast(msg)
                }

//            openActivity(SignInActivity::class.java)
            }
            R.id.addressET -> {
                openActivityForResult(LocationPickerActivity::class.java, LOCATION_PICKER_CODE)
            }
//            R.id.alreadyHaveAccountTV -> {
//                openActivityForResult(SignInActivity::class.java, LOCATION_PICKER_CODE)
//            }
            R.id.termsTV -> {
                openActivityForResult(TermConditionActivity::class.java, LOCATION_PICKER_CODE)
            }
            R.id.privacyTV -> {
                openActivityForResult(PrivacyPolicyActivity::class.java, LOCATION_PICKER_CODE)
            }
            R.id.signUpLoginTV -> {
                openActivityForResult(SignInActivity::class.java, LOCATION_PICKER_CODE)
            }
            R.id.categoryTV -> {
                val dialog = Dialog(this)
                //set layout custom
                //set layout custom
                dialog.setContentView(R.layout.popup_category)
                selectedList.clear()

                val rvcaddy = dialog.findViewById<View>(R.id.categoryREC) as RecyclerView

                val adapter = AdapterCategory(this, categoryList, onItemClicked = {
                    if (!tempCategoryList.contains(categoryList[it])) {
                        tempCategoryList.add(categoryList[it])
                    }
                    categoryId.add(categoryList[it].categoryId)
                }, onRemoveItemClick = {
                    if (tempCategoryList.contains(categoryList[it])) {
                        tempCategoryList.remove(categoryList[it])
                    }
                    categoryId.remove(categoryList[it].categoryId)

                }, onItemCheckedCallback = { i: Int, data: CategoryDataModel.Data ->

                    if (data.isChecked!!) {
                        if (!categoryIdParam.contains(data.categoryId)) {
                            categoryIdParam.add(data.categoryId)
                        }
                        selectedList.add(
                            CategoryDataModel.Data(
                                data.categoryId,
                                data.categoryName,
                                data.isChecked

                            )
                        )
                        Log.e("list", Gson().toJson(selectedList))
                    } else {
                        if (categoryIdParam.contains(data.categoryId)) {
                            categoryIdParam.remove(data.categoryId)
                        }

                        for (i in selectedList.indices) {
                            if (data.categoryId == selectedList.get(i).categoryId) {
                                selectedList.removeAt(i)
                                break
                            }
                        }
                        Log.e("list1111", Gson().toJson(selectedList))
                    }
                })

                rvcaddy.adapter = adapter
                val mLayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(applicationContext)
                rvcaddy.layoutManager = mLayoutManager
                dialog.continuueBtn.setOnClickListener {

                    var categoryName = arrayListOf<String>()
                    categoryIdTV.text = ""
                    var selectTitle: String = ""
                    for (i in selectedList) {
                        if (selectTitle.equals("")) {
                            selectTitle = i.categoryName
                        } else {
                            selectTitle = selectTitle + ", " + i.categoryName
                        }
                    }
                    categoryIdTV.text = selectTitle
                    dialog.hide()
                }
                dialog.show()
            }
            R.id.editImage -> {
                PermissionUtils.checkForPermission(this@CompanyRegisterationActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,

                    myCallBack = {
                        if (it) showPictureDialog()
                    })
            }
        }
    }
    }


