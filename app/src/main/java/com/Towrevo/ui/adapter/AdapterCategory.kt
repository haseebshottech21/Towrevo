package com.Towrevo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.Towrevo.R
import com.Towrevo.ui.datamodel.CategoryDataModel
import kotlinx.android.synthetic.main.item_category_list.view.*

class AdapterCategory(
    val context: Context,
    private val categoryList: MutableList<CategoryDataModel.Data>,
    private val onItemClicked: (Int) -> Unit = {},
    private val onRemoveItemClick: (Int) -> Unit = {},
    val onItemCheckedCallback:(Int, CategoryDataModel.Data) -> Any?
) : RecyclerView.Adapter<AdapterCategory.ViewHolder>() {
    override fun onBindViewHolder(holder: AdapterCategory.ViewHolder, position: Int) {
        val obj = categoryList[position]
        holder.itemView.categoryIdTV.text = categoryList[position].categoryName
        //To change body of created functions use File | Settings | File Templates.

//        holder.itemView.setOnClickListener {
//            context.openActivity(TripDetailsActivity::class.java)
//        }
        holder.itemView.categoryCheck.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    obj.isChecked = true
                    onItemCheckedCallback(position,obj)
                }
                else{
                    obj.isChecked = false
                    onItemCheckedCallback(position,obj)
                }
                //onItemClickCallback(position,obj)
            }
        })
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            /*view.categoryCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

                if (isChecked){
                    onItemClicked(adapterPosition)
                }else {
                    onRemoveItemClick(adapterPosition)
                }
            })*/
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterCategory.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_category_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}