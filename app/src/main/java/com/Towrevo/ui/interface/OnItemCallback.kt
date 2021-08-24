package com.Towrevo.ui.`interface`

import com.Towrevo.ui.datamodel.ImageGridModel

interface OnItemCallback {
    fun onItemCallback(position:Int,model: ImageGridModel)
    fun onItemDeleteCallback(position:Int,model: ImageGridModel)
}