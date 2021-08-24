package com.Towrevo.social

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.Towrevo.application.MyApp
import com.Towrevo.preference.PREF_IS_LOGIN
import com.Towrevo.preference.PREF_LOGIN_TYPE
import com.Towrevo.preference.PreferenceHelper.set
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object FBAuth {

    lateinit var fbcallbackManager: CallbackManager
    var loginManager= LoginManager.getInstance()

    fun fbLogin(loginCallback: (JSONObject) -> Unit) {
        fbcallbackManager = CallbackManager.Factory.create()

        loginManager.registerCallback(fbcallbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    MyApp.pref[PREF_IS_LOGIN]=true
                    MyApp.pref[PREF_LOGIN_TYPE]="fb"
                    Log.d("Facebook Success-->", loginResult!!.accessToken.userId)

                    val request = GraphRequest.newMeRequest(
                        loginResult.accessToken) { _, response ->
                        loginCallback(response.jsonObject)
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,link")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() { // App code
                    Log.d("Facebook cancel-->", "Cancel")
                }

                override fun onError(exception: FacebookException) { // App code
                    Log.d("Facebook Fail-->", exception.message)
                }
            })
    }

    fun isFBLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun generateFBHashKey(context: Context) {
        lateinit var info: PackageInfo
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )

            } else {
                info = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                )
            }
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }

    }

    fun fbLogOut(context: Context,logoutCallback: (Context) -> Unit){
        LoginManager.getInstance().logOut()
        MyApp.pref[PREF_IS_LOGIN]=false
        logoutCallback(context)
    }
}