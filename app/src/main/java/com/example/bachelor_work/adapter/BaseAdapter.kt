package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(
    private val layoutResId: Int,
    private val data: MutableList<T>,
    private val onItemClick: ((T) -> Unit)? = null,
    private val onItemLongClick: ((T) -> Unit)? = null
) : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        val viewHolder = createViewHolder(view)
        onItemClick?.let { listener ->
            view.setOnClickListener {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(data[position])
                }
            }
        }
        onItemLongClick?.let { listener ->
            view.setOnLongClickListener {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(data[position])
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    abstract fun createViewHolder(view: View): BaseViewHolder

    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: T) {}
    }

    fun addItem(item: T) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun editItem(item: T, position: Int) {
        data[position] = item
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}
