package com.app.thefruitsspirit.auth

import android.content.Intent
import android.view.LayoutInflater
import com.app.thefruitsspirit.activity.HomeActivity
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.databinding.ActivitySubscriptionBinding

class SubscriptionActivity : BaseActivity<ActivitySubscriptionBinding>(){
    override val bindingInflater: (LayoutInflater) -> ActivitySubscriptionBinding
        get(){
            return ActivitySubscriptionBinding::inflate
        }

    override fun setup() {
       onClicks()
    }

    private fun onClicks() {
     binding.ivBack.setOnClickListener {
         onBackPressedDispatcher.onBackPressed()
     }

        binding.tvByNow.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
            finishAffinity()
        }
    }

}