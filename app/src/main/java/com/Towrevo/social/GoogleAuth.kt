package com.Towrevo.social

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.Towrevo.application.MyApp.Companion.pref
import com.Towrevo.preference.PREF_IS_LOGIN
import com.Towrevo.preference.PreferenceHelper.set


object GoogleAuth {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    fun googleClient(context: Context) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun checkForExistLogin(context: Context): Boolean {
        /* Check for existing Google Sign In account, if the user is already signed in
        the GoogleSignInAccount will be non-null.*/
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null

    }

    fun googleSignIn(context: Context) {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, 0);
    }

    fun googleLogOut(context: Context, logoutCallback: (Context) -> Unit) {
        googleClient(context)
        mGoogleSignInClient.signOut()
        pref[PREF_IS_LOGIN] = false
        logoutCallback(context)
    }
}

