package com.example.bachelor_work.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.bachelor_work.R
import com.example.bachelor_work.utils.ChartTitle
import com.example.bachelor_work.utils.ProfileDataHandler
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class StatisticsFragment : Fragment() {

    private lateinit var profileDataHandler: ProfileDataHandler
    private lateinit var chartsContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        profileDataHandler = ProfileDataHandler(requireContext())
        chartsContainer = view.findViewById(R.id.chartsContainer)

        val statisticsData = profileDataHandler.getStatisticsData()
        displayCurrentData(statisticsData)

        return view
    }

    private fun displayCurrentData(statisticsData: Map<String, List<Pair<String, Float>>>) {
        displayHorizontalBarChart(statisticsData["Strength Metrics"] ?: emptyList(), "Strength Metrics")
        displayPieChart(statisticsData["Health Metrics"] ?: emptyList(), "Health Metrics")
    }

    private fun displayHorizontalBarChart(data: List<Pair<String, Float>>, title: String) {
        val barChart = BarChart(requireContext())
        barChart.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.chart_height)
        )
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.first })
        xAxis.labelCount = data.size

        val leftAxis = barChart.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f

        barChart.axisRight.isEnabled = false

        val entries = data.mapIndexed { index, pair -> BarEntry(index.toFloat(), pair.second) }
        val dataSet = BarDataSet(entries, title)
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barData.barWidth = 0.5f

        barChart.data = barData
        barChart.animateY(1000, Easing.EaseInOutQuad)
        barChart.invalidate()

        val chartContainer = createChartContainer(barChart, title)
        chartsContainer.addView(chartContainer)
    }

    private fun displayPieChart(data: List<Pair<String, Float>>, title: String) {
        val pieChart = PieChart(requireContext())
        pieChart.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.chart_height)
        )
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)
        pieChart.transparentCircleRadius = 61f

        val entries = data.map { PieEntry(it.second, it.first) }
        val dataSet = PieDataSet(entries, title)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.BLACK)

        pieChart.data = pieData
        pieChart.animateY(1000, Easing.EaseInOutQuad)
        pieChart.invalidate()

        val chartContainer = createChartContainer(pieChart, title)
        chartsContainer.addView(chartContainer)
    }

    private fun createChartContainer(chart: View, title: String): LinearLayout {
        val chartContainer = LinearLayout(requireContext())
        chartContainer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chartContainer.orientation = LinearLayout.VERTICAL

        val chartTitle = ChartTitle(requireContext())
        chartTitle.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chartTitle.setPadding(16, 16, 16, 8)
        chartTitle.text = title
        chartTitle.setTextColor(Color.BLACK)
        chartTitle.textSize = 18f

        chartContainer.addView(chartTitle)
        chartContainer.addView(chart)

        return chartContainer
    }
}
