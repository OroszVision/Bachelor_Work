package com.example.bachelor_work.adapter

import android.view.View
import android.widget.EditText
import com.example.bachelor_work.R
import com.example.bachelor_work.model.Injury

class InjuryHistoryAdapter(
    data: MutableList<Injury>,
    onItemClick: ((Injury) -> Unit)? = null,
    onItemLongClick: ((Injury) -> Unit)? = null
) : BaseAdapter<Injury>(R.layout.dialog_add_other_metric, data, onItemClick, onItemLongClick) {

    override fun createViewHolder(view: View): BaseViewHolder {
        return InjuryHistoryViewHolder(view)
    }

    inner class InjuryHistoryViewHolder(itemView: View) : BaseAdapter<Injury>.BaseViewHolder(itemView) {
        private val metricNameEditText: EditText = itemView.findViewById(R.id.metricNameEditText)

        override fun bind(item: Injury) {
            metricNameEditText.setText(item.details)
        }
    }
}
