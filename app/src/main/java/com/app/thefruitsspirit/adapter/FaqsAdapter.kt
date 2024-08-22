package com.app.thefruitsspirit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.databinding.ItemFaqsBinding
import com.app.thefruitsspirit.model.FaqsResponse
import com.app.thefruitsspirit.model.WelcomeModel
import com.app.thefruitsspirit.utils.isGone
import com.app.thefruitsspirit.utils.isVisible

class FaqsAdapter(val context: Context, private var list: ArrayList<FaqsResponse.Body>):
    RecyclerView.Adapter<FaqsAdapter.ViewHolder>() {
    private var isClick = false
    inner class ViewHolder(private val binding: ItemFaqsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
//             binding.ivPlus.setOnClickListener {
//                 binding.tvContent.isVisible()
//                 binding.ivPlus.isGone()
//                 binding.ivMinus.isVisible()
//             }
//            binding.ivMinus.setOnClickListener {
//                binding.tvContent.isGone()
//                binding.ivPlus.isVisible()
//                binding.ivMinus.isGone()
//            }


            binding.clOut.setOnClickListener {
                if (isClick) {
                    isClick = false
                    binding.ivPlus.setImageResource(R.drawable.minus_icon)
                    binding.tvContent.isVisible()
                } else {
                    isClick = true
                    binding.ivPlus.setImageResource(R.drawable.plus_icon)
                    binding.tvContent.isGone()
                }
            }
           binding.tvName.text = list[position].question
           binding.tvContent.text = list[position].answer
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsAdapter.ViewHolder {
        val binding = ItemFaqsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FaqsAdapter.ViewHolder, position: Int) {
        holder.onBind(position)

    }
    override fun getItemCount(): Int {
        return list.size
    }

}