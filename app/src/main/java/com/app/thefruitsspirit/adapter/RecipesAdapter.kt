package com.app.thefruitsspirit.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.ItemHomeBinding
import com.app.thefruitsspirit.databinding.ItemRecipesBinding
import com.app.thefruitsspirit.model.RecipesResponse
import com.app.thefruitsspirit.model.WelcomeModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RecipesAdapter(val context: Context, private var list: ArrayList<RecipesResponse.Body>): RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
          binding.tvViewDetails.setOnClickListener {
              val bundle = Bundle()
              bundle.putString("id",list[position]._id)
              bundle.putString("type", "2")
              binding.tvViewDetails.findNavController().navigate(R.id.action_recipesFragment_to_recipesDetailsFragments,bundle)
          }

            binding.apply {
                tvName.text = list[position].title
                Glide.with(context).load(ApiConstants.PRODUCT_IMAGE+list[position]?.image).apply(
                    RequestOptions.placeholderOf(
                        R.drawable.placeholder_img
                    )
                ).into(ivImage)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesAdapter.ViewHolder {
        val binding = ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecipesAdapter.ViewHolder, position: Int) {
        holder.onBind(position)

    }
    override fun getItemCount(): Int {
        return list.size
    }

}