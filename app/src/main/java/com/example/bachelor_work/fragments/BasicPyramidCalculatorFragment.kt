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

class BasicPyramidCalculatorFragment : Fragment() {

    private lateinit var startingWeightEditText: EditText
    private lateinit var startingRepsPicker: NumberPicker
    private lateinit var numSetsPicker: NumberPicker
    private lateinit var percentageIncreasePicker: NumberPicker
    private lateinit var calculateButton: Button
    private lateinit var weightTableDialog: BottomSheetDialog
    private lateinit var weightRecyclerView: RecyclerView
    private lateinit var closeButton: Button
    private lateinit var backgroundOverlay: View
    private lateinit var dialogStorage: DialogStorage
    private lateinit var exportButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basic_pyramid_calculator, container, false)

        startingWeightEditText = view.findViewById(R.id.starting_weight_edittext)
        startingRepsPicker = view.findViewById(R.id.starting_reps_picker)
        numSetsPicker = view.findViewById(R.id.num_sets_picker)
        percentageIncreasePicker = view.findViewById(R.id.percentage_increase_picker)
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
            calculatePyramid()
            toggleWeightTableDialog()
        }

        closeButton.setOnClickListener {
            toggleWeightTableDialog()
        }

        exportButton.setOnClickListener {
            val adapter = weightRecyclerView.adapter as WeightAdapter
            val fragmentName = "Basic Pyramid Calculator"
            val timestamp = System.currentTimeMillis().toString()
            val fileName = "BasicPyramid_$timestamp.pdf"
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

        percentageIncreasePicker.minValue = 1
        percentageIncreasePicker.maxValue = 20
        percentageIncreasePicker.value = 5
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

    private fun calculatePyramid() {
        val startingWeight = startingWeightEditText.text.toString().toDoubleOrNull()
        val startingReps = startingRepsPicker.value
        val numSets = numSetsPicker.value
        val weightIncreasePercentage = percentageIncreasePicker.value.toDouble()

        if (startingWeight != null) {
            val repsList = calculateRepsForPyramid(startingWeight, startingReps, numSets, weightIncreasePercentage)
            val weightList = populateRecyclerView(repsList, startingWeight, weightIncreasePercentage)

            // Store dialog with calculation result and timestamp
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timestampString = dateFormat.format(Date())
            storeDialog(timestampString, weightList)
        }
    }

    private fun populateRecyclerView(repsList: List<Int>, startingWeight: Double, weightIncreasePercentage: Double): List<WeightItem> {
        val weightList = mutableListOf<WeightItem>()
        var currentWeight = startingWeight

        for ((index, reps) in repsList.withIndex()) {
            val weightForSet = currentWeight * (1 + weightIncreasePercentage * index / 100)
            val percentageWeight = 100 + weightIncreasePercentage * index
            weightList.add(WeightItem(percentageWeight.toInt(), index + 1, reps, weightForSet.round(2)))
            currentWeight *= (1 + weightIncreasePercentage / 100)
        }

        val adapter = WeightAdapter(weightList)
        weightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        weightRecyclerView.adapter = adapter

        return weightList
    }

    private fun calculateRepsForPyramid(startingWeight: Double, startingReps: Int, numSets: Int, weightIncreasePercentage: Double): List<Int> {
        val repsList = mutableListOf<Int>()
        var currentWeight = startingWeight
        var currentReps = startingReps

        for (i in 0 until numSets) {
            val weightForSet = currentWeight * (1 + weightIncreasePercentage * i / 100)
            val repsForSet = (currentReps * (startingWeight / weightForSet)).toInt()
            repsList.add(repsForSet)
            currentWeight *= (1 + weightIncreasePercentage / 100)
        }

        return repsList
    }

    private fun storeDialog(timestamp: String, weightItems: List<WeightItem>) {
        val dialogInfo = DialogInfo("Basic Pyramid Calculator", timestamp, weightItems)
        dialogStorage.storeDialog(dialogInfo)
    }
}
