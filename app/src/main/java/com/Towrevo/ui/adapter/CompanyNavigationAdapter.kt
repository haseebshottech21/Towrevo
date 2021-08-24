package com.Towrevo.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Towrevo.R
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.ui.activity.*
import com.Towrevo.ui.cms.AboutUsActivity
import com.Towrevo.ui.cms.PrivacyPolicyActivity
import com.Towrevo.ui.cms.TermConditionActivity
import com.Towrevo.ui.datamodel.NavigationModel
import com.Towrevo.util.EDIT_PROFILE_CODE
import com.Towrevo.util.USER_ID_CONTAINER
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.openActivityForResult
import kotlinx.android.synthetic.main.item_navigation.view.*

class CompanyNavigationAdapter(
    val activity: Activity,
    private val navList: MutableList<NavigationModel>
) : RecyclerView.Adapter<CompanyNavigationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(activity).inflate(R.layout.item_navigation, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return navList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.navTitleTV.text = navList[position].navTitle



    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {

                when (adapterPosition) {
                    0 -> {
                        activity.openActivityForResult(
                            ChangeNewPasswordActivity::class.java,
                            EDIT_PROFILE_CODE
                        ) {
                            putString(USER_ID_CONTAINER, PreferenceHelper.getUserID())
                        }
                    }
//                    1 -> {
//                        activity.openActivity(CompanyHomeActivity::class.java)
//                    }
                    1 -> {
                        activity.openActivity(AboutUsActivity::class.java)

                    }
//                    3 -> {
//                        activity.openActivity(TermConditionActivity::class.java)
//                    }
//                    4 -> {
//                        activity.openActivity(PrivacyPolicyActivity::class.java)
//                    }
                    2 -> {
                        activity.openActivity(ContactActivity::class.java)
                    }
                    3 -> {
                        activity.openActivity(FaqsActivity::class.java)
                    }
                    4 -> {
                        activity.openActivity(CompanyHomeActivity::class.java)
                    }
//                    5 -> {
//                        activity.openActivity(TermConditionActivity::class.java)
//                    }
//                    6 -> {
//                        activity.openActivity(PrivacyPolicyActivity::class.java)
//                    }

                }

                (activity as CompanyHomeActivity).closeDrawer()

            }
        }
    }
}