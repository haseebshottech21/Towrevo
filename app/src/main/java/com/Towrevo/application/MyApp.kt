package com.Towrevo.application

import android.app.Application
import android.content.SharedPreferences
import android.location.Location
import com.Towrevo.preference.PreferenceHelper
import com.Towrevo.preference.PreferenceHelper.PREFCUSTOM
import com.Towrevo.preference.PreferenceHelper.PREFLABEL
import com.Towrevo.preference.PreferenceHelper.PREFLOGIN
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        pref = PreferenceHelper.customPrefs(this, PREFCUSTOM)
        preflogin = PreferenceHelper.customPrefs(this, PREFLOGIN)
        prefLabel = PreferenceHelper.customPrefs(this, PREFLABEL)
        RxPaparazzo.register(this)
    }


    companion object {
        lateinit var pref: SharedPreferences
        lateinit var preflogin: SharedPreferences
        lateinit var prefLabel: SharedPreferences
        var myCurrentLocation: Location? = null
        var currentAddress = ""
        var currentLat = ""
        var currentLong = ""

        var destinationAddress=""
        var destLat=""
        var destLong=""
    }


}