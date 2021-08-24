package com.Towrevo.ui.navigation.notification


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

import com.Towrevo.R
import com.Towrevo.util.OnBottomReachedListener
import com.vrin.taylorenggpms.ui.navigation.notification.AdapterNotification
import com.vrin.taylorenggpms.ui.navigation.notification.NotificationsModel
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : Fragment(), OnBottomReachedListener {

    var isRefresh = false
    var page_no = 1
    var total_notification=100
    var items = ArrayList<NotificationsModel>()
    lateinit var adapter: AdapterNotification

    override fun onBottomReached(position: Int) {
        if (items.size < total_notification) {
            page_no++
           /*
           call api here
            */
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rvNotification.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        swipetorefreshNotificaton.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                activity!!,
                R.color.colorPrimary
            )
        )
        swipetorefreshNotificaton.setColorSchemeColors(Color.WHITE)
        swipetorefreshNotificaton.setOnRefreshListener {
            isRefresh = true
            page_no = 1
            /*
            call api here
             */
            swipetorefreshNotificaton.isRefreshing = false
        }

        items.add(NotificationsModel("1", "Test1","10/02/2020"))
        items.add(NotificationsModel("2", "Test2","10/02/2020"))
        items.add(NotificationsModel("3", "Test3","10/02/2020"))
        items.add(NotificationsModel("4", "Test4","10/02/2020"))
        items.add(NotificationsModel("5", "Test5","10/02/2020"))
        items.add(NotificationsModel("1", "Test1","10/02/2020"))
        items.add(NotificationsModel("2", "Test2","10/02/2020"))
        items.add(NotificationsModel("3", "Test3","10/02/2020"))
        items.add(NotificationsModel("4", "Test4","10/02/2020"))
        items.add(NotificationsModel("5", "Test5","10/02/2020"))

        adapter = AdapterNotification(items, activity!!.applicationContext, myCallBack = {})
        rvNotification.adapter = adapter
        adapter.onBottomReachedListener = this
    }
}
