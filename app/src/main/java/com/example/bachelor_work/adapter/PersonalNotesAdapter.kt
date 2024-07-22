package com.example.bachelor_work.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.PersonalNote
import com.example.bachelor_work.utils.DeletableRecordProvider

class PersonalNotesAdapter(private val personalNotes: MutableList<PersonalNote>) :
    RecyclerView.Adapter<PersonalNotesAdapter.PersonalNoteViewHolder>(), DeletableRecordProvider {

    private lateinit var editablePersonalNoteProvider: EditablePersonalNoteProvider
    private lateinit var itemClickListener: OnItemClickListener

    // Interface to provide access to edit personal note dialog
    interface EditablePersonalNoteProvider {
        fun showEditPersonalNoteDialog(personalNote: PersonalNote)
    }

    // Interface to handle item click events
    interface OnItemClickListener {
        fun onItemClick(personalNote: PersonalNote)
    }

    // Method to set the click listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    // Method to set the editable personal note provider
    fun setEditablePersonalNoteProvider(provider: EditablePersonalNoteProvider) {
        editablePersonalNoteProvider = provider
    }

    override fun deleteRecord(record: Any) {
        if (record is PersonalNote) {
            val position = personalNotes.indexOf(record)
            if (position != -1) {
                personalNotes.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    // ViewHolder class
    inner class PersonalNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.metricTitleTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.metricValueTextView)

        init {
            // Set OnClickListener to open edit dialog when item is clicked
            titleTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val personalNote = personalNotes[position]
                    itemClickListener.onItemClick(personalNote)
                }
            }
        }

        fun bind(personalNote: PersonalNote) {
            titleTextView.text = personalNote.title
            contentTextView.text = personalNote.content
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalNoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.personal_note_recycler_view, parent, false)
        return PersonalNoteViewHolder(view)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: PersonalNoteViewHolder, position: Int) {
        val personalNote = personalNotes[position]
        holder.bind(personalNote)
    }

    // getItemCount
    override fun getItemCount(): Int = personalNotes.size

    // Function to add a new personal note
    fun addPersonalNote(personalNote: PersonalNote) {
        personalNotes.add(personalNote)
        notifyDataSetChanged()
    }

    // Function to update an existing personal note
    fun updatePersonalNote(oldPersonalNote: PersonalNote, newPersonalNote: PersonalNote) {
        val position = personalNotes.indexOf(oldPersonalNote)
        if (position != -1) {
            personalNotes[position] = newPersonalNote
            notifyItemChanged(position)
        }
    }

    fun getPersonalNotes(): MutableList<PersonalNote> {
        return personalNotes
    }
}
