package com.app.thefruitsspirit.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.thefruitsspirit.R
import com.app.thefruitsspirit.databinding.ItemHomeBinding
import com.app.thefruitsspirit.model.HomeResponse
class HomeAdapter(val context: Context,
    private var list: ArrayList<HomeResponse.Body>, var onClick: OnClick) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    var listColor = arrayListOf("#ED2403", "#B3F9FF7D", "#B34895F6", "#B3844204", "#B3446635","#B3EF52B0","#B3914178","#804B0082","#B3ED7015")

    var pickColor = 0
    var myColor = ""

    inner class ViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            var id = list[position]._id
            binding.tvJoye.text = list[position].name

            binding.clItemHome.setOnClickListener {
                onClick.click(position, id.toString())
            }

            if (((position + 1) % 9) == 0) {
                myColor = listColor[pickColor]
                pickColor = 0
            } else {
                myColor = listColor[pickColor]
                pickColor += 1
            }

            try {
                val unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.bg_item)
                if (unwrappedDrawable != null) {
                    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
                    DrawableCompat.setTint(wrappedDrawable, Color.parseColor(myColor))
                    binding.clItemHome.setBackgroundResource(R.drawable.bg_item)
                }
            } catch (e: Exception) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        holder.onBind(position)

    }

    override fun getItemCount(): Int {
        return 8
    }

    interface OnClick {
        fun click(position: Int,id: String)
    }
}