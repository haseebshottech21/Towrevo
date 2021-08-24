package com.Towrevo.labels

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import com.Towrevo.application.MyApp
import java.util.*
import android.os.Build


object LabelUtils{

    fun getLabel(mActivity: Context, keyValue: String, @StringRes defaultValue: Int): String {
        return getLabel(mActivity, keyValue, mActivity.getString(defaultValue))
    }

    fun getLabel(mActivity: Context, keyValue: String, defaultValue: String): String {
        return MyApp.prefLabel.getString(keyValue,defaultValue).toString()
    }

    fun getLabelValue(mActivity: Context, defaultValue: String, keyValue: String): String {
        return defaultValue
    }



    @SuppressLint("ObsoleteSdkInt")
    fun setLanguage(context: Context, languageCode: String, countryCode: String) {
        val locale = Locale(languageCode,countryCode)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            context.applicationContext.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}