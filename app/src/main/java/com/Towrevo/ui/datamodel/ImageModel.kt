package com.Towrevo.ui.datamodel

import android.net.Uri
import java.io.Serializable

class ImageModel(
    var image_url: String,
    var localPath: Uri,
    var remotePath: String,
    var isLocal: Boolean = false,
    var isUploaded: Boolean = false,
    var isAddImage: Boolean = false
):Serializable

