package com.app.thefruitsspirit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.databinding.ItemYearlyYearBinding
import com.app.thefruitsspirit.model.YearModel
import java.util.Calendar

class AdapterYear(
    val context: Context, private var list: ArrayList<YearModel>,
    var onClickYear: OnClickYear) :
    RecyclerView.Adapter<AdapterYear.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemYearlyYearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            binding.tvYear.text = list[position].year

            binding.tvYear.setOnClickListener {
                onClickYear.yearClick(list[position].year)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterYear.ViewHolder {
        val binding =
            ItemYearlyYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterYear.ViewHolder, position: Int) {
        holder.onBind(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickYear {
        fun yearClick(year: String)
    }
}