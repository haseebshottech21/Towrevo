package com.Towrevo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.ui.datamodel.CompanyInquiryListModel
import kotlinx.android.synthetic.main.item_inquiry_management_layout.view.*
import kotlinx.android.synthetic.main.item_inquiry_management_layout.view.dateTV

class AdapterInquiryManagement (val context: Context,
                                private val companyInquiryLists: MutableList<CompanyInquiryListModel.Data>,
                                var itemClickedCallback: (Int) -> Unit = {},
                                var callCompanysCallback:(Int)-> Unit={}




)
    : RecyclerView.Adapter<AdapterInquiryManagement.ViewHolder>() {
    override fun onBindViewHolder(holder: AdapterInquiryManagement.ViewHolder, position: Int) {
        holder.itemView.carCompanyTowingServiceTV.text = companyInquiryLists[position].userName
        holder.itemView.mobileNumberTV.text = companyInquiryLists[position].mobile
        holder.itemView.dateTV.text = companyInquiryLists[position].date

        //To change body of created functions use File | Settings | File Templates.

        holder.itemView.setOnClickListener {
           // context.openActivity(CustomerDetailActivity::class.java)
        }

        holder.itemView.main.setOnClickListener{
            itemClickedCallback(position)
        }
        holder.itemView.mobileNumberTV.setOnClickListener{
            callCompanysCallback(position)
        }

        Glide.with(context).load(companyInquiryLists[position].profileImage)
            .placeholder(R.drawable.ic_group_image).into(holder.itemView.profileImageIV)
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterInquiryManagement.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_inquiry_management_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return companyInquiryLists.size
    }
}
