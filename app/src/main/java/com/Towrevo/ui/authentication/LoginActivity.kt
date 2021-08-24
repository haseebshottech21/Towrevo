package com.Towrevo.ui.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import com.google.android.gms.common.SignInButton
import com.Towrevo.R
import com.Towrevo.application.MyApp.Companion.pref
import com.Towrevo.social.FBAuth.fbLogOut
import com.Towrevo.social.FBAuth.fbLogin
import com.Towrevo.social.FBAuth.isFBLoggedIn
import com.Towrevo.social.GoogleAuth.checkForExistLogin
import com.Towrevo.social.GoogleAuth.googleClient
import com.Towrevo.social.GoogleAuth.googleSignIn
import com.Towrevo.labels.*
import com.Towrevo.preference.*
import com.Towrevo.preference.PreferenceHelper.set
import com.Towrevo.ui.activity.MainActivity
import com.Towrevo.ui.cms.PrivacyPolicyActivity
import com.Towrevo.ui.cms.TermConditionActivity
import com.Towrevo.util.LOGIN_TYPE
import com.Towrevo.util.Validation.TansformPassword
import com.Towrevo.util.Validation.isEmailValid
import com.Towrevo.util.extension.openActivity
import com.Towrevo.util.extension.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.no_internet_layout.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL


class LoginActivity : BaseAuthActivity(), View.OnClickListener, View.OnTouchListener {



//    private val loginViewModel: LoginViewModel by lazy {
//        ViewModelProvider(
//            this,
//            BaseViewModelFactory { LoginViewModel(this, loginMainLL, noInternetView) }).get(
//            LoginViewModel::class.java
//        )
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        setContentView(R.layout.activity_login)
        setLabel()
        setData()
        setGoogleLoginButton()
//        generateFBHashKey(this)
        setFBLoginButton()

        if (pref.getBoolean(PREF_IS_LOGIN, false)) {
            openActivity(MainActivity::class.java)
        }
    }

    private fun setFBLoginButton() {
        LOGIN_TYPE = 1
        fbLoginBT.setPermissions(EMAIL)
        fbLoginBT.setOnClickListener(this)
    }

    private fun setGoogleLoginButton() {
        // Set the dimensions of the sign-in button.
        googleLoginBT.setSize(SignInButton.SIZE_ICON_ONLY)
        googleLoginBT.setOnClickListener(this)
        // check if already login
    }

    private fun setData() {
        loginBT.setOnClickListener(this)
        forgotpasswordTV.setOnClickListener(this)
        termconditonTV.setOnClickListener(this)
        privacypolicyTV.setOnClickListener(this)
        retryTV.setOnClickListener(this)
        signupTV.setOnClickListener(this)
        changePWTV.setOnClickListener(this)
        passwordET.setOnTouchListener(this)

//        handleAPI()

    }

//    private fun handleAPI() {
//        /**
//         * handle api response
//         */
//        loginViewModel.loginResponseLiveData.observe(this, Observer {
//            when (it[0].code) {
//                SUCCESS -> {
//                    pref[PREF_IS_LOGIN] = true
//                    pref[PREF_LOGIN_TYPE] = ""
//                    openActivity(MainActivity::class.java)
//                    finish()
//                }
//                INVALID_TOKEN -> applicationContext.toast(it[0].message)
//                else -> {
//                    //temporary redirection
//                    pref[PREF_IS_LOGIN] = true
//                    pref[PREF_LOGIN_TYPE] = ""
//                    openActivity(MainActivity::class.java)
//                    finish()
//                }
//            }
//
//        })
//    }

    private fun setLabel() {
        setTitleBar()
        retryTV.text = LabelUtils.getLabel(this, RETRY, R.string.retry)
        emailHintTV.hint = LabelUtils.getLabel(this, EMAIL, R.string.email)
        passwordHintTV.hint = LabelUtils.getLabel(this, PASSWORD, R.string.password)
        forgotpasswordTV.text =
            LabelUtils.getLabel(this, FORGOT_PW, R.string.forget_password)
        termconditonTV.text =
            LabelUtils.getLabel(this, TERMS_CONDITION, R.string.term_condition)
        privacypolicyTV.text =
            LabelUtils.getLabel(this, PRIVACY_POLICY, R.string.privacy_policy)
        signupTV.text =
            LabelUtils.getLabel(this, SIGNUP, R.string.signup)
        loginBT.text = LabelUtils.getLabel(this, LOGIN_C, R.string.login)
    }

    private fun setTitleBar() {
        /* title bar*/
        titleBarTV.text = LabelUtils.getLabel(this, LOGIN, R.string.login)
        searchIV.visibility = GONE
        backIV.visibility = VISIBLE
        backIV.setOnClickListener(this)
        /**********************/
    }


    /**
     * password show/hide touch event
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (v!!.id) {
            R.id.passwordET -> TansformPassword(event, passwordET)
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginBT -> {
                val msg = isValidDetail()
                if (msg.isEmpty()) {
                    /* call api */
//                    loginViewModel.callLoginApi(emailET.text.toString(), passwordET.text.toString())
                } else {
                    toast(msg)
                }
            }
            R.id.forgotpasswordTV -> openActivity(ForgotPasswordActivity::class.java)
            R.id.retryTV -> {
            }
            R.id.termconditonTV -> openActivity(TermConditionActivity::class.java)
            R.id.privacypolicyTV -> openActivity(PrivacyPolicyActivity::class.java)
            R.id.signupTV -> openActivity(RegistrationActivity::class.java)
            R.id.changePWTV -> openActivity(CreateNewPasswordActivity::class.java)
            {
                putString("","dfdf")
            }
            R.id.backIV -> finish()
            R.id.googleLoginBT -> if (!checkForExistLogin(this)) {
                LOGIN_TYPE = 0
                googleClient(this)
                googleSignIn(this)
            }
            R.id.fbLoginBT -> if (!isFBLoggedIn()) {
                LOGIN_TYPE = 1
                fbLogin(loginCallback = {
                    if(getFBUserInfo(it)) {
                        openActivity(MainActivity::class.java)
                        finish()
                    }
                    else fbLogOut(this,logoutCallback = {toast("Your Email Id is private \n Please login using this application or change permission")})
                })
            }
        }
    }

    /**
     * get fb user info from graph api
     */
    private fun getFBUserInfo(it: JSONObject): Boolean {
        try {
            val userId = it.getString("id")
            val profilePicture =
                URL("https://graph.facebook.com/" + userId.toString() + "/picture?width=500&height=500")
            if (it.has("first_name")) pref[PREF_FIRST_NAME] = it.getString("first_name")
            if (it.has("last_name")) pref[PREF_LAST_NAME] = it.getString("last_name")
            if (it.has("birthday")) pref[PREF_BIRTHDAY] = it.getString("birthday")
            if (it.has("gender")) pref[PREF_GENDER] = it.getString("gender")
            if (it.has("email")) {
                pref[PREF_EMAIL] = it.getString("email")
                return true
            } else return false

        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * check validation
     */
    private fun isValidDetail(): String {

        var msg = ""

        when {
            emailET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_EMAIL,
                    getString(R.string.email_can_not_blank)
                )

            }
            !isEmailValid(emailET.text.toString()) -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_VALID_EMAIL,
                    getString(R.string.email_should_valid)
                )

            }
            passwordET.text.toString().isEmpty() -> {
                msg = LabelUtils.getLabel(
                    this,
                    PLEASE_ENTER_PASSWORD,
                    getString(R.string.password_can_not_blank)
                )

            }
        }
        return msg
    }

}


