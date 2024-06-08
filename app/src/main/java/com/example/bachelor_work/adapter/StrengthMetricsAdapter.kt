package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.StrengthMetric

class StrengthMetricsAdapter(private val strengthMetrics: MutableList<StrengthMetric>) :
    RecyclerView.Adapter<StrengthMetricsAdapter.StrengthMetricViewHolder>() {

    // Define listener interfaces
    interface EditableStrengthMetricProvider {
        fun showEditStrengthMetricDialog(strengthMetric: StrengthMetric)
    }

    interface OnItemClickListener {
        fun onItemClick(strengthMetric: StrengthMetric)
    }

    private lateinit var editableStrengthMetricProvider: EditableStrengthMetricProvider
    private lateinit var itemClickListener: OnItemClickListener

    // Method to set the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // Method to set the editable strength metric provider
    fun setEditableStrengthMetricProvider(provider: EditableStrengthMetricProvider) {
        editableStrengthMetricProvider = provider
    }

    // ViewHolder class
    inner class StrengthMetricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.metricTitleTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.metricValueTextView)

        init {
            // Set OnClickListener to open edit dialog when item is clicked
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val strengthMetric = strengthMetrics[position]
                    itemClickListener.onItemClick(strengthMetric)
                }
            }
        }

        fun bind(strengthMetric: StrengthMetric) {
            nameTextView.text = strengthMetric.exerciseName
            valueTextView.text = strengthMetric.maxLift.toString()
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrengthMetricViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false)
        return StrengthMetricViewHolder(view)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: StrengthMetricViewHolder, position: Int) {
        val strengthMetric = strengthMetrics[position]
        holder.bind(strengthMetric)
    }

    // getItemCount
    override fun getItemCount(): Int = strengthMetrics.size

    // Function to add a new strength metric
    fun addStrengthMetric(strengthMetric: StrengthMetric) {
        strengthMetrics.add(strengthMetric)
        notifyItemInserted(strengthMetrics.size - 1) // Notify adapter about the new item
    }

    // Function to update an existing strength metric
    fun updateStrengthMetric(oldStrengthMetric: StrengthMetric, newStrengthMetric: StrengthMetric) {
        val position = strengthMetrics.indexOf(oldStrengthMetric)
        if (position != -1) {
            strengthMetrics[position] = newStrengthMetric
            notifyItemChanged(position) // Notify adapter about the changed item
        }
    }

    fun getStrengthMetrics(): MutableList<StrengthMetric> {
        return strengthMetrics
    }
}
