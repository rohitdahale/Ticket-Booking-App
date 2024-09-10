package com.rohit.ticketcollect.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.ticketcollect.databinding.ViewholderCategoryBinding

class CategoryEachFilmAdapter(private var items :List<String>):RecyclerView.Adapter<CategoryEachFilmAdapter.ViewHolder>() {
    class ViewHolder(val binding: ViewholderCategoryBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CategoryEachFilmAdapter.ViewHolder {

        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryEachFilmAdapter.ViewHolder, position: Int) {
        holder.binding.titleTxt.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}