package com.Towrevo.ui.authentication

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.Towrevo.application.MyApp
import com.Towrevo.social.FBAuth
import com.Towrevo.preference.PREF_IS_LOGIN
import com.Towrevo.preference.PREF_LOGIN_TYPE
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.activity.MainActivity
import com.Towrevo.util.LOGIN_TYPE
import com.Towrevo.util.extension.openActivity

open class BaseAuthActivity:AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_TYPE ) { // The Task returned from this call is always completed, no need to attach a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("Google login", "signInResult:=" + account!!.displayName)
                MyApp.pref[PREF_IS_LOGIN] = true
                MyApp.pref[PREF_LOGIN_TYPE] = "google"
                openActivity(MainActivity::class.java)
                finish()
                // Signed in successfully, show authenticated UI.
            } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.d("Google login", "signInResult:failed code=" + e.statusCode)

            }

        } else if(LOGIN_TYPE ==1) {
            FBAuth.fbcallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
}