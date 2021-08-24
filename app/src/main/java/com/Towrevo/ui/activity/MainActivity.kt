package com.Towrevo.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.Towrevo.R
import com.Towrevo.labels.*
import com.Towrevo.ui.navigation.attendance.AttendenceFragment
import com.Towrevo.ui.navigation.notification.NotificationFragment
import com.Towrevo.ui.navigation.profile.ProfileFragment
import com.Towrevo.ui.navigation.project.ProjectsFragment
import com.Towrevo.util.extension.negativeButton
import com.Towrevo.util.extension.positiveButton
import com.Towrevo.util.extension.showAlertDialog
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.titlebar_layout.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkVersion(false)
        setData()
        setLabel()
    }

    private fun setLabel() {
        menuProjectTV.text = LabelUtils.getLabel(this, PROJECTS, R.string.title_projects)
        menuAttendenceTV.text = LabelUtils.getLabel(this, ATTENDANCE, R.string.title_attendance)
        menuNotificationTV.text = LabelUtils.getLabel(this, NOTIFICATION, R.string.title_notification)
        menuProfileTV.text = LabelUtils.getLabel(this, MYACCOUNT, R.string.title_profile)
    }

    fun setData() {
        projectLL.setOnClickListener(this)
        attendenceLL.setOnClickListener(this)
        notificationLL.setOnClickListener(this)
        profileLL.setOnClickListener(this)
        projectLL.performClick()
    }

    override fun onClick(v: View?) {
        lateinit var fragment: Fragment
        when (v?.id) {
            R.id.projectLL -> {
                fragment = ProjectsFragment()
                setFragment(fragment,1)
            }
            R.id.attendenceLL -> {
                fragment = AttendenceFragment()
                setFragment(fragment,2)
            }
            R.id.notificationLL -> {
                fragment = NotificationFragment()
                setFragment(fragment,3)
            }
            R.id.profileLL -> {
                fragment = ProfileFragment()
                setFragment(fragment,4)
            }
        }

    }

    fun checkVersion(isUpdate:Boolean) {
        if(isUpdate){
            showAlertDialog(this) {
                setTitle(R.string.app_name)
                setMessage(LabelUtils.getLabel(this@MainActivity, VERSION_UPDATE, R.string.please_update_version))
                positiveButton(LabelUtils.getLabel(this@MainActivity, UPDATE, R.string.update)) {
                   //redirect to playstore
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.packageName)))

                }

                negativeButton(LabelUtils.getLabel(this@MainActivity, CANCEL, R.string.cancel)) {
                    finishAffinity()
                }
            }
        }

    }

    fun isOnFragment(name: String): Boolean {
        return supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name!!.toString()
            .equals(name, ignoreCase = true)
    }

    fun setFragment(fragment: Fragment,position:Int) {
        if(position==1){
            setDefault()
            menuProjectImageIV.setImageDrawable(getDrawable(R.drawable.menu_projects_active_btn))
            menuProjectTV.setTextColor(Color.parseColor("#f4c230"))
            titleBarTV.text= LabelUtils.getLabel(this, PROJECTS, R.string.title_projects)
        }
        if(position==2){
            setDefault()
            menuAttendanceImageIV.setImageDrawable(getDrawable(R.drawable.menu_attendance_active_btn))
            menuAttendenceTV.setTextColor(Color.parseColor("#f4c230"))
            titleBarTV.text= LabelUtils.getLabel(this, ATTENDANCE, R.string.title_attendance)
        }
        if(position==3){
            setDefault()
            menuNotificationImageIV.setImageDrawable(getDrawable(R.drawable.menu_notification_active_btn))
            menuNotificationTV.setTextColor(Color.parseColor("#f4c230"))
            titleBarTV.text= LabelUtils.getLabel(this, NOTIFICATION, R.string.title_notification)
        }
        if(position==4){
            setDefault()
            menuProfileImageIV.setImageDrawable(getDrawable(R.drawable.menu_profile_active_btn))
            menuProfileTV.setTextColor(Color.parseColor("#f4c230"))
            titleBarTV.text= LabelUtils.getLabel(this, MYACCOUNT, R.string.title_profile)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            if (!isOnFragment(fragment.javaClass.simpleName)) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.navController, fragment, fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commit()
            }
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.navController, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        }
    }

    private fun setDefault(){
        menuProjectImageIV.setImageDrawable(getDrawable(R.drawable.menu_projects_btn))
        menuProjectTV.setTextColor(Color.parseColor("#ffffff"))

        menuAttendanceImageIV.setImageDrawable(getDrawable(R.drawable.menu_attendance_btn))
        menuAttendenceTV.setTextColor(Color.parseColor("#ffffff"))

        menuNotificationImageIV.setImageDrawable(getDrawable(R.drawable.menu_notification_btn))
        menuNotificationTV.setTextColor(Color.parseColor("#ffffff"))

        menuProfileImageIV.setImageDrawable(getDrawable(R.drawable.menu_profile_btn))
        menuProfileTV.setTextColor(Color.parseColor("#ffffff"))
    }
    private fun showDialog() {
        showAlertDialog(this@MainActivity) {
            setTitle(R.string.app_name)
            setMessage(LabelUtils.getLabel(this@MainActivity, ARE_YOU_SURE_WANT_TO_EXIT, R.string.are_you_sure_want_to_exit))
            positiveButton(LabelUtils.getLabel(this@MainActivity, YES, R.string.yes)) {
                toast("yes")
            }

            negativeButton(LabelUtils.getLabel(this@MainActivity, NO, R.string.no)) {
                toast("no")
            }
        }

    }

    override fun onBackPressed() {
        showDialog()
        super.onBackPressed()
    }


}
