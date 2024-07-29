package com.example.bachelor_work.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.adapter.GuideAdapter
import com.example.bachelor_work.model.GuideEntry

class GuideFragment : Fragment() {

    private lateinit var guideRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guide, container, false)

        guideRecyclerView = view.findViewById(R.id.guideRecyclerView)
        guideRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val guideEntries = listOf(
            GuideEntry("Calculators", R.drawable.calculator_example,
                getString(R.string.calculators_guide_description)),
            GuideEntry("History", 0, getString(R.string.history_guide_description)),
            GuideEntry("PDF Export", 0, getString(R.string.pdf_export_guide_description)),
            GuideEntry("Stopwatch", 0, getString(R.string.stopwatch_guide_description)),
            GuideEntry("Profile", 0, getString(R.string.profile_guide_description)),
            GuideEntry("Statistics", 0, getString(R.string.statistics_guide_description))
        )

        val adapter = GuideAdapter(guideEntries) { guideEntry ->
            showGuideModal(guideEntry)
        }
        guideRecyclerView.adapter = adapter

        return view
    }

    private fun showGuideModal(guideEntry: GuideEntry) {
        val guideModal = GuideModalFragment.newInstance(guideEntry.title, guideEntry.guideText, guideEntry.imageResId)
        guideModal.show(parentFragmentManager, "guideModal")
    }
}
