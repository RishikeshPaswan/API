package com.app.thefruitsspirit.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.ItemResourceBinding
import com.app.thefruitsspirit.model.ResourceResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ResourceAdapter(val context: Context, private var list: ArrayList<ResourceResponse.Body>): RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemResourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.ivImage.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id",list[position].productID)
                bundle.putString("image",list[position].image)
                bundle.putString("type","4")
                binding.ivImage.findNavController().navigate(R.id.action_resourceFragment_to_recipesDetailsFragments,bundle)
            }
              binding.apply {
                  tvName.text = list[position].title
                  tvContent.text = list[position].description

                  Glide.with(context).load(ApiConstants.PRODUCT_IMAGE+list[position]?.image).apply(
                      RequestOptions.placeholderOf(
                          R.drawable.placeholder_img
                      )
                  ).into(ivImage)
              }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceAdapter.ViewHolder {
        val binding = ItemResourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ResourceAdapter.ViewHolder, position: Int) {
        holder.onBind(position)

    }
    override fun getItemCount(): Int {
        return list.size
    }

}