package com.app.thefruitsspirit.auth

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.app.thefruitsspirit.constants.CacheConstants
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.cache.getLoginPreference
import com.app.thefruitsspirit.cache.saveLoginPreference
import com.app.thefruitsspirit.cache.saveToken
import com.app.thefruitsspirit.databinding.ActivityLoginBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.genricdatacontainer.ValidateData
import com.app.thefruitsspirit.model.SignUpResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(),
    Observer<Resource<SignUpResponse>> {
    private var isClick = false
    private val loginVM by viewModels<AuthVM>()
    private var isClicked = ""
    private var session = ""
    private var device_token: String = ""


    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() {
            return ActivityLoginBinding::inflate
        }

    override fun setup() {

//        binding.edtEmail.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        val email = getLoginPreference("email", "")
          session = intent.getStringExtra("session").toString()

        if (session == "0"){
            showErrorAlert(this , "Session Expired!")
        }

        Log.d("fgggfgfgdssf",""+session)
        if (getLoginPreference(CacheConstants.REMEMBER_STATUS, "") == "0") {
            (binding.edtEmail as TextView).text = email
            binding.ivUnselect.setImageResource(R.drawable.select)
            isClick = false
        } else {
            binding.ivUnselect.setImageResource(R.drawable.unselect)
            isClick = true
        }
        onClick()
        getDeviceToken()
    }

    private fun onClick() {
        binding.ivUnselect.setOnClickListener {
            if (isClick) {
                isClick = false
                binding.ivUnselect.setImageResource(R.drawable.select)
            } else {
                isClick = true
                binding.ivUnselect.setImageResource(R.drawable.unselect)
            }
        }

        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            if (ValidateData.loginUpValidation(
                    this,
                    binding.edtEmail.text.toString().trim(),
                )
            ) {
                val email = binding.edtEmail.text.toString()
                saveLoginPreference(CacheConstants.EMAIL, email)
                saveLoginPreference(CacheConstants.REMEMBER_STATUS, "0")
                loginApi()
            }
        }
    }
    private fun loginApi() {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["email"] = binding.edtEmail.text.toString().trim()
        hashMap["deviceType"] = "0"
        hashMap["deviceToken"] = device_token
        loginVM.login(hashMap, this).observe(this, this)
    }
    override fun onChanged(value: Resource<SignUpResponse>) {
        when (value?.status) {
            Status.SUCCESS -> {
                saveToken(this, value.data?.body?.token.toString())
//                saveLoginPreference("user_location", value.data?.body!!.location?.locationName.toString())
                saveLoginPreference("user_name", value.data?.body!!.name.toString())

                saveLoginPreference("is_verify", value.data?.body!!.otpVerified.toString())
                saveLoginPreference("user_location", value.data?.body!!.location?.locationName.toString())

                isClicked = if (!isClick) {
                    "0"
                } else {
                    "1"
                }
                saveLoginPreference(CacheConstants.REMEMBER_STATUS, isClicked)

                if (getLoginPreference("is_verify", "") == "1") {
                    startActivity(Intent(this, SubscriptionActivity::class.java).apply {
                        putExtra("date", binding.edtEmail.text.toString().trim())
                        putExtra("date", value.data?.body?.email)
                    })
                    finishAffinity()
                } else {
                    startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                        putExtra("email", value.data?.body?.email)
                        putExtra("otp", value.data?.body?.otp)

                    })
                }

            }

            Status.ERROR -> {
                showErrorAlert(this, value.message!!)
            }

            Status.LOADING -> {

            }

            null -> {
                showErrorAlert(this, value.data?.message!!)
            }
        }
    }

    private fun getDeviceToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("DEVICE_TOKEN", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result
                Log.e("DEVICE_TOKEN", token.toString())
                device_token = token.toString()
                Log.d("jksdjksdmlsfdmlsd", "gfnbghhg" + device_token)

            })
        } catch (e: Exception) {
            Log.d("DEVICE_TOKEN_ERROR", "getDeviceToken: " + e.message)
        }
    }

}