package com.example.bachelor_work.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.adapter.WeightAdapter
import com.example.bachelor_work.model.DialogInfo
import com.example.bachelor_work.model.WeightItem
import com.example.bachelor_work.utils.DialogStorage
import com.example.bachelor_work.utils.ToolBarHelper.Companion.round
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReversePyramidCalculatorFragment : Fragment() {

    private lateinit var startingWeightEditText: EditText
    private lateinit var startingRepsPicker: NumberPicker
    private lateinit var numSetsPicker: NumberPicker
    private lateinit var percentageDecreasePicker: NumberPicker
    private lateinit var calculateButton: Button
    private lateinit var weightTableDialog: BottomSheetDialog
    private lateinit var weightRecyclerView: RecyclerView
    private lateinit var closeButton: Button
    private lateinit var backgroundOverlay: View
    private lateinit var exportButton: Button

    private lateinit var dialogStorage: DialogStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reverse_pyramid_calculator, container, false)

        startingWeightEditText = view.findViewById(R.id.starting_weight_edittext)
        startingRepsPicker = view.findViewById(R.id.starting_reps_picker)
        numSetsPicker = view.findViewById(R.id.num_sets_picker)
        percentageDecreasePicker = view.findViewById(R.id.percentage_decrease_picker)
        calculateButton = view.findViewById(R.id.calculate_button)
        backgroundOverlay = view.findViewById(R.id.backgroundOverlay)

        // Initialize the dialog and its components
        val dialogView = inflater.inflate(R.layout.dialog_weight_table, container, false)
        weightTableDialog = BottomSheetDialog(requireContext())
        weightTableDialog.setContentView(dialogView)
        weightRecyclerView = dialogView.findViewById(R.id.weightRecyclerView)!!
        closeButton = dialogView.findViewById(R.id.closeButton)
        exportButton = dialogView.findViewById(R.id.exportButton)

        setupPickers()

        dialogStorage = DialogStorage(requireContext())

        super.onViewCreated(view, savedInstanceState)

        calculateButton.setOnClickListener {
            calculateReversePyramid()
            toggleWeightTableDialog()
        }

        closeButton.setOnClickListener {
            toggleWeightTableDialog()
        }

        exportButton.setOnClickListener {
            val adapter = weightRecyclerView.adapter as WeightAdapter
            val fragmentName = "Reverse Pyramid Calculator"
            val timestamp = System.currentTimeMillis().toString()
            val fileName = "ReversePyramid_$timestamp.pdf"
            dialogStorage.exportDialogToPDF(requireContext(), adapter, fileName, fragmentName)
        }

        return view
    }

    private fun setupPickers() {
        startingRepsPicker.minValue = 1
        startingRepsPicker.maxValue = 20
        startingRepsPicker.value = 10

        numSetsPicker.minValue = 1
        numSetsPicker.maxValue = 20
        numSetsPicker.value = 5

        percentageDecreasePicker.minValue = 1
        percentageDecreasePicker.maxValue = 20
        percentageDecreasePicker.value = 5
    }

    private fun toggleWeightTableDialog() {
        if (weightTableDialog.isShowing) {
            weightTableDialog.dismiss()
            backgroundOverlay.visibility = View.GONE
        } else {
            weightTableDialog.show()
            backgroundOverlay.visibility = View.VISIBLE
        }
    }

    private fun calculateReversePyramid() {
        val startingWeight = startingWeightEditText.text.toString().toDoubleOrNull()
        val startingReps = startingRepsPicker.value
        val numSets = numSetsPicker.value
        val weightDecreasePercentage = percentageDecreasePicker.value.toDouble()

        if (startingWeight != null) {
            val repsList = calculateRepsForReversePyramid(startingWeight, startingReps, numSets, weightDecreasePercentage)
            val weightList = populateRecyclerView(repsList, startingWeight, weightDecreasePercentage)

            // Store dialog with calculation result and timestamp
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timestampString = dateFormat.format(Date())
            storeDialog(timestampString, weightList)
        }
    }

    private fun populateRecyclerView(repsList: List<Int>, startingWeight: Double, weightDecreasePercentage: Double): List<WeightItem> {
        val weightList = mutableListOf<WeightItem>()
        var currentWeight = startingWeight

        for ((index, reps) in repsList.withIndex()) {
            // Calculate weight for the current set
            val weightForSet = currentWeight * (1 - weightDecreasePercentage * index / 100)

            // Calculate percentage weight for the current set
            val percentageWeight = 100 - weightDecreasePercentage * index

            // Add WeightItem with all information
            weightList.add(WeightItem(percentageWeight.toInt(), index + 1, reps, weightForSet.round(2)))

            // Decrease weight for the next set
            currentWeight *= (1 - weightDecreasePercentage / 100)
        }

        val adapter = WeightAdapter(weightList)
        weightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        weightRecyclerView.adapter = adapter

        return weightList
    }

    private fun calculateRepsForReversePyramid(startingWeight: Double, startingReps: Int, numSets: Int, weightDecreasePercentage: Double): List<Int> {
        val repsList = mutableListOf<Int>()

        var currentWeight = startingWeight
        var currentReps = startingReps

        // Calculate reps for each set
        for (i in 0 until numSets) {
            // Calculate weight for the current set
            val weightForSet = currentWeight * (1 - weightDecreasePercentage * i / 100)

            // Calculate estimated reps for the current set
            val repsForSet = (currentReps * (startingWeight / weightForSet)).toInt()

            // Add the reps to the list
            repsList.add(repsForSet)

            // Decrease weight for the next set
            currentWeight *= (1 - weightDecreasePercentage / 100)
        }

        return repsList
    }

    private fun storeDialog(timestamp: String, weightItems: List<WeightItem>) {
        // Create DialogInfo object with necessary data and store it using dialogStorage
        val dialogInfo = DialogInfo("Reverse Pyramid Calculator", timestamp, weightItems)
        dialogStorage.storeDialog(dialogInfo)
    }
}
