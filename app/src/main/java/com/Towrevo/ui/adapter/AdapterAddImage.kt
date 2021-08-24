package com.Towrevo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.ui.datamodel.ImageModel
import kotlinx.android.synthetic.main.item_add_task_image.view.*

class AdapterAddImage(
    val context: Context,
    val images: MutableList<ImageModel>,
    val onAddCallback:(Int, ImageModel, Boolean)->Any?,
    val onDeleteCallback:(Int, ImageModel)->Any?
) : RecyclerView.Adapter<AdapterAddImage.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_add_task_image, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val taskImageModel = images[position]

        if (taskImageModel.isAddImage) {
            holder.imgDeleteIV.visibility = View.GONE
            Glide.with(context).load(taskImageModel.localPath).into(holder.imageTaskIV)
            holder.imageTaskIV.setOnClickListener {
                onAddCallback(position, taskImageModel, false)
            }
            holder.imgDeleteIV.setOnClickListener {

            }
        } else {
            holder.imgDeleteIV.visibility = View.VISIBLE

            if (taskImageModel.isLocal)
                Glide.with(context).load(taskImageModel.localPath).into(holder.imageTaskIV)
            else
                Glide.with(context).load(taskImageModel.remotePath).into(holder.imageTaskIV)

            holder.imgDeleteIV.setOnClickListener {
                onDeleteCallback(position, taskImageModel)
            }
            holder.imageTaskIV.setOnClickListener {
                onAddCallback(position, taskImageModel, true)
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    /*interface OnTaskImageClickListener {
        fun onAddImageClick(
            position: Int,
            taskImageModel: ImageModel,
            shouldOpenBigImage: Boolean
        )

        fun onDeleteImageClick(position: Int, taskImageModel: ImageModel)
    }*/


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageTaskIV: ImageView = view.imgTaskIV
        val imgDeleteIV: ImageView = view.imgDeleteIV
    }
}