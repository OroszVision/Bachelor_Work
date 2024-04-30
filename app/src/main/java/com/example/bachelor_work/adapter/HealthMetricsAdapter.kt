package com.example.bachelor_work.adapter

import android.view.View
import android.widget.EditText
import com.example.bachelor_work.R
import com.example.bachelor_work.model.HealthMetric

class HealthMetricsAdapter(
    data: MutableList<HealthMetric>,
    onItemClick: ((HealthMetric) -> Unit)? = null,
    onItemLongClick: ((HealthMetric) -> Unit)? = null
) : BaseAdapter<HealthMetric>(R.layout.dialog_add_health_metric, data, onItemClick, onItemLongClick) {

    override fun createViewHolder(view: View): BaseViewHolder {
        return HealthMetricsViewHolder(view)
    }

    inner class HealthMetricsViewHolder(itemView: View) : BaseAdapter<HealthMetric>.BaseViewHolder(itemView) {
        private val healthMetricNameEditText: EditText = itemView.findViewById(R.id.healthMetricNameEditText)
        private val valueEditText: EditText = itemView.findViewById(R.id.valueEditText)

        override fun bind(item: HealthMetric) {
            healthMetricNameEditText.setText(item.metricName)
            valueEditText.setText(item.value.toString())
        }
    }
}
