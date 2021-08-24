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
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
import com.Towrevo.ui.datamodel.CategoryDataModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.CompanyEditProfileViewModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.openActivityForResult
import com.Towrevo.util.extension.toast
import com.Towrevo.util.extension.visible
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_company_edit_profile.*
import kotlinx.android.synthetic.main.activity_company_edit_profile.aboutET
import kotlinx.android.synthetic.main.activity_company_edit_profile.aboutUSTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.addressET
import kotlinx.android.synthetic.main.activity_company_edit_profile.addressTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.browseImageIV
import kotlinx.android.synthetic.main.activity_company_edit_profile.categoryTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.companyNameET
import kotlinx.android.synthetic.main.activity_company_edit_profile.companyNameTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.countryCodePicker
import kotlinx.android.synthetic.main.activity_company_edit_profile.eaddressET
import kotlinx.android.synthetic.main.activity_company_edit_profile.emailAddressTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.mainLL
import kotlinx.android.synthetic.main.activity_company_edit_profile.mobileNumberTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.noInternetView
import kotlinx.android.synthetic.main.activity_company_edit_profile.pNumberET
import kotlinx.android.synthetic.main.activity_company_edit_profile.passwordTV
import kotlinx.android.synthetic.main.activity_company_edit_profile.profileImageIV
import kotlinx.android.synthetic.main.activity_company_edit_profile.registerBT
import kotlinx.android.synthetic.main.activity_company_edit_profile.workingHourET
import kotlinx.android.synthetic.main.activity_company_edit_profile.workingHourTV
import kotlinx.android.synthetic.main.popup_category.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import okhttp3.RequestBody

class CompanyEditProfileActivity : AppCompatActivity(),View.OnClickListener {

    private var photoURI: Uri? = null
    private val imgCount = 1
    private var categoryList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var selectedList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var tempCategoryList: MutableList<CategoryDataModel.Data> = arrayListOf()
    private var categoryId = ArrayList<String>()

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
    private var place_name =""
    private var countryCode: String = ""
    var filePath: String? = ""
    var fileName = ""
    private var mobileNo=""


    private val companyEditProfileViewModel: CompanyEditProfileViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory {
                CompanyEditProfileViewModel(
                    this,
                    mainLL,
                    noInternetView
                )
            }).get(
            CompanyEditProfileViewModel::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_edit_profile)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        callViewProfileApi()
        setLabel()
        handleApi()
        callCategoryApi()
        backIV.setOnClickListener { finish() }
    }
    private fun setData(){
        browseImageIV.setOnClickListener(this)
        editImageFL.setOnClickListener(this)
        addressET.setOnClickListener(this)
        registerBT.setOnClickListener(this)
        categoryTV.setOnClickListener(this)


  //      countryCode = countryCodePicker.defaultCountryCode
        countryCode = countryCode


      //  pNumberET.setText("+" + countryCodePicker.defaultCountryCode + " ")

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
    private fun setLabel(){
        titleBarTV.text = LabelUtils.getLabel(this, EDIT_PROFILE,R.string.edit_profile)
        companyNameTV.text = LabelUtils.getLabel(this, COMPANY_NAME,R.string.company_name)
        aboutUSTV.text = LabelUtils.getLabel(this, ABOUT_US,R.string.about)
        workingHourTV.text = LabelUtils.getLabel(this, WORKING_HOUR,R.string.working_hour)
        addressTV.text = LabelUtils.getLabel(this, ADDRESS,R.string.address)
        emailAddressTV.text = LabelUtils.getLabel(this, EMAIL_ADDRESS,R.string.email_address)
        mobileNumberTV.text = LabelUtils.getLabel(this, MOBILE_NUMBER,R.string.phone_number)
        passwordTV.text = LabelUtils.getLabel(this, PASSWORD,R.string.password)
        registerBT.text = LabelUtils.getLabel(this, SAVE,R.string.save)
    }
    private fun callViewProfileApi() {

        val map: HashMap<String, Any> = HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
        map[PARAM_DEVICE_TYPE] = "1"

        companyEditProfileViewModel.callViewProfileApi(map)

    }
    private fun handleApi() {
        companyEditProfileViewModel.companyeditProfileResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    //Company details
                   // MyApp.preflogin[PREF_IS_LOGIN] = true
                    MyApp.preflogin[PREF_LOGIN_TYPE] = ""
                    MyApp.preflogin[PREF_FIRST_NAME] = it[0].data[0].firstName
                    MyApp.preflogin[PREF_LAST_NAME] = it[0].data[0].lastName
                    MyApp.preflogin[PREF_USER_ID] = it[0].data[0].userId
                    MyApp.preflogin[PREF_TOKEN] = it[0].data[0].token
                    MyApp.preflogin[PREF_USER_TYPE] = it[0].data[0].userType
                    MyApp.preflogin[PREF_PROFILE_IMAGE] = it[0].data[0].profileImage
                    MyApp.preflogin[PREF_EMAIL] = it[0].data[0].email
                    MyApp.preflogin[PREF_MOBILE] = it[0].data[0].mobile
                    MyApp.preflogin[PREF_COMPANY_NAME] = it[0].data[0].companyName

                    openActivity(CompanyHomeActivity::class.java)
                }
                FAILURE -> {
                    toast(it[0].message)
                }


            }
        })

        companyEditProfileViewModel.viewProfileResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {
                    mainLL.visible()

                    companyNameET.setText(it[0].data[0].companyName)
                    aboutET.setText(it[0].data[0].about)
                    workingHourET.setText(it[0].data[0].workingHour)
                    addressET.setText(it[0].data[0].address)
                    eaddressET.setText(it[0].data[0].email)
                    pNumberET.setText("+"+it[0].data[0].mobile)
                    sourceLat=it[0].data[0].latitude
                    sourceLong=it[0].data[0].longitude
                    Glide.with(this).load(it[0].data[0].companyProfileImage).placeholder(R.drawable.ic_company_image).into(profileImageIV)
                }
                FAILURE -> {
                    toast(it[0].message)
                }
            }
        })
        companyEditProfileViewModel.categoryDataResponseLiveData.observe(this, Observer {
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

    private fun callCategoryApi() {

        val map: java.util.HashMap<String, Any> = java.util.HashMap()
        map[PARAM_TOKEN] = PreferenceHelper.getToken()
        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
        companyEditProfileViewModel.callCategoryDataApi(map)
    }

    private fun callCompanyEditProfileApi(
        filePath: String, fileName: String, companyName: String,
        email: String, pNumber: String, address: String, aboutUs: String, workingHour: String,
        lat: String, long: String, deviceType: String, category_id: String){

        /*     val map: HashMap<String, Any> = HashMap()
             map[PARAM_COMPANY_NAME] = companyNameET.text.toString().trim()
             map[PARAM_EMAIL] = eaddressET.text.toString().trim()
             map[PARAM_MOBILE] = pNumberET.text.toString().trim()
             map[PARAM_ADDRESS] = addressET.text.toString().trim()
             map[PARAM_ABOUT] = aboutET.text.toString().trim()
             map[PARAM_WORKING_HOUR] = workingHourET.text.toString().trim()
             map[PARAM_LATITTUDE] = "23.0747"
             map[PARAM_LONGITUDE] = "72.5256"
             map[PARAM_DEVICE_TYPE] = "1"
             map[PARAM_CATEGORY_ID] = "1"
             map[PARAM_PASSWORD] = passwordET.text.toString().trim()


             companyregisterViewModel.callCreateUserApi(map)*/


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
        params.put(PARAM_USER_TYPE, ApiClient.getRequestBody(PreferenceHelper.getUserType()))
        params.put(PARAM_TOKEN, ApiClient.getRequestBody(PreferenceHelper.getToken()))
        params.put(PARAM_USER_ID, ApiClient.getRequestBody(PreferenceHelper.getUserID()))

        companyEditProfileViewModel.callCompanyEditProfileApi(params, Util.getFileToUpload(PARAM_PROFILE_IAMGE, filePath, fileName))

    }


//        val password = passwordET.text.toString().trim()
//        val map: HashMap<String, Any> = HashMap()
//        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
//        map[PARAM_COMPANY_NAME] = companyNameET.text.toString().trim()
//        map[PARAM_EMAIL] = eaddressET.text.toString().trim()
//        map[PARAM_MOBILE] = pNumberET.text.toString().trim()
//        map[PARAM_ADDRESS] = addressET.text.toString().trim()
//        map[PARAM_ABOUT] = aboutET.text.toString().trim()
//        map[PARAM_WORKING_HOUR] = workingHourET.text.toString().trim()
//        map[PARAM_PASSWORD] = PreferenceHelper.getPassword()
//        map[PARAM_DEVICE_TYPE] = "1"
//        map[PARAM_TOKEN] = PreferenceHelper.getToken()
//        map[PARAM_USER_TYPE] = PreferenceHelper.getUserType()
//        map[PARAM_DEVICE_TOKEN] = PreferenceHelper.getDeviceToken()
//        map[PARAM_LATITTUDE] = "23.565621"
//        map[PARAM_LONGITUDE] = "72.123132"
//        if (password.isEmpty()) {
//            map[PARAM_PASSWORD] = PreferenceHelper.getPassword()
//        } else {
//            map[PARAM_PASSWORD] = password
//
//        }
//
//        companyEditProfileViewModel.callCompanyEditProfileApi(map)

//    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.registerBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {

                    if (pNumberET.text.toString().trim().contains(" ")) {
                        val numberArray = pNumberET.text.toString().trim().split(" ")

                        mobileNo = countryCode + numberArray[numberArray.lastIndex]
                    } else {
                        mobileNo = countryCode + pNumberET.text.toString().trim()
                    }


                    callCompanyEditProfileApi(filePath!!, fileName, companyNameET.text.toString().trim(), eaddressET.text.toString().trim(),
                        mobileNo, addressET.text.toString().trim(),
                        aboutET.text.toString().trim(),workingHourET.text.toString().trim(),sourceLat,sourceLong,
                        "1","1"
                    )
                } else {
                    toast(msg)
                }
            }
            R.id.categoryTV -> {
                val dialog = Dialog(this)
                //set layout custom
                //set layout custom
                dialog.setContentView(R.layout.popup_category)
                selectedList.clear()

                val rvcaddy =
                    dialog.findViewById<View>(R.id.categoryREC) as RecyclerView

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
                        selectedList.add(
                            CategoryDataModel.Data(
                                data.categoryId,
                                data.categoryName,
                                data.isChecked
                            )
                        )
                        Log.e("list", Gson().toJson(selectedList))
                    } else {

                        for (i in selectedList.indices) {
                            if (data.categoryId == selectedList.get(i).categoryId) {
                                selectedList.removeAt(i)
                                break
                            }
                        }
                        Log.e("list1111", Gson().toJson(selectedList))

                    }
                })
                // Add some item here to show the list.
                // Add some item here to show the list.
                rvcaddy.adapter = adapter
                val mLayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(applicationContext)
                rvcaddy.layoutManager = mLayoutManager
                dialog.continuueBtn.setOnClickListener {
                    /* paramCategoryId=categoryId.joinToString()

  //                    for( )*/
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
            R.id.addressET -> {
                openActivityForResult(LocationPickerActivity::class.java, LOCATION_PICKER_CODE)
            }
            R.id.editImageFL -> {
                PermissionUtils.checkForPermission(this@CompanyEditProfileActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,


                    myCallBack = {
                            if (it) showPictureDialog()
                        })
            }
            R.id.backIV -> {
                finish()
            }
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
            addressET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_ADDRESS,
                    getString(R.string.address_can_not_blank)
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

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(
                LabelUtils.getLabel(
                        this@CompanyEditProfileActivity,
                        SELECT_OPTION,
                        R.string.select_option
                )
        )
        val pictureDialogItems = arrayOf(
                LabelUtils.getLabel(this@CompanyEditProfileActivity, GALLERY, R.string.gallery),
                LabelUtils.getLabel(this@CompanyEditProfileActivity, CAMERA, R.string.camera)
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
            Glide.with(this).load(obtainResult[0]).into(profileImageIV)
            photoURI = obtainResult[0]
            filePath = Util.getRealPathFromURI_API19(applicationContext, photoURI!!)
            fileName = Util.generateImageName()
            profileImageIV.setImageURI(obtainResult[0])
            Log.e("filePath", filePath.toString())
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
//            Glide.with(this).load(photoURI).into(profileImageIV)
            profileImageIV.setImageURI(photoURI)
            filePath = currentPhotoPath+
            Log.e("filePath", filePath.toString())
            fileName = Util.generateImageName()
        }

        if (requestCode == LOCATION_PICKER_CODE && resultCode == RESULT_OK && data != null) {

            address = data.getSerializableExtra(ADDRESS_CONTAINER) as String
            sourceLat = data.getSerializableExtra(LAT_CONTAINER) as String
            sourceLong = data.getSerializableExtra(LONG_CONTAINER) as String
            place_name = address

            addressET.setText(address)

        }
    }

}