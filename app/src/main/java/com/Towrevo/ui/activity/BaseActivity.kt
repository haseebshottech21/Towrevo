package com.twoSecure.ui.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.Towrevo.application.MyApp.Companion.pref
import com.Towrevo.preference.LANGUAGE_ENGLISH_CODE
import com.Towrevo.preference.PREF_LANGUAGE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    private var connectivityDisposable: Disposable? = null
    private val TAG = "BaseActivity"
    private var mutableNetworkLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun isNetworkConnected(): LiveData<Boolean> {
        return mutableNetworkLiveData
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContentView(getLayoutResourceId())
        connectivityDisposable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { connectivity ->
                Log.d(TAG, connectivity.toString())
                if (connectivity.state() == NetworkInfo.State.CONNECTED) {
                    mutableNetworkLiveData.postValue(true)
                } else {
                    mutableNetworkLiveData.postValue(false)
                }

            }

        main()
    }

    protected abstract fun getLayoutResourceId(): Int

    protected abstract fun main()


    override fun onDestroy() {
        super.onDestroy()
        safelyDispose(connectivityDisposable)

    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context?): Context? {

        val language: String =
            pref.getString(PREF_LANGUAGE, LANGUAGE_ENGLISH_CODE) ?: LANGUAGE_ENGLISH_CODE
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context ?: this, locale)
        } else updateResourcesLocaleLegacy(context ?: this, locale)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context? {
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    @Suppress("DEPRECATION")
    fun getCurrentLanguage(context: Context = this): String {
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
        return locale.toString()
    }

}
