package com.app.thefruitsspirit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.ItemSummaryBinding
import com.app.thefruitsspirit.model.SummaryResponse
import com.app.thefruitsspirit.model.WelcomeModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AdapterSummary(val context: Context, private var list: ArrayList<SummaryResponse.Body>) :
    RecyclerView.Adapter<AdapterSummary.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.tvName.text = list[position].name
            binding.tvNumber.text = list[position].count.toString()

            Glide.with(context).load(ApiConstants.PRODUCT_IMAGE+list[position]?.image).apply(
                RequestOptions.placeholderOf(
                    R.drawable.placeholder_img
                )
            ).into(binding.ivImage)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSummary.ViewHolder {
        val binding = ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterSummary.ViewHolder, position: Int) {
        holder.onBind(position)

    }

    override fun getItemCount(): Int {
        return list.size

    }
}