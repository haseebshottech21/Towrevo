package com.Towrevo.ui.navigation.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Towrevo.R
import com.Towrevo.application.MyApp.Companion.pref
import com.Towrevo.social.FBAuth.fbLogOut
import com.Towrevo.social.GoogleAuth.googleLogOut
import com.Towrevo.preference.PREF_IS_LOGIN
import com.Towrevo.preference.PREF_LOGIN_TYPE
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.authentication.LoginActivity
import com.Towrevo.util.extension.openActivity
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(),View.OnClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        logOutBT.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.logOutBT->logOut()
        }
    }

    private fun logOut() {
        if (pref.getBoolean(PREF_IS_LOGIN, false)) {
            if (pref.getString(PREF_LOGIN_TYPE, "") == "google") {
                googleLogOut(requireActivity(), logoutCallback = {
                    requireActivity().openActivity(LoginActivity::class.java)
                    requireActivity().finishAffinity()

                })
            } else if (pref.getString(PREF_LOGIN_TYPE, "") == "fb"){
                fbLogOut(requireActivity(),logoutCallback = {
                    requireActivity().openActivity(LoginActivity::class.java)
                    requireActivity().finishAffinity()

                })
            }
            else
            {
                pref[PREF_IS_LOGIN] = false
                requireActivity().openActivity(LoginActivity::class.java)
                requireActivity().finishAffinity()
            }
        }
    }


}