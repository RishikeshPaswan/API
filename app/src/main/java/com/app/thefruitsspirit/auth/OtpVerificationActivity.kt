package com.app.thefruitsspirit.auth

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.cache.saveLoginPreference
import com.app.thefruitsspirit.databinding.ActivityOtpVerificationBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.genricdatacontainer.ValidateData
import com.app.thefruitsspirit.model.CommanResponse
import com.app.thefruitsspirit.model.SignUpResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.utils.showSuccessAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerificationActivity : BaseActivity<ActivityOtpVerificationBinding>(),
    Observer<Resource<SignUpResponse>> {
    private var email = ""
    private var otp = ""
    private val verificationVM by viewModels<AuthVM>()
    override val bindingInflater: (LayoutInflater) -> ActivityOtpVerificationBinding
        get() {
            return ActivityOtpVerificationBinding::inflate
        }

    override fun setup() {
        showSuccessAlert(this,"Your Otp is 1111")
        email = intent.getStringExtra("email").toString()
        otp = intent.getStringExtra("otp").toString()
        binding.tvEmail.text = email

        onClick()
    }

    private fun onClick() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnVerify.setOnClickListener {
            if (ValidateData.otpVerification(
                    this,
                    binding.otpPin.text.toString().trim(),
                )
            ) {
                otpVerificationApi(binding.otpPin.text.toString(), email)
            }
        }

        binding.tvResend.setOnClickListener {
            verificationVM.resendOtp(email, this).observe(this, resendOtpApi)
        }
    }

    private fun otpVerificationApi(otp: String, email: String) {
        verificationVM.otpVerification(otp, email, this).observe(this, this)
    }

    override fun onChanged(value: Resource<SignUpResponse>) {
        when (value?.status) {
            Status.SUCCESS -> {
                saveLoginPreference("otp", value.data?.body!!.otp.toString())
                startActivity(Intent(this, SubscriptionActivity::class.java))
                finishAffinity()
            }

            Status.ERROR -> {
                showErrorAlert(this, value.message!!)
            }

            Status.LOADING -> {

            }

            null -> {

            }
        }
    }

    private var resendOtpApi = Observer<Resource<CommanResponse>> {
        when (it.status) {
            Status.SUCCESS -> {
                showSuccessAlert(this, it.data?.message.toString())
                if (it.data?.body != null) {
                }
            }

            Status.ERROR -> {
                showErrorAlert(this, it.message!!)
            }

            Status.LOADING -> {


            }
        }
    }
}