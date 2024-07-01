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
            GuideEntry("Calculators", R.drawable.baseline_add_circle_24, "Use the calculators for One rep max, Drop-set, Pyramid, etc. These calculators help you estimate your maximum lifts, plan your workout sets, and much more."),
            GuideEntry("History", R.drawable.baseline_add_circle_24, "Keep track of your past calculations and workouts. This section allows you to review and analyze your progress over time."),
            GuideEntry("PDF Export", R.drawable.baseline_add_circle_24, "Export your calculated tables as PDFs. This feature is useful for sharing your workout plans and results with trainers or keeping a physical record."),
            GuideEntry("Stopwatch", R.drawable.baseline_add_circle_24, "Use the stopwatch to time your workout sessions, rest periods, and more. It helps in maintaining the desired intensity and timing in your workouts."),
            GuideEntry("Profile", R.drawable.baseline_add_circle_24, "Manage your personal data such as measurements, max lifts, and allergies. This section helps you keep track of your personal fitness metrics."),
            GuideEntry("Statistics", R.drawable.baseline_add_circle_24, "View graphs and charts that visualize your health and strength metrics. This feature helps you compare and analyze your progress.")
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
