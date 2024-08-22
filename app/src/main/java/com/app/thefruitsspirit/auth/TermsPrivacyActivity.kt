package com.app.thefruitsspirit.auth

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.base.BaseActivity
import com.app.thefruitsspirit.databinding.ActivityTermsPrivacyBinding
import com.app.thefruitsspirit.genricdatacontainer.Resource
import com.app.thefruitsspirit.model.TermasPrivacyResponse
import com.app.thefruitsspirit.retrofit.Status
import com.app.thefruitsspirit.utils.showErrorAlert
import com.app.thefruitsspirit.view_model.AuthVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsPrivacyActivity : BaseActivity<ActivityTermsPrivacyBinding>(),
    Observer<Resource<TermasPrivacyResponse>> {
    private val termsPrivacyVM by viewModels<AuthVM>()

    private var from = ""
    private var pos = -1
    var type = ""

    override val bindingInflater: (LayoutInflater) -> ActivityTermsPrivacyBinding
        get() {
            return ActivityTermsPrivacyBinding::inflate
        }

    override fun setup() {
        type = intent.getStringExtra("value").toString()
        from = intent.getStringExtra("from").toString()
        pos = intent.getIntExtra("pos", 0)

        onClick()
    }

    private fun onClick() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (from == "walkThrough") {
            if (pos == 0) {
                binding.tvTopName.text = "Welcome"
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvTitle.text = getString(R.string.welcome_to_fruitspirit)
                binding.tvConant.text =
                    "I made this app to help me grow spiritually and deepen my faith by living out the teachings of \"The Fruit of the Spirit\" every day.\n" +
                            "Life can get tough with work, family, relationships, and different personalities we encounter, which can lead to stress, tension, and anxiety. This app aims to bring inner peace and counteract emotional turmoil.\n" +
                            "I became a Christian at 13 and have followed Christ for nearly 50 years, but discussions about these spiritual principles were rare in my church. I realized that if God gave me these spiritual gifts, I should actively use them daily.\n" +
                            "This app helps me become a better version of myself, striving to be more like Jesus each day. It's not just for me; it's meant to inspire others to live out God's intentions for their lives too.\n" +
                            "Using this app has brought me more peace and patience amid life's challenges. I hope it can do the same for you and contribute to a broader spiritual revival.\n" +
                            "I invite you to join me on this journey toward a better life. Let the Fruits of the Spirit—Love, Joy, Peace, Patience, Kindness, Goodness, Faithfulness, Gentleness, and Self-Control—guide us all.\n" +
                            "For more inspiration, read Philippians 1:9-11."
            } else {
                binding.tvTopName.text = "Get Started"
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvWarning.visibility = View.VISIBLE
                binding.tvTitle.text = getString(R.string.welcome_to_fruitspirit1)
//                binding.tvWarning.text = getString(R.string.warning)
                binding.tvConant.text =
                    "Let's understand the message from Galatians 5:22-26 first. It talks about qualities called the \"Fruit of the Spirit\" that Christians should develop, like love, joy, peace, patience, kindness, goodness, faithfulness, gentleness, and self-control. Believers are urged to live by the Spirit, letting these qualities guide their thoughts, actions, and relationships, leading to greater peace and joy.\n" +
                            "Now, about \"The Fruit of the Spirit Application.\" This app helps you grow in the qualities Jesus gave you when you accepted Him as your Lord and Savior. These qualities shape your character and decisions as you go through life. The app is easy to use, with icons for each Fruit on the home page. You tap a Fruit when you've shown it in your interactions with yourself or others. The app tracks your progress daily, weekly, monthly, and yearly.\n" +
                            "The aim is self-awareness—understanding your strengths and weaknesses—and striving to be more like Jesus each day. It's not about comparing yourself to others; it's a personal journey. After tapping a Fruit, the app congratulates you and gives you the option to go to the characteristics and qualities of that fruit. There might also be Bible verses for self-reflection and growth.\n" +
                            "The app has a bonus section, located on the 3rd tab at the bottom bar, for potential personal growth. There's also a resource page, the 4th tab, with prayer lines, online sermons, daily readings, our contact information as with sponsorship information.\n" +
                            "The developer hopes that each day, you'll use the Holy Spirit's power within you and the gift of the “Fruit of the Spirit” for your personal growth in life’s journey and to impact everyone around you including your family, friends, your co-workers and loved ones in positive way. \n" +
                            "The developer’s motto is simple: To Love, To Give, To Do, To Share.\n" +
                            "May God bless you on your journey!"

            }
        } else {
            if (type == "1") {
                binding.tvTopName.text = "Privacy Policy"
                getTermsPrivacyApi()
            } else {
                binding.tvTopName.text = "Terms & Conditions"
                getTermsPrivacyApi()
            }
        }
    }

    private fun getTermsPrivacyApi() {
        termsPrivacyVM.getTermsPrivacyApi(type, this).observe(this, this)
    }

    override fun onChanged(value: Resource<TermasPrivacyResponse>) {
        when (value.status) {
            Status.SUCCESS -> {
                binding.tvConant.text = value.data!!.body?.content?.let {
                    HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }
////                val text = "This is a sample text with a link: http://www.example.com"
//
//                val spannableString = SpannableString(binding.tvConant.text)
//
//                val start = binding.tvConant.text.indexOf("https://termify.io")
//                val end = start + "https://termify.io".length
//
//                spannableString.setSpan(
//                    URLSpan("https://termify.io"),
//                    start,
//                    end,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//
//                binding.tvConant.text = spannableString
//                binding.tvConant.movementMethod = LinkMovementMethod.getInstance()

                binding.tvConant.isClickable = true
                binding.tvConant.movementMethod = LinkMovementMethod.getInstance()

                val text = binding.tvConant.text.toString()
                val spannableString = SpannableString(text)

                val url = "https://termify.io"
                val start = text.indexOf(url)

                if (start != -1) {
                    val end = start + url.length

                    spannableString.setSpan(
                        URLSpan(url),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    binding.tvConant.text = spannableString
                } else {
                    // Optionally handle the case where the URL is not found in the text
                    Log.e("LinkSpanError", "URL not found in text")
                }
            }

            Status.ERROR -> {
                Log.e("error", value.message.toString())
                showErrorAlert(this, value.message.toString())
            }

            Status.LOADING -> {
                Log.e("error", value.message.toString())
            }
        }
    }
}