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
import com.example.bachelor_work.model.WeightItem
import com.example.bachelor_work.utils.ToolbarHelper
import com.example.bachelor_work.utils.ToolbarHelper.Companion.round
import com.google.android.material.bottomsheet.BottomSheetDialog

class DropSetCalculatorFragment : Fragment() {

    private lateinit var startingWeightEditText: EditText
    private lateinit var percentageDecreasePicker: NumberPicker
    private lateinit var numSetsPicker: NumberPicker
    private lateinit var calculateButton: Button
    private lateinit var weightTableDialog: BottomSheetDialog
    private lateinit var weightRecyclerView: RecyclerView
    private lateinit var closeButton: Button
    private lateinit var backgroundOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drop_set_calculator, container, false)

        startingWeightEditText = view.findViewById(R.id.starting_weight_edittext)
        percentageDecreasePicker = view.findViewById(R.id.percentage_decrease_picker)
        numSetsPicker = view.findViewById(R.id.num_sets_picker)
        calculateButton = view.findViewById(R.id.calculate_button)
        backgroundOverlay = view.findViewById(R.id.backgroundOverlay)

        // Initialize the dialog and its components
        val dialogView = inflater.inflate(R.layout.dialog_weight_table, container, false)
        weightTableDialog = BottomSheetDialog(requireContext())
        weightTableDialog.setContentView(dialogView)
        weightRecyclerView = dialogView.findViewById(R.id.weightRecyclerView)!!
        closeButton = dialogView.findViewById(R.id.closeButton)

        setupPercentageDecreasePicker()
        setupNumSetsPicker()

        super.onViewCreated(view, savedInstanceState)
        ToolbarHelper.setupToolbar(this, view)

        calculateButton.setOnClickListener {
            calculateDropSetWeights()
            toggleWeightTableDialog()
        }

        closeButton.setOnClickListener {
            toggleWeightTableDialog()
        }

        return view
    }


    private fun setupPercentageDecreasePicker() {
        percentageDecreasePicker.minValue = 1
        percentageDecreasePicker.maxValue = 50
        percentageDecreasePicker.value = 10
    }

    private fun setupNumSetsPicker() {
        numSetsPicker.minValue = 1
        numSetsPicker.maxValue = 20
        numSetsPicker.value = 3
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

    private fun calculateDropSetWeights() {
        val startingWeight = startingWeightEditText.text.toString().toDoubleOrNull()
        val percentageDecrease = percentageDecreasePicker.value
        val numSets = numSetsPicker.value

        if (startingWeight != null) {
            val weightList = mutableListOf<WeightItem>()
            var currentWeight = startingWeight
            for (i in 1..numSets) {
                weightList.add(WeightItem(-1,i,-1,currentWeight.round(2)))
                currentWeight *= (1 - percentageDecrease / 100.0)
            }
            populateRecyclerView(weightList)
        }
    }

    private fun populateRecyclerView(weightList: List<WeightItem>) {
        val adapter = WeightAdapter(weightList)
        weightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        weightRecyclerView.adapter = adapter
    }


}
