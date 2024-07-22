package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.InjuryMetric
import com.example.bachelor_work.utils.DeletableRecordProvider

class InjuryHistoryAdapter(private val injuryMetrics: MutableList<InjuryMetric>) :
    RecyclerView.Adapter<InjuryHistoryAdapter.InjuryMetricViewHolder>(),DeletableRecordProvider {

    private lateinit var editableInjuryMetricProvider: EditableInjuryMetricProvider
    private lateinit var itemClickListener: OnItemClickListener

    // Interface to provide access to edit injury metric dialog
    interface EditableInjuryMetricProvider {
        fun showEditInjuryMetricDialog(injuryMetric: InjuryMetric)
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(injuryMetric: InjuryMetric)
    }

    // Method to set the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // Method to set the editable injury metric provider
    fun setEditableInjuryMetricProvider(provider: EditableInjuryMetricProvider) {
        editableInjuryMetricProvider = provider
    }
    override fun deleteRecord(record: Any) {
        if (record is InjuryMetric) {
            val position = injuryMetrics.indexOf(record)
            if (position != -1) {
                injuryMetrics.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }
    // ViewHolder class
    inner class InjuryMetricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.metricTitleTextView)
        private val valueTextView: TextView = itemView.findViewById(R.id.metricValueTextView)

        init {
            // Set OnClickListener to open edit dialog when item is clicked
            nameTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val injuryMetric = injuryMetrics[position]
                    itemClickListener.onItemClick(injuryMetric)
                }
            }
        }

        fun bind(injuryMetric: InjuryMetric) {
            nameTextView.text = injuryMetric.details
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InjuryMetricViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_view, parent, false)
        return InjuryMetricViewHolder(view)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: InjuryMetricViewHolder, position: Int) {
        val injuryMetric = injuryMetrics[position]
        holder.bind(injuryMetric)
    }

    // getItemCount
    override fun getItemCount(): Int = injuryMetrics.size

    // Function to add a new injury metric
    fun addInjuryMetric(injuryMetric: InjuryMetric) {
        injuryMetrics.add(injuryMetric)
        notifyDataSetChanged()
    }

    // Function to update an existing injury metric
    fun updateInjuryMetric(oldInjuryMetric: InjuryMetric, newInjuryMetric: InjuryMetric) {
        val position = injuryMetrics.indexOf(oldInjuryMetric)
        if (position != -1) {
            injuryMetrics[position] = newInjuryMetric
            notifyItemChanged(position)
        }
    }

    fun getInjuryMetrics(): MutableList<InjuryMetric> {
        return injuryMetrics
    }
}

