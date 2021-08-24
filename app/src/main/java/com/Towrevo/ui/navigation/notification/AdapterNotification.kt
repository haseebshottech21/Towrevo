package com.vrin.taylorenggpms.ui.navigation.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Towrevo.R
import com.Towrevo.util.OnBottomReachedListener
import kotlinx.android.synthetic.main.item_notification_card.view.*
import java.util.*

class AdapterNotification(
    val items: ArrayList<NotificationsModel>,
    val context: Context,
    val myCallBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterNotification.ViewHolder>(), OnBottomReachedListener {
    lateinit var onBottomReachedListener: OnBottomReachedListener


    override fun onBottomReached(position: Int) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_notification_card, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.nameTV.text=items[position].notification_name
        holder.itemView.dateTV.text=items[position].notification_date
        holder.itemView.setOnClickListener { myCallBack(position) }
        if (position == items.size - 2) {
            onBottomReachedListener.onBottomReached(position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}