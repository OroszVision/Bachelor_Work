package com.example.bachelor_work.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.StrengthMetric

class StrengthMetricsAdapter(private val strengthMetrics: MutableList<StrengthMetric>) :
    RecyclerView.Adapter<StrengthMetricsAdapter.StrengthMetricViewHolder>() {

    private lateinit var editableStrengthMetricProvider: EditableStrengthMetricProvider
    private lateinit var itemClickListener: OnItemClickListener

    // Interface to provide access to edit strength metric dialog
    interface EditableStrengthMetricProvider {
        fun showEditStrengthMetricDialog(strengthMetric: StrengthMetric)
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(strengthMetric: StrengthMetric)
    }

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
        private val nameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.maxLiftTextView)

        init {
            // Set OnClickListener to open edit dialog when item is clicked
            itemView.setOnClickListener {
                Log.d("StrengthMetricsAdapter", "Item clicked")
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
        notifyDataSetChanged()
    }

    // Function to update an existing strength metric
    fun updateStrengthMetric(oldStrengthMetric: StrengthMetric, newStrengthMetric: StrengthMetric) {
        val position = strengthMetrics.indexOf(oldStrengthMetric)
        if (position != -1) {
            strengthMetrics[position] = newStrengthMetric
            notifyItemChanged(position)
        }
    }
}
