package com.Towrevo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.core.widget.TextViewCompat
import com.Towrevo.R
import com.Towrevo.ui.datamodel.FaqModel
import com.Towrevo.ui.datamodel.FaqModel2
import kotlinx.android.synthetic.main.item_faq_header.view.*
import kotlinx.android.synthetic.main.list_row_child.view.*

class FaqExpandableListAdapter(
        private val context: Context,
        private val listFaq: List<FaqModel2>
) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return listFaq[groupPosition].child[childPosititon]
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
            groupPosition: Int, childPosition: Int,
            isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row_child, null, false)
        view.faqChildTV.text = getChild(groupPosition, childPosition) as String
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listFaq[groupPosition].child.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.listFaq[groupPosition]
    }

    override fun getGroupCount(): Int {
        return this.listFaq.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
            groupPosition: Int, isExpanded: Boolean,
            convertView: View?, parent: ViewGroup
    ): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_faq_header, null, false)
        view.faqHeaderTV.text = listFaq[groupPosition].header

        if (isExpanded) {
            TextViewCompat.setTextAppearance(view.faqHeaderTV, R.style.Font14Bold)
            view.faqIV.rotation = 90f
        } else {
            view.faqIV.rotation = 0f
            TextViewCompat.setTextAppearance(view.faqHeaderTV, R.style.Font12Regular)
        }

        return view
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }


}
