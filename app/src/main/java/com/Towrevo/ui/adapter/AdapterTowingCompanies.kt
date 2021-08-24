package com.Towrevo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.Towrevo.R
import com.Towrevo.ui.activity.LocationActivity
import com.Towrevo.ui.datamodel.NearByCompanyModel
import com.Towrevo.util.extension.openActivity
import kotlinx.android.synthetic.main.item_towing_companies_layout.view.*

class AdapterTowingCompanies ( val context: Context,
                               private val tripDetailLists: MutableList<NearByCompanyModel.Data>,
                               var itemClickCallback: (Int) -> Unit = {},
                               var callCompanyCallback:(Int)-> Unit={},
                               var dirctionCallback:(Int)-> Unit={}
                            )



    : RecyclerView.Adapter<AdapterTowingCompanies.ViewHolder>() {
        override fun onBindViewHolder(holder: AdapterTowingCompanies.ViewHolder, position: Int) {
            holder.itemView.workingHourTV.text = tripDetailLists[position].workingHours
            holder.itemView.companyTV.text = tripDetailLists[position].companyName
            holder.itemView.companyDetailsTV.text = tripDetailLists[position].companyDetails
            holder.itemView.distanceTV.text = "( "+tripDetailLists[position].distance+" Miles )"

            Glide.with(context).load(tripDetailLists[position].companyProfileImage)
                .placeholder(R.drawable.ic_group_image).into(holder.itemView.companyProfileIV)

        holder.itemView.getDirectionTV.setOnClickListener {
            context.openActivity(LocationActivity::class.java)
        }
//            holder.itemView.callIV.setOnClickListener {
////                val intent = Intent()
////                intent.action = Intent.ACTION_DIAL // Action for what intent called for
////                intent.data =
////                        Uri.parse("tel: $mobileNumber") // Data with intent respective action on intent
////                startActivity(intent)
//            }

            holder.itemView.ProfileLL.setOnClickListener{
                itemClickCallback(position)
            }
            holder.itemView.callIV.setOnClickListener{
                callCompanyCallback(position)
            }
            holder.itemView.getDirectionTV.setOnClickListener{
                dirctionCallback(position)
            }

        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AdapterTowingCompanies.ViewHolder {

            val view = LayoutInflater.from(context).inflate(R.layout.item_towing_companies_layout, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return tripDetailLists.size
        }
    }
