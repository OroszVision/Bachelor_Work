package com.example.bachelor_work.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.bachelor_work.R

class GuideModalFragment : DialogFragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_TEXT = "text"
        private const val ARG_IMAGE = "image"

        fun newInstance(title: String, text: String, imageResId: Int): GuideModalFragment {
            val fragment = GuideModalFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_TEXT, text)
            args.putInt(ARG_IMAGE, imageResId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guide_modal, container, false)

        val titleTextView: TextView = view.findViewById(R.id.guideModalTitle)
        val imageView: ImageView = view.findViewById(R.id.guideModalImage)
        val textView: TextView = view.findViewById(R.id.guideModalText)

        arguments?.let {
            titleTextView.text = it.getString(ARG_TITLE)
            textView.text = it.getString(ARG_TEXT)
            imageView.setImageResource(it.getInt(ARG_IMAGE))
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
