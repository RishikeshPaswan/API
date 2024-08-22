package com.app.thefruitsspirit.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.auth.TermsPrivacyActivity
import com.app.thefruitsspirit.databinding.ItemHomeViewPaserBinding
import com.app.thefruitsspirit.databinding.ItemViewPagerBinding
import com.app.thefruitsspirit.model.WelcomeModel
import com.app.thefruitsspirit.utils.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ViewPagerAdapter (var context : Context, var bannerImage:ArrayList<WelcomeModel>
): RecyclerView.Adapter<ViewPagerAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(private val binding: ItemViewPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.apply {
                tvTitle.text = bannerImage[pos].content
                tvContent.text = bannerImage[pos].name
                tbWelcome.isVisible()
            }

            Glide.with(context).load(bannerImage[pos].image).placeholder(R.drawable.welcome_new_02).into(binding.ivSlider)

            binding.tvSeeMore.setOnClickListener {
                context.startActivity(Intent((context), TermsPrivacyActivity::class.java).apply {
                    putExtra("from", "walkThrough")
                    putExtra("pos", pos)
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemViewPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return bannerImage.size
    }
}