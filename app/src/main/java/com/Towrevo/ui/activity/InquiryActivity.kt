package com.Towrevo.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.demomvvm.network.ApiClient
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.network.*
import com.Towrevo.preference.*
import com.Towrevo.ui.`interface`.OnItemCallback
import com.Towrevo.ui.adapter.ImageGridAdapter
import com.Towrevo.ui.datamodel.ImageGridModel
import com.Towrevo.ui.datamodel.ImageUploadModel
import com.Towrevo.ui.viewmodel.BaseViewModelFactory
import com.Towrevo.ui.viewmodel.UserInquiryViewDataModel
import com.Towrevo.util.*
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_inquiry.*
import kotlinx.android.synthetic.main.activity_inquiry.eaddressET
import kotlinx.android.synthetic.main.activity_inquiry.emailAddressTV
import kotlinx.android.synthetic.main.activity_inquiry.fNameET
import kotlinx.android.synthetic.main.activity_inquiry.firstNameTV
import kotlinx.android.synthetic.main.activity_inquiry.lNameET
import kotlinx.android.synthetic.main.activity_inquiry.lastNameTV
import kotlinx.android.synthetic.main.activity_inquiry.mobileNumberTV
import kotlinx.android.synthetic.main.activity_inquiry.noInternetView
import kotlinx.android.synthetic.main.activity_inquiry.pNumberET
import kotlinx.android.synthetic.main.activity_user_sign_up.*
import kotlinx.android.synthetic.main.activity_user_sign_up.countryCodePicker
import kotlinx.android.synthetic.main.item_image_grid.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class InquiryActivity : AppCompatActivity(), View.OnClickListener, OnItemCallback {

    private var sourceLat = ""
    private var sourceLong = ""
    private var address = ""
    private var company_id = ""
    private var photoURI: Uri? = null
    private val imgCount = 1
    private var imgAddPos = 0
    private lateinit var currentPhotoPath: String
    private var countryCode: String = ""
    private var imageList: MutableList<ImageGridModel> = arrayListOf()
    private var imageUpload: MutableList<ImageUploadModel> = arrayListOf()
    private val images: MutableList<String> = arrayListOf()
    private lateinit var imageListAdapter: ImageGridAdapter
    private var isNewImageAdded = false
    var filePath: String? = ""
    var fileName = ""
    /*var surveyImagesParts = arrayListOf<MultipartBody.Part>()*/


    var parts:List<MultipartBody.Part> = ArrayList()
    private val userInquiryViewDataModel: UserInquiryViewDataModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModelFactory { UserInquiryViewDataModel(this, mailLL, noInternetView) }).get(
            UserInquiryViewDataModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inquiry)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setData()
        setLabel()
        setBlockList()
        handleApi()
        backIV.setOnClickListener { finish() }
        if (intent.hasExtra(ADDRESS_CONTAINER)) {
            address = intent.getStringExtra(ADDRESS_CONTAINER) as String
            sourceLat = intent.getStringExtra(LAT_CONTAINER) as String
            sourceLong = intent.getStringExtra(LONG_CONTAINER) as String
            company_id = intent.getStringExtra(COMPANY_CONTAINER) as String

        }
    }

    private fun setBlockList() {
//        imageList.add(
//            ImageGridModel("","", true)
//        )

        imageListAdapter = ImageGridAdapter(this, imageList, this)

        imagesREC.adapter = imageListAdapter
    }


    private fun handleApi() {
        userInquiryViewDataModel.userInquiryResponseLiveData.observe(this, Observer {
            when (it[0].code) {
                SUCCESS -> {

                    openActivity(MyInquiresActivity::class.java)
                }
                FAILURE -> {
                    toast(it[0].message)
                }
                NO_DATA_FOUND -> {
                    toast(it[0].message)
                }
            }
        })

//        userInquiryViewDataModel.viewProfileResponseLiveData.observe(this, Observer {
//            when (it[0].code) {
//                SUCCESS -> {
//
//                    eaddressET.setText(it[0].data[0].email)
//                    fNameET.setText(it[0].data[0].firstName)
//                    lNameET.setText(it[0].data[0].lastName)
//                    pNumberET.setText(it[0].data[0].mobile)
//                    aboutET.setText(it[0].data[0].about)
//
//               }
//                FAILURE -> {
//                    toast(it[0].message)
//                }
//            }
//        })
    }

    private fun callUserInquiryApi(
        userId: String,
        filePath: String,
        fileName: String,
        firstName: String,
        lastName: String,
        email: String,
        mobile: String,
        aboutUs: String,
        deviceType: String,
        address: String,
        lat: String,
        long: String,
        companyId: String,
        token: String,
        deviceToken: String
    ) {

        val params: MutableMap<String, RequestBody> = HashMap()
        params.put(PARAM_USER_ID, ApiClient.getRequestBody(userId))
        params.put(PARAM_FIRST_NAME, ApiClient.getRequestBody(firstName))
        params.put(PARAM_LAST_NAME, ApiClient.getRequestBody(lastName))
        params.put(PARAM_EMAIL, ApiClient.getRequestBody(email))
        params.put(PARAM_MOBILE, ApiClient.getRequestBody(mobile))
        params.put(PARAM_ABOUT, ApiClient.getRequestBody(aboutUs))
        params.put(PARAM_DEVICE_TYPE, ApiClient.getRequestBody(deviceType))
        params.put(PARAM_LATITTUDE, ApiClient.getRequestBody(lat))
        params.put(PARAM_LONGITUDE, ApiClient.getRequestBody(long))
        params.put(PARAM_ADDRESS, ApiClient.getRequestBody(address))
        params.put(PARAM_COMPANY_ID, ApiClient.getRequestBody(companyId))
        params.put(PARAM_TOKEN, ApiClient.getRequestBody(token))
        params.put(PARAM_DEVICE_TOKEN, ApiClient.getRequestBody(deviceToken))


        userInquiryViewDataModel.callUserInquiryApi(
            params, Util.getFileToUpload(
                PARAM_INQUIRY_IAMGE,
                filePath,
                fileName
            )
        )

    }
