package com.app.thefruitsspirit.auth

import android.content.Intent
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.adapter.ViewPagerAdapter
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.databinding.ActivityWelcomeBinding
import com.app.thefruitsspirit.model.WelcomeModel
import java.util.ArrayList

class WelcomeActivity : BaseActivity<ActivityWelcomeBinding>() {

    var auto = 0
    private  val homeViewHolder by lazy { ViewPagerAdapter(this,bannerImage) }
    private var bannerImage = ArrayList<WelcomeModel>()
    override val bindingInflater: (LayoutInflater) -> ActivityWelcomeBinding
        get(){
            return ActivityWelcomeBinding::inflate
        }

    override fun setup() {
        onClick()
        setAdapters()

        binding.viewPaser.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (auto==0){
                    when (position) {
                        0 -> {
                            binding.btnNext.text = "Next"
                        }
                        else -> {
                            binding.btnNext.text = "Get Started"
                        }
                    }

                }

            }
        })
    }

    private fun onClick() {
        binding.btnNext.setOnClickListener {
            var pos: Int = binding.viewPaser.currentItem
            when (pos) {
                1 -> {
                    pos += 1
//                    Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show()
                      startActivity(Intent(this, LoginActivity::class.java))
                }
                else -> {
                    pos += 1
                    binding.viewPaser.currentItem = pos
                    homeViewHolder.notifyDataSetChanged()
                }
            }
        }
    }
    private fun setAdapters() {
        bannerImage.clear()
        bannerImage.add(WelcomeModel(R.drawable.welcome_new_01, "I made this app to help me grow spiritually and deepen my faith by living out the teachings of \"The Fruit of the Spirit\" every day.","Welcome"))
        bannerImage.add(WelcomeModel(R.drawable.welcome_new_02, "Let's understand the message from Galatians 5:22-26 first. It talks about qualities called the \"Fruit of the Spirit\" that Christians should develop, like love, joy, peace, patience, kindness, goodness, faithfulness, gentleness, and self-control. Believers are urged to live by the Spirit, letting these qualities guide their thoughts, actions, and relationships, leading to greater peace and joy.","Get started"))
        binding.viewPaser.adapter = homeViewHolder
    }
}