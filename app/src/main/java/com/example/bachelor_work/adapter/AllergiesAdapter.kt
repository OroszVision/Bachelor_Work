package com.example.bachelor_work.adapter

import android.view.View
import android.widget.EditText
import com.example.bachelor_work.R
import com.example.bachelor_work.model.Allergy

class AllergiesAdapter(
    data: MutableList<Allergy>,
    onItemClick: ((Allergy) -> Unit)? = null,
    onItemLongClick: ((Allergy) -> Unit)? = null
) : BaseAdapter<Allergy>(R.layout.dialog_add_other_metric, data, onItemClick, onItemLongClick) {

    override fun createViewHolder(view: View): BaseViewHolder {
        return AllergiesViewHolder(view)
    }

    inner class AllergiesViewHolder(itemView: View) : BaseAdapter<Allergy>.BaseViewHolder(itemView) {
        private val metricNameEditText: EditText = itemView.findViewById(R.id.metricNameEditText)

        override fun bind(item: Allergy) {
            metricNameEditText.setText(item.type)
        }
    }
}
