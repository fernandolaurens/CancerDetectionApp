package com.dicoding.asclepius.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter(private var listHistory: List<HistoryEntity>, private val context: Context) : RecyclerView.Adapter<HistoryAdapter.ListViewHolder>(){
    class ListViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val itemHistory = listHistory[position]
        with(holder.binding){
            Glide.with(root).load(itemHistory.image).into(imageList)
            tvCancer.text = itemHistory.label
            tvConfidence.text = itemHistory.confidenceScore
        }
    }
}