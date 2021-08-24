package com.Towrevo.util

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.Towrevo.R


object Validation {

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPhoneValid(phone: String): Boolean {
        if (phone.length <10 || phone.length>14) {
            return false
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches()
        }
    }


     fun TansformPassword(event: MotionEvent?, passwordET: AppCompatEditText): Boolean {

        if (event!!.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= passwordET.right - passwordET.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {

                if (passwordET.transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                    passwordET.transformationMethod = PasswordTransformationMethod.getInstance()
                    passwordET.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.eye_new_close,0)
                } else {
                    passwordET.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    passwordET.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eye_new,0)
                }
                return true
            }

        }
        return false
    }
}