package com.app.thefruitsspirit.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.databinding.ItemMonthBinding
import com.app.thefruitsspirit.model.MonthModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class AdapterSummaryMonth(
    val context: Context,
    private var list: ArrayList<MonthModel>,
    private val isWithDraw: Boolean,
    private var onClickMonth: OnClickMonth,
    var monthId: String
    ) :
    RecyclerView.Adapter<AdapterSummaryMonth.ViewHolder>() {
    private val dateFormat: DateFormat = SimpleDateFormat("MM")
    private val date = Date()
    var curentDate = dateFormat.format(date)
    var index = "11"

    inner class ViewHolder(val binding: ItemMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.tvName.text = list[position].name
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSummaryMonth.ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: AdapterSummaryMonth.ViewHolder, position: Int) {
        holder.onBind(position)
        holder.itemView.setOnClickListener {
            onClickMonth.clickMonth(list[position].id)
            if (isWithDraw) {
                index = position.toString()
                notifyDataSetChanged()
            }
        }
        if (index == position.toString()) {
            holder.binding.rlName.setBackgroundResource(R.drawable.bg_button)
            holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.white))

        } else {
            holder.binding.rlName.setBackgroundResource(R.drawable.bg_gray_button)
            holder.binding.tvName.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        Log.d("jksfdjksfdjkhdsfjk", "" + list[position].id)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickMonth {
        fun clickMonth(id: String)
    }

}