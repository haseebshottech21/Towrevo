package com.Towrevo.util

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.Towrevo.R


object PermissionUtils {
    var isChecked = false
    fun showRationaleDialog(mActivity: Activity, token: PermissionToken, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(mActivity)
        alertDialogBuilder.setTitle(mActivity.getString(R.string.app_name))
        //TODO APPLY LABEL
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setNegativeButton(mActivity.getString(R.string.cancel)) { dialog, which ->
            token.cancelPermissionRequest()
            dialog.dismiss()
        }
        alertDialogBuilder.setPositiveButton(mActivity.getString(R.string.ok)) { dialog, which ->
            dialog.dismiss()
            isChecked = true
            token.continuePermissionRequest()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun showRationaleDialog(
        mActivity: Activity,
        message: String,
        onClickListener: DialogInterface.OnClickListener
    ) {
        val alertDialogBuilder = AlertDialog.Builder(mActivity)
        alertDialogBuilder.setTitle(mActivity.getString(R.string.app_name))
        //TODO APPLY LABEL
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setNegativeButton(mActivity.getString(R.string.cancel), onClickListener)
        alertDialogBuilder.setPositiveButton(mActivity.getString(R.string.ok), onClickListener)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun showSettingsDialog(activity: Activity, REQUEST_PERMISSION_SETTING: Int) {
        //TODO SET LABELS
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.app_name))
        builder.setMessage(R.string.the_app_needs_permission_to_use_this_feature)
        builder.setPositiveButton(R.string.goto_settings) { dialog, which ->
            dialog.cancel()
            openDeviceSettings(activity, REQUEST_PERMISSION_SETTING)
        }
        builder.setNegativeButton(activity.getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
        builder.show()

    }

    private fun openDeviceSettings(context: Activity, REQUEST_PERMISSION_SETTING: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
    }

    /*Check Camera storage Permission*/
    fun checkForPermission(
        context: Activity,
        vararg params: String,
        myCallBack: (Boolean) -> Unit
    ) {
        val permisionList = params.toList()
        Dexter.withActivity(context)
            .withPermissions(
                permisionList
            )
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        myCallBack(true)
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {

                        showSettingsDialog(
                            context,
                            REQUEST_PERMISSION_SETTING
                        )
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationaleDialog(context, token!!, "Need Permission")
                    if (isChecked) myCallBack(true)
                    else myCallBack(false)
                }
            })
            .withErrorListener { }
            .onSameThread()
            .check()


    }



}
