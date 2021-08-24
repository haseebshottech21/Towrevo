package com.Towrevo.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.Towrevo.R
import com.Towrevo.ui.adapter.AdapterAddImage
import com.Towrevo.ui.datamodel.ImageModel
import kotlinx.android.synthetic.main.activity_multiple_image_grid.*

class MultipleImageGridActivity : AppCompatActivity() {

    private var LIMIT = 5
    private var items = mutableListOf<ImageModel>()
    private val uri = Uri.parse("android.resource://com.vrin.taylorenggpms/drawable/add_image_box")
    private var adapterAddImage: AdapterAddImage? = null
    private lateinit var currentPhotoPath: String
    private var photoURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_image_grid)
        imageViewRV.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)

        items.add(ImageModel("no-image", uri, "", isAddImage = true))

        adapterAddImage = AdapterAddImage(this, items,
            onAddCallback = { i, imageModel, b ->
              //  if (!b) showPictureDialog()
            },
            onDeleteCallback = { i, imageModel ->
                items.removeAt(i)
                LIMIT += 1

                if (LIMIT > 0 && items.size > 0 && !items[0].isAddImage)
                    items.add(0, ImageModel("no-image", uri, "", isAddImage = true))

                adapterAddImage?.notifyDataSetChanged()
            })
        imageViewRV.adapter = adapterAddImage
    }


   /* private fun openBigImage() {

    }

    *//*
      choose option dialog
       *//*
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