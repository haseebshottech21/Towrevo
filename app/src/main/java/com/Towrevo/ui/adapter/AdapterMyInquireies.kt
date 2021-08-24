package com.Towrevo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.ui.datamodel.CustomerInquiryListModel
import kotlinx.android.synthetic.main.item_my_inquires_layout.view.*


class AdapterMyInquireies ( val context: Context,
                               private val companyList: MutableList<CustomerInquiryListModel.Data>,
                            var itemClickedCallback: (Int) -> Unit = {},
                            var callCompanysCallback:(Int)-> Unit={}


) : RecyclerView.Adapter<AdapterMyInquireies.ViewHolder>() {
    override fun onBindViewHolder(holder: AdapterMyInquireies.ViewHolder, position: Int) {
        holder.itemView.carTowingServiceTV.text = companyList[position].companyName
        holder.itemView.dateTV.text = companyList[position].date

//        holder.itemView.carTowingServiceTV.text = tripDetailLists[position].user
        //To change body of created functions use File | Settings | File Templates.

        holder.itemView.setOnClickListener {
//            context.openActivity(CarTowsServiceActivity::class.java)
        }
        holder.itemView.mainLL.setOnClickListener{
            itemClickedCallback(position)
        }
        holder.itemView.dateTV.setOnClickListener{
            callCompanysCallback(position)
        }


        Glide.with(context).load(companyList[position].profileImage)
            .placeholder(R.drawable.ic_group_image).into(holder.itemView.profileIV)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterMyInquireies.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_my_inquires_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return companyList.size
    }
}
