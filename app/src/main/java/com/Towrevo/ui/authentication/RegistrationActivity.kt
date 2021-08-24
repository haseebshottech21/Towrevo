package com.Towrevo.ui.authentication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.util.*
import com.Towrevo.util.Util.openCameraIntent
import com.Towrevo.util.Util.openGalleryPicker
import com.Towrevo.util.Validation.isEmailValid
import com.Towrevo.util.Validation.isPhoneValid
import com.Towrevo.util.extension.toast
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import kotlinx.android.synthetic.main.titlebar_layout.*


class RegistrationActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var currentPhotoPath: String
    private var photoURI: Uri? = null
    val imgCount=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setLabel()
        setData()
    }

    private fun setData() {
        registerBT.setOnClickListener(this)
        editImage.setOnClickListener(this)
    }

    private fun setLabel() {
        setTitleBar()
        retryTV.text = LabelUtils.getLabel(this, RETRY, R.string.retry)
        fnameTIL.hint = LabelUtils.getLabel(this, FIRST_NAME, R.string.first_name)
        lnameTIL.hint = LabelUtils.getLabel(this, LASTNAME, R.string.last_name)
        phoneTIL.hint = LabelUtils.getLabel(this, PHONE_NUMBER, R.string.phone_number)
        addressTIL.hint = LabelUtils.getLabel(this, ADDRESS, R.string.address)
        emailTIL.hint = LabelUtils.getLabel(this, EMAIL, R.string.email)
        registerBT.text = LabelUtils.getLabel(this, REGISTER, R.string.register)
    }

    private fun setTitleBar() {
        /* title bar*/
        titleBarTV.text = LabelUtils.getLabel(this, REGISTRATION, R.string.registration)
        searchIV.visibility = GONE
        backIV.visibility = VISIBLE
        backIV.setOnClickListener(this)
        /**********************/
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.registerBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {
                    //call api
                } else {
                    toast(msg)
                }
            }
            R.id.retryTV -> {
            }
            R.id.editImage -> {
                PermissionUtils.checkForPermission(this@RegistrationActivity,  Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,myCallBack = {
                        if (it) showPictureDialog()
                    })
            }
            R.id.backIV -> finish()
        }
    }

    /**
     * check validation
     */
    private fun isValidDetail(): String {

        var msg = ""

        when {
            firstnameET.text.toString().isEmpty() -> { msg = LabelUtils.getLabel(this, PLEASE_ENTER_FNAME, getString(R.string.firstname_can_not_blank))

            }
            lastnameET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_LNAME,
                    getString(R.string.lastname_can_not_blank)
                )

            }
            phoneNumberET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PHONE,
                    getString(R.string.phone_can_not_blank)
                )

            }
            !isPhoneValid(phoneNumberET.text.toString()) -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_PHONE_NUMBER,
                    getString(R.string.phone_should_valid)
                )
            }
            addressET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_ADDRESS,
                    getString(R.string.address_can_not_blank)
                )

            }
            emailIdET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_EMAIL,
                    getString(R.string.email_can_not_blank)
                )

            }
            !isEmailValid(emailIdET.text.toString()) -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_EMAIL,
                    getString(R.string.email_should_valid)
                )

            }

        }
        return msg
    }


    /*
    choose option dialog
     */
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(
            LabelUtils.getLabel(
                this@RegistrationActivity,
                SELECT_OPTION,
                R.string.select_option
            )
        )
        val pictureDialogItems = arrayOf(
            LabelUtils.getLabel(this@RegistrationActivity, GALLERY, R.string.gallery),
            LabelUtils.getLabel(this@RegistrationActivity, CAMERA, R.string.camera)
        )
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> {
                    openGalleryPicker(this, imgCount,imageCallback = {
                        // get if multiple image selected
                            photoURI = Uri.fromFile(it)
                            profileImageIV.setImageURI(photoURI)
                         //   UploadImage(mutableListOf(photoURI!!), false,"",this,true,uploadCompleteCallback = {Log.d("upload name--",it)})
                    })
                }
                1 -> {
                    openCameraIntent(this,imageCallback = {
                        currentPhotoPath=it!!.absolutePath },imageUriCallback = {photoURI=it})
                }
            }
        }
        pictureDialog.show()
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICKER_CODE && resultCode == Activity.RESULT_OK ) {
            // get if single image selected
            val obtainResult = Matisse.obtainResult(data)
            profileImageIV.setImageURI(obtainResult[0])
            //UploadImage(obtainResult, false,"",this,true,uploadCompleteCallback = {Log.d("upload name--",it)})
        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK ) {
            profileImageIV.setImageURI(photoURI)
          //  UploadImage(mutableListOf(photoURI!!),true,currentPhotoPath,this,true,uploadCompleteCallback = {Log.d("upload name--",it)})
        }
    }

}
