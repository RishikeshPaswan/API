package com.app.thefruitsspirit.genricdatacontainer

import android.app.Activity
import android.text.TextUtils
import android.util.Patterns
import com.app.thefruitsspirit.utils.showErrorAlert

private val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+[a-z]"

object ValidateData {
    var errorMsg = ""

    fun signUpValidation(
        context: Activity,
        image: String,
        firstname: String,
        email: String,
        ccp: String,
        phone: String,
        location: String,

        ): Boolean {
        var check = false
        when {
            image.isEmpty() -> {
                showErrorAlert(context, "Please select Image")
            }

            firstname.isEmpty() -> {
                showErrorAlert(context, "Please enter  name")
            }

            email.isEmpty() -> {
                showErrorAlert(context, "Please enter email")
            }

            !isValidEmail(email) -> {
                showErrorAlert(context, "Please enter a valid email")
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context, "Please enter valid email")
            }

            ccp.isEmpty() -> {
                showErrorAlert(context, "Please select country code")
            }

            phone.isEmpty() -> {
                showErrorAlert(context, "Please enter phone")
            }

            phone.length < 8 -> {
                showErrorAlert(context, "please enter valid phone number")
            }

            phone.length > 15 -> {
                showErrorAlert(context, "please enter valid phone number")
            }

            location.isEmpty() -> {
                showErrorAlert(context, "Please select location")
            }

            else -> {
                check = true
            }
        }
        return check
    }

    fun editValidation(
        context: Activity,
        image: String,
        name: String,
        ccp: String,
        phone: String,
    ): Boolean {
        var check = false
        when {
            name.isEmpty() -> {
                showErrorAlert(context, "Please enter first name")
            }

            ccp.isEmpty() -> {
                showErrorAlert(context, "Please select country code")
            }

            phone.isEmpty() -> {
                showErrorAlert(context, "Please enter phone")
            }

            else -> {
                check = true
            }
        }
        return check
    }

    fun loginUpValidation(
        context: Activity,
        email: String,
    ): Boolean {
        var check = false
        when {

            email.isEmpty() -> {
                showErrorAlert(context, "Please enter email")
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context, "Please enter valid email")
            }

            !isValidEmail(email) -> {
                showErrorAlert(context, "Please enter a valid email")
            }

            else -> {
                check = true
            }
        }
        return check
    }


    fun otpVerification(
        context: Activity,
        otp: String,
    ): Boolean {
        var check = false
        when {

            otp.isEmpty() -> {
                showErrorAlert(context, "Please enter otp")
            }

            otp.length < 4 -> {
                showErrorAlert(context, "please enter valid otp")
            }

            else -> {
                check = true
            }
        }
        return check
    }

    fun contactUsValidation(
        context: Activity,
        name: String,
        email: String,
        ccp: String,
        phone: String,
        message: String,

        ): Boolean {
        var check = false
        when {
            name.isEmpty() -> {
                showErrorAlert(context, "Please enter  name")
            }

            email.isEmpty() -> {
                showErrorAlert(context, "Please enter email")
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context, "Please enter valid email")
            }
            ccp.isEmpty() -> {
                showErrorAlert(context, "Please select country code")
            }

            phone.isEmpty() -> {
                showErrorAlert(context, "Please enter phone number")
            }
            phone.length < 8 -> {
                showErrorAlert(context, "please enter valid phone number")
            }

            phone.length > 15 -> {
                showErrorAlert(context, "please enter valid phone number")
            }
            message.isEmpty() -> {
                showErrorAlert(context, "Please enter  message")
            }

            else -> {
                check = true
            }
        }
        return check
    }
}

private fun isValidEmail(email: String): Boolean {
    return email.matches(emailRegex.toRegex())
}

