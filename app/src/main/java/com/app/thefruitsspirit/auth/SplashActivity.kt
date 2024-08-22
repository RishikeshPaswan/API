package com.app.thefruitsspirit.auth

import android.content.Intent
import android.view.LayoutInflater
import com.app.thefruitsspirit.activity.HomeActivity
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.cache.getLoginPreference
import com.app.thefruitsspirit.cache.getToken
import com.app.thefruitsspirit.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val activityScope = CoroutineScope(Dispatchers.Main)
    override val bindingInflater: (LayoutInflater) -> ActivitySplashBinding
        get() {
            return ActivitySplashBinding::inflate
        }

    override fun setup() {
        initView()
    }

//    private fun initView() {
//        activityScope.launch {
//            delay(300)
//            if (getToken(this@SplashActivity)?.isNotEmpty() == true) {
//                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
//               finish()
//            }
//            else{
//                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
//                finish()
//            }
//
//
//        }
//    }

    private fun initView() {
        activityScope.launch {
            delay(500)
            if (getToken(this@SplashActivity)?.isNotEmpty() == true) {
                if (getLoginPreference("is_verify", "") == "1") {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finishAffinity()
                } else {

                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finishAffinity()
                }
            } else {

                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                finishAffinity()
            }
        }
    }

}