package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.AllergiesMetric
import com.example.bachelor_work.utils.DeletableRecordProvider

class AllergiesAdapter(private val allergiesMetrics: MutableList<AllergiesMetric>) :
    RecyclerView.Adapter<AllergiesAdapter.AllergiesMetricViewHolder>(),DeletableRecordProvider {

    private lateinit var editableAllergiesMetricProvider: EditableAllergiesMetricProvider
    private lateinit var itemClickListener: OnItemClickListener

    // Interface to provide access to edit allergies metric dialog
    interface EditableAllergiesMetricProvider {
        fun showEditAllergiesMetricDialog(allergiesMetric: AllergiesMetric)
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(allergiesMetric: AllergiesMetric)
    }

    // Method to set the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // Method to set the editable allergies metric provider
    fun setEditableAllergiesMetricProvider(provider: EditableAllergiesMetricProvider) {
        editableAllergiesMetricProvider = provider
    }
    override fun deleteRecord(record: Any) {
        if (record is AllergiesMetric) {
            val position = allergiesMetrics.indexOf(record)
            if (position != -1) {
                allergiesMetrics.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    // ViewHolder class
    inner class AllergiesMetricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.metricTitleTextView)

        init {
            // Set OnClickListener to open edit dialog when item is clicked
            nameTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val allergiesMetric = allergiesMetrics[position]
                    itemClickListener.onItemClick(allergiesMetric)
                }
            }
        }

        fun bind(allergiesMetric: AllergiesMetric) {
            nameTextView.text = allergiesMetric.type
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergiesMetricViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false)
        return AllergiesMetricViewHolder(view)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: AllergiesMetricViewHolder, position: Int) {
        val allergiesMetric = allergiesMetrics[position]
        holder.bind(allergiesMetric)
    }

    // getItemCount
    override fun getItemCount(): Int = allergiesMetrics.size

    // Function to add a new allergies metric
    fun addAllergiesMetric(allergiesMetric: AllergiesMetric) {
        allergiesMetrics.add(allergiesMetric)
        notifyDataSetChanged()
    }

    // Function to update an existing allergies metric
    fun updateAllergiesMetric(oldAllergiesMetric: AllergiesMetric, newAllergiesMetric: AllergiesMetric) {
        val position = allergiesMetrics.indexOf(oldAllergiesMetric)
        if (position != -1) {
            allergiesMetrics[position] = newAllergiesMetric
            notifyItemChanged(position)
        }
    }

    fun getAllergiesMetrics(): MutableList<AllergiesMetric> {
        return allergiesMetrics
    }
}