//
//        val map: HashMap<String, Any> = HashMap()
//        map[PARAM_USER_ID] = PreferenceHelper.getUserID()
//        map[PARAM_TOKEN] = PreferenceHelper.getToken()
//        map[PARAM_FIRST_NAME] = fNameET.text.toString().trim()
//        map[PARAM_LAST_NAME] = lNameET.text.toString().trim()
//        map[PARAM_EMAIL] = eaddressET.text.toString().trim()
//        map[PARAM_MOBILE] = pNumberET.text.toString().trim()
//        map[PARAM_ABOUT] = aboutET.text.toString().trim()
//        map[PARAM_ADDRESS] = address
//        map[PARAM_COMPANY_ID] = company_id
//        map[PARAM_LATITTUDE] = sourceLat
//        map[PARAM_LONGITUDE] = sourceLong
//
//        userInquiryViewDataModel.callUserInquiryApi(map)
//
//    }

    private fun setData() {
        nextBT.setOnClickListener(this)
        attachImageTV.setOnClickListener(this)
        countryCode = countryCodePicker.defaultCountryCode
        fNameET.setText(PreferenceHelper.getFirstName())
        lNameET.setText(PreferenceHelper.getLastName())
        pNumberET.setText(PreferenceHelper.getUserMobile())
        eaddressET.setText(PreferenceHelper.getUserEmail())

//        pNumberET.setText("+" + countryCodePicker.defaultCountryCode + " ")
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
//                if (!s.toString().startsWith("+" + countryCodePicker.defaultCountryCode + " ")) {
//                    pNumberET.setText("+" + countryCodePicker.defaultCountryCode + " ")
//                    Selection.setSelection(pNumberET.getText(), pNumberET.getText()!!.length)
//                }
//            }
//        })
    }

    private fun setLabel() {
        titleBarTV.text = LabelUtils.getLabel(this, INQUIRY, R.string.inquiry)
        firstNameTV.text = LabelUtils.getLabel(this, FIRST_NAME, R.string.first_name)
        lastNameTV.text = LabelUtils.getLabel(this, LASTNAME, R.string.last_name)
        emailAddressTV.text = LabelUtils.getLabel(this, EMAIL_ADDRESS, R.string.email_address)
        mobileNumberTV.text = LabelUtils.getLabel(this, MOBILE_NUMBER, R.string.phone_number)
        aboutUSTV.text = LabelUtils.getLabel(this, MESSAGE, R.string.message)
        nextBT.text = LabelUtils.getLabel(this, SUBMIT, R.string.submit)
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
            pNumberET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PHONE,
                    getString(R.string.phone_can_not_blank)
                )

            }
            aboutET.text.toString().trim().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_MESSAGE,
                    getString(R.string.message_can_not_blank)
                )

            }
        }
        return msg
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.nextBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {
//                msg = isValidDetail()




                    /* call api */



                    callUserInquiryApi(
                        PreferenceHelper.getUserID(),
                        filePath!!,
                        fileName,
                        fNameET.text.toString().trim(),
                        lNameET.text.toString().trim(),
                        eaddressET.text.toString().trim(),
                        pNumberET.text.toString().trim(),
                        aboutET.text.toString().trim(),
                        "1",
                        address,
                        sourceLat,
                        sourceLong,
                        company_id,
                        PreferenceHelper.getToken(),
                        PreferenceHelper.getDeviceToken()

                    )

                } else {
                    toast(msg)
                }
            }
            R.id.attachImageTV -> {
                PermissionUtils.checkForPermission(this@InquiryActivity,
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
                this@InquiryActivity,
                SELECT_OPTION,
                R.string.select_option
            )
        )
        val pictureDialogItems = arrayOf(
            LabelUtils.getLabel(this@InquiryActivity, GALLERY, R.string.gallery),
            LabelUtils.getLabel(this@InquiryActivity, CAMERA, R.string.camera)
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
                    }, mediaUriCallback = {
                        photoURI = it
                    })
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
//            thumbIV.setImageURI()
            Log.e("filePath", filePath.toString())


            if (imageList.size <= 6) {
                imageList.add(imgAddPos, ImageGridModel(photoURI.toString(), fileName,false))
            } else {
                imageList.removeAt(imgAddPos)
                imageList.add(imgAddPos, ImageGridModel(photoURI.toString(), fileName,false))
            }
            imageListAdapter.notifyDataSetChanged()
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
//            thumbIV.setImageURI(photoURI)
//            filePath = currentPhotoPath
//            Log.e("filePath", filePath.toString())
            filePath = Util.getRealPathFromURI_API19(applicationContext, photoURI!!)
            fileName = Util.generateImageName()
            if (imageList.size < 6) {
                imageList.add(imgAddPos, ImageGridModel(photoURI.toString(), fileName,false))
            } else {
                imageList.removeAt(imgAddPos)
                imageList.add(imgAddPos, ImageGridModel(photoURI.toString(), fileName,false))
            }
            imageListAdapter.notifyDataSetChanged()
        }

    }

    override fun onItemCallback(position: Int, model: ImageGridModel) {
        if (model.isAddImage) {
            imgAddPos = position
            PermissionUtils.checkForPermission(this@InquiryActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                myCallBack = {
                    if (it) showPictureDialog()
                })
        }
    }

    override fun onItemDeleteCallback(position: Int, model: ImageGridModel) {
        imageList.remove(model)
        for (i in imageList.indices) {
            if (imageList[i].isAddImage) {
                isNewImageAdded = true
                break
            }
        }
        if (isNewImageAdded) {
            for (i in imageList.indices) {
                if (imageList[i].isAddImage) {
                    imageList.removeAt(i)
                    imageList.add(imageList.lastIndex , ImageGridModel("", "",true))
                    break
//                    imageList.add(imageList.lastIndex + 1, ImageGridModel("", "",true))
//                    break
                }
            }
        } else {
            imageList.add(imageList.lastIndex , ImageGridModel("", "",true))
        }
        imageListAdapter.notifyDataSetChanged()
    }
}

