package com.Towrevo.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.ui.`interface`.OnItemCallback
import com.Towrevo.ui.datamodel.ImageGridModel
import com.Towrevo.util.TOTAL_UPLOAD_IMAGE
import kotlinx.android.synthetic.main.item_image_grid.view.*


class ImageGridAdapter(
    val context: Context,
    private val images: MutableList<ImageGridModel>,
    private val onRowClick: OnItemCallback


) : RecyclerView.Adapter<ImageGridAdapter.ViewHolder>() {

    val sdk = Build.VERSION.SDK_INT
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_grid, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images.size != TOTAL_UPLOAD_IMAGE ) {
            if (images[position].isAddImage) {
                holder.itemView.addPhotosFL.visibility = View.VISIBLE
                holder.itemView.ll_image_pic.visibility = View.GONE
            } else {
                holder.itemView.ll_image_pic.visibility = View.VISIBLE
                holder.itemView.addPhotosFL.visibility = View.GONE
                Glide.with(context).load(images[position].thumb).error(R.drawable.ic_group_image).into(holder.itemView.thumbIV)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.addPhotosFL.setOnClickListener {
                if (images[adapterPosition].isAddImage) {
                    onRowClick.onItemCallback(adapterPosition, images[adapterPosition])
                }
            }

            itemView.imgDelete.setOnClickListener {
                onRowClick.onItemDeleteCallback(adapterPosition, images[adapterPosition])
            }
        }
    }
}