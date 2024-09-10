package com.rohit.ticketcollect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohit.ticketcollect.Models.Cast
import com.rohit.ticketcollect.databinding.ViewholderCastBinding

class CastListAdapter(private val cast : ArrayList<Cast>):RecyclerView.Adapter<CastListAdapter.ViewHolder>() {
    private var context:Context?=null
   inner class ViewHolder(private val binding: ViewholderCastBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast){
            context?.let {
                Glide.with(it)
                    .load(cast.PicUrl)
                    .into(binding.actorImage)
            }
            binding.nameTxt.text = cast.Actor
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastListAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderCastBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastListAdapter.ViewHolder, position: Int) {
holder.bind(cast[position])    }

    override fun getItemCount(): Int {
return cast.size    }

}