package com.example.bachelor_work.adapter

import android.view.View
import android.widget.EditText
import com.example.bachelor_work.R
import com.example.bachelor_work.model.PersonalNote

class PersonalNotesAdapter(
    data: MutableList<PersonalNote>,
    onItemClick: ((PersonalNote) -> Unit)? = null,
    onItemLongClick: ((PersonalNote) -> Unit)? = null
) : BaseAdapter<PersonalNote>(R.layout.dialog_add_other_metric, data, onItemClick, onItemLongClick) {

    override fun createViewHolder(view: View): BaseViewHolder {
        return PersonalNotesViewHolder(view)
    }

    inner class PersonalNotesViewHolder(itemView: View) : BaseAdapter<PersonalNote>.BaseViewHolder(itemView) {
        private val metricNameEditText: EditText = itemView.findViewById(R.id.metricNameEditText)

        override fun bind(item: PersonalNote) {
            metricNameEditText.setText(item.content)
        }
    }
}
