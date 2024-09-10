package com.rohit.ticketcollect.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohit.ticketcollect.R
import com.rohit.ticketcollect.databinding.ItemTimeBinding

class TimeAdapter(private val timeSlots: List<String>) :
    RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class TimeViewHolder(private val binding: ItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(time: String) {
            binding.TextViewTime.text = time
            if (selectedPosition == position){
                binding.TextViewTime.setBackgroundResource(R.drawable.white_bg)
                binding.TextViewTime.setTextColor(ContextCompat.getColor(itemView.context,R.color.black))
            }
            else{
                binding.TextViewTime.setBackgroundResource(R.drawable.light_black_bg)
                binding.TextViewTime.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
            }

            binding.root.setOnClickListener {
                val position = position
                if (position!=RecyclerView.NO_POSITION){
                    lastSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapter.TimeViewHolder {
        return TimeViewHolder(
            ItemTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TimeAdapter.TimeViewHolder, position: Int) {
holder.bind(timeSlots[position])    }

    override fun getItemCount(): Int = timeSlots.size
}