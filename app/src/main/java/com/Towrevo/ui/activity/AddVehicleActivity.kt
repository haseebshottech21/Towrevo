package com.Towrevo.ui.activity

import android.net.Uri
import com.Towrevo.R
import com.Towrevo.ui.adapter.AdapterAddImage
import com.Towrevo.ui.datamodel.ImageModel
import com.twoSecure.ui.activity.BaseActivity

class AddVehicleActivity : BaseActivity() {

    private var LIMIT = 5
    private var items = mutableListOf<ImageModel>()
    private val uri = Uri.parse("android.resource://com.kotlinbaseapplication/drawable/ic_icon_plus_bg")
    private var adapterAddImage: AdapterAddImage? = null
    private lateinit var currentPhotoPath: String
    private var photoURI: Uri? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_add_vehicle
    }

    override fun main() {
     //   setTitle()
      //  setLable()
       // setData()
       // onClick()

    }

    /*private fun setLable() {
        vehicleTypeSpinner.hint =  LabelUtils.getLabel(this, VEHICLE_TYPE,getString(R.string.vehicle_type))
        vehicleNameSpinner.hint =  LabelUtils.getLabel(this, VEHICLE_NAME,getString(R.string.vehicle_name))
        vehicleImageTV.text =  LabelUtils.getLabel(this, VEHICLE_IMAGE,getString(R.string.vehicle_image))
        continueBTN.text = LabelUtils.getLabel(this, CONTINUE,getString(R.string.continue_key))
    }
    private fun setTitle() {
        signUpLable.visibility = View.VISIBLE
        signUpLable.text = LabelUtils.getLabel(this, ADD_VEHICLE,getString(R.string.add_vehicle))
    }
    private fun onClick() {
        image_back_arrow.setOnClickListener {
            onBackPressed()
        }
        continueBTN.setOnClickListener {
           // openActivity(TowingTypeActivity::class.java)
        }
    }

    private fun setData() {
       setSpinnerData()
       setImage()
    }
    fun setSpinnerData(){
        var vList_type = arrayListOf<String>()
        vList_type.add("Type 1")
        vList_type.add("Type 2")
        vList_type.add("Type 3")
        vList_type.add("Type 4")
        vList_type.add("Type 5")

        val vTypeAdpter = ArrayAdapter(this,android.R.layout.simple_list_item_1,vList_type)
        vehicleTypeSpinner.adapter = vTypeAdpter

        var vList_name = arrayListOf<String>()
        vList_name.add("Name 1")
        vList_name.add("Name 2")
        vList_name.add("Name 3")
        vList_name.add("Name 4")
        vList_name.add("Name 5")

        val vNameAdpter = ArrayAdapter(this,android.R.layout.simple_list_item_1,vList_name)
        vehicleNameSpinner.adapter = vNameAdpter
    }
    fun setImage(){
        items.add(ImageModel("no-image", uri, "", isAddImage = true))

        adapterAddImage = AdapterAddImage(this, items,
            onAddCallback = { i, imageModel, b ->
                if (!b) showPictureDialog()
            },
            onDeleteCallback = { i, imageModel ->
                items.removeAt(i)
                LIMIT += 1

                if (LIMIT > 0 && items.size > 0 && !items[0].isAddImage)
                    items.add(0, ImageModel("no-image", uri, "", isAddImage = true))

                adapterAddImage?.notifyDataSetChanged()
            })
        vehicleImageRV.adapter = adapterAddImage
    }
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(
            LabelUtils.getLabel(
                this,
                SELECT_OPTION,
                R.string.select_option
            )
        )
        val pictureDialogItems = arrayOf(
            LabelUtils.getLabel(this, GALLERY, R.string.gallery),
            LabelUtils.getLabel(this, CAMERA, R.string.camera)
        )
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> {
                    Util.openGalleryPicker(this, LIMIT, imageCallback = {
                    })
                }
                1 -> {
                    Util.openCameraIntent(this, imageCallback = {
                        currentPhotoPath = it!!.absolutePath
                    }, imageUriCallback = { photoURI = it })
                }
            }
        }
        pictureDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            // get if multiple image selected
            val obtainResult = Matisse.obtainResult(data)
            prepareUploadCall(obtainResult, false)

        }
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            prepareUploadCall(mutableListOf(photoURI!!), true)

        }
    }
    fun prepareUploadCall(list: MutableList<Uri>, isCamera: Boolean) {
        LIMIT -= list.size
        if (LIMIT == 0) {
            items.removeAt(0)
        }
        val listOfImagesModel = mutableListOf<ImageModel>()
        if (isCamera) {
            val taskImageModel =
                ImageModel(
                    File(currentPhotoPath).name,
                    photoURI!!,
                    "",
                    isLocal = true,
                    isUploaded = false,
                    isAddImage = false
                )
            listOfImagesModel.add(taskImageModel)
        } else {
            for (uri in list) {
                val filePath = Util.getRealPathFromURI_API19(applicationContext, uri)
                val file = File(filePath!!)
                val taskImageModel =
                    ImageModel(
                        file.name,
                        uri,
                        "",
                        isLocal = true,
                        isUploaded = false,
                        isAddImage = false
                    )
                listOfImagesModel.add(taskImageModel)
            }
        }

        items.addAll(listOfImagesModel)
        adapterAddImage?.notifyDataSetChanged()

        Util.UploadImage(
            list,
            isCamera,
            "",
            this,
            false,
            uploadCompleteCallback = {
                Log.d("upload name--", it)
                val imageNames = it.split(",")
                for (item in items) {
                    for (image in imageNames) {
                        if (item.image_url == image)
                            item.isUploaded = true
                        break
                    }
                }
            })
    }*/
}