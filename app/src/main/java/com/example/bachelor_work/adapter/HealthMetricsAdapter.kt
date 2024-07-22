package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.HealthMetric
import com.example.bachelor_work.utils.DeletableRecordProvider

class HealthMetricsAdapter(private val healthMetrics: MutableList<HealthMetric>) :
    RecyclerView.Adapter<HealthMetricsAdapter.HealthMetricViewHolder>(), DeletableRecordProvider {

    private lateinit var editableHealthMetricProvider: EditableHealthMetricProvider
    private lateinit var itemClickListener: OnItemClickListener

    // Interface to provide access to edit health metric dialog
    interface EditableHealthMetricProvider {
        fun showEditHealthMetricDialog(healthMetric: HealthMetric)
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(healthMetric: HealthMetric)
    }

    // Method to set the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // Method to set the editable health metric provider
    fun setEditableHealthMetricProvider(provider: EditableHealthMetricProvider) {
        editableHealthMetricProvider = provider
    }
    override fun deleteRecord(record: Any) {
        if (record is HealthMetric) {
            val position = healthMetrics.indexOf(record)
            if (position != -1) {
                healthMetrics.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
    // ViewHolder class
    inner class HealthMetricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.metricTitleTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.metricValueTextView)

        init {
            // Set OnClickListener to open edit dialog when item is clicked
            nameTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val healthMetric = healthMetrics[position]
                    itemClickListener.onItemClick(healthMetric)
                }
            }
        }

        fun bind(healthMetric: HealthMetric) {
            nameTextView.text = healthMetric.metricName
            valueTextView.text = healthMetric.value.toString()
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthMetricViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false)
        return HealthMetricViewHolder(view)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: HealthMetricViewHolder, position: Int) {
        val healthMetric = healthMetrics[position]
        holder.bind(healthMetric)
    }

    // getItemCount
    override fun getItemCount(): Int = healthMetrics.size

    // Function to add a new health metric
    fun addHealthMetric(healthMetric: HealthMetric) {
        healthMetrics.add(healthMetric)
        notifyDataSetChanged()
    }

    // Function to update an existing health metric
    fun updateHealthMetric(oldHealthMetric: HealthMetric, newHealthMetric: HealthMetric) {
        val position = healthMetrics.indexOf(oldHealthMetric)
        if (position != -1) {
            healthMetrics[position] = newHealthMetric
            notifyItemChanged(position)
        }
    }

    fun getHealthMetrics(): MutableList<HealthMetric> {
        return healthMetrics
    }
}
