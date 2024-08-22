package com.app.thefruitsspirit.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.constants.ApiConstants
import com.app.thefruitsspirit.databinding.ItemNotificationBinding
import com.app.thefruitsspirit.model.GetNotificationResponse
import com.app.thefruitsspirit.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class NotificationAdapter(
    val context: Context,
    private var list: ArrayList<GetNotificationResponse.Body>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {

            binding.apply {
                 if (list[position].type == "1"){
                     binding.clLayout.setOnClickListener {
                         binding.clLayout.findNavController().navigate(R.id.seeAllFragments)
                     }
                 }else{
                     binding.clLayout.setOnClickListener {
                         val bundle = Bundle()
                         bundle.putString("type",list[position].type)
                         bundle.putString("id", list[position].productId?._id)
                         bundle.putString("image",list[position].productId?.image)
                         binding.clLayout.findNavController()
                             .navigate(R.id.recipesDetailsFragments, bundle)
                     }
                 }


                tvContent.text = list[position].productId?.name
                tvName.text = list[position].message
                tvOneDayAgo.text = Utils.parseDateToddMMyyyy(list[position].createdAt, "MMM dd yyyy HH:mm a")
                Glide.with(context).load(ApiConstants.PRODUCT_IMAGE + list[position]?.productId?.image).apply(
                    RequestOptions.placeholderOf(R.drawable.placeholder_img)
                ).into(binding.ivImage)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.onBind(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }

}