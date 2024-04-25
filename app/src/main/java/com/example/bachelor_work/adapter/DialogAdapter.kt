package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.DialogInfo

class DialogAdapter(private val dialogList: List<DialogInfo>) :
    RecyclerView.Adapter<DialogAdapter.DialogViewHolder>() {

    private var onItemClickListener: ((DialogInfo) -> Unit)? = null

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val dialogInfo = dialogList[position]
                    onItemClickListener?.invoke(dialogInfo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dialog, parent, false)
        return DialogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val currentItem = dialogList[position]
        holder.titleTextView.text = currentItem.title
        holder.timestampTextView.text = currentItem.timestamp
    }

    override fun getItemCount() = dialogList.size

    fun setOnItemClickListener(listener: (DialogInfo) -> Unit) {
        onItemClickListener = listener
    }
}
