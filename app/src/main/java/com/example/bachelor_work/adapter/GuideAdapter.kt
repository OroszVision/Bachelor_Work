// com.example.bachelor_work.adapter.GuideAdapter.kt
package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.GuideEntry

class GuideAdapter(
    private val guideEntries: List<GuideEntry>,
    private val onClick: (GuideEntry) -> Unit
) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guideEntry = guideEntries[position]
        holder.bind(guideEntry)
    }

    override fun getItemCount() = guideEntries.size

    inner class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.calculator_name)

        fun bind(guideEntry: GuideEntry) {
            titleTextView.text = guideEntry.title
            itemView.setOnClickListener { onClick(guideEntry) }
        }
    }
}
