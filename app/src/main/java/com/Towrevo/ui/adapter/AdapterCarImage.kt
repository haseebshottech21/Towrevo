package com.Towrevo.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.ui.datamodel.CarTowsModel
import kotlinx.android.synthetic.main.item_inquiry_image.view.*

class AdapterCarImage(
    val context: Context,
    private val images: MutableList<CarTowsModel.Data.Image>



) : RecyclerView.Adapter<AdapterCarImage.ViewHolder>() {

    val sdk = Build.VERSION.SDK_INT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_inquiry_image, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(images[position].image)
            .placeholder(R.drawable.ic_group_image).into(holder.itemView.addNewImage)

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}