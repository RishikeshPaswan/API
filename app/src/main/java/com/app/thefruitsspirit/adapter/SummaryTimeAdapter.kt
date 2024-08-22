package com.app.thefruitsspirit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.databinding.ItemButtonBinding
import com.app.thefruitsspirit.model.WelcomeModel
class SummaryTimeAdapter(val context: Context, private var list: ArrayList<WelcomeModel>, val isWithDraw: Boolean, var onSummaryClick : OnSummaryClick ) :
    RecyclerView.Adapter<SummaryTimeAdapter.ViewHolder>() {
    var index = 0
    inner class ViewHolder(val binding: ItemButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.tvName.text = list[position].name
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SummaryTimeAdapter.ViewHolder {
        val binding = ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SummaryTimeAdapter.ViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            onSummaryClick.click(position)
            if (isWithDraw) {
                index = position
                notifyDataSetChanged()
            }
        }
        if (index == position) {
            holder.binding.rlName.setBackgroundResource(R.drawable.bg_button)
            holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.white))

        } else {
            holder.binding.rlName.setBackgroundResource(R.drawable.bg_gray_button)
            holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    interface OnSummaryClick{
        fun click(position: Int)
    }
}