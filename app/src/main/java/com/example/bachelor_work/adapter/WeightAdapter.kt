package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.WeightItem

class WeightAdapter(private val weightList: List<WeightItem>) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val percentageTextView: TextView = itemView.findViewById(R.id.percentageTextView)
        val setsTextView: TextView = itemView.findViewById(R.id.setTextView)
        val repsTextView: TextView = itemView.findViewById(R.id.repsTextView)
        val weightTextView: TextView = itemView.findViewById(R.id.weightTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_weight_row, parent, false)
        return WeightViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val currentItem = weightList[position]
        holder.percentageTextView.text = if (currentItem.percentage != -1) "${currentItem.percentage}%" else "#"
        holder.setsTextView.text = if (currentItem.sets != -1) "${currentItem.sets}" else "#"
        holder.repsTextView.text = if (currentItem.reps != -1) "${currentItem.reps}" else "#"
        holder.weightTextView.text = if (currentItem.weight != -1.0) "${currentItem.weight} kg" else "#"
    }

    fun getItems(): List<WeightItem> {
        return weightList
    }

    override fun getItemCount() = weightList.size
}
