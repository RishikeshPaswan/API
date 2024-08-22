package com.app.thefruitsspirit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.databinding.ItemHomeViewPaserBinding
import com.app.thefruitsspirit.model.WelcomeModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class HomeViewPagerAdapter(var context : Context, var bannerImage:ArrayList<WelcomeModel>): RecyclerView.Adapter<HomeViewPagerAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(private val binding: ItemHomeViewPaserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.apply {
                tvTitle.text = bannerImage[pos].content
//                tvContent.text = bannerImage[pos].name
            }

            Glide.with(context).load(bannerImage[pos]?.image).apply(
                RequestOptions.placeholderOf(
                    R.drawable.placeholder_img
                )
            ).into(binding.ivSlider)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeViewPaserBinding.inflate(
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