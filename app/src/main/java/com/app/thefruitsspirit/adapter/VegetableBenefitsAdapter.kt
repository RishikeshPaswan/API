package com.app.thefruitsspirit.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.ItemCharecteristiesBinding
import com.app.thefruitsspirit.databinding.ItemVegetableBenefitsBinding
import com.app.thefruitsspirit.model.BenfitsResponse
import com.app.thefruitsspirit.model.WelcomeModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class VegetableBenefitsAdapter(val context: Context, private var list: ArrayList<BenfitsResponse.Body>) :
    RecyclerView.Adapter<VegetableBenefitsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemCharecteristiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id",list[position].productID)
                itemView.findNavController().navigate(R.id.action_benefitFragment_to_recipesDetailsFragments,bundle)
            }

            binding.apply {
                Glide.with(context).load(ApiConstants.PRODUCT_IMAGE+list[position].image).apply(
                    RequestOptions.placeholderOf(
                        R.drawable.placeholder_img
                    )
                ).into(ivImage)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VegetableBenefitsAdapter.ViewHolder {
        val binding =
            ItemCharecteristiesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: VegetableBenefitsAdapter.ViewHolder, position: Int) {
        holder.onBind(position)

    }
    override fun getItemCount(): Int {
        return list.size
    }

}