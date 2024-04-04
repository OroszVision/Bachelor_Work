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
import kotlin.math.pow

class OneRepMaxCalculatorFragment : Fragment() {

    private lateinit var weightEditText: EditText
    private lateinit var repetitionsNumberPicker: NumberPicker
    private lateinit var weightTableDialog: BottomSheetDialog
    private lateinit var weightRecyclerView: RecyclerView
    private lateinit var closeButton: Button
    private lateinit var backgroundOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one_rep_max_calculator, container, false)
        weightEditText = view.findViewById(R.id.weightEditText)
        repetitionsNumberPicker = view.findViewById(R.id.repetitionsNumberPicker)
        weightTableDialog = BottomSheetDialog(requireContext())
        weightTableDialog.setContentView(R.layout.dialog_weight_table)
        weightRecyclerView = weightTableDialog.findViewById(R.id.weightRecyclerView)!!
        closeButton = weightTableDialog.findViewById(R.id.closeButton)!!
        backgroundOverlay = view.findViewById(R.id.backgroundOverlay)

        // Set onClickListener for the toggle table button
        view.findViewById<Button>(R.id.toggleTableButton).setOnClickListener {
            toggleWeightTableDialog()
        }
        super.onViewCreated(view, savedInstanceState)
        ToolbarHelper.setupToolbar(this, view)

        // Set onClickListener for the close button in weight table dialog
        closeButton.setOnClickListener {
            toggleWeightTableDialog()
        }

        // Initialize NumberPicker
        repetitionsNumberPicker.minValue = 1
        repetitionsNumberPicker.maxValue = 20

        // Setup onClickListeners for calculation buttons
        view.findViewById<Button>(R.id.epleyButton).setOnClickListener {
            calculateOneRepMax("Epley")
            toggleWeightTableDialog()
        }

        view.findViewById<Button>(R.id.brzyckiButton).setOnClickListener {
            calculateOneRepMax("Brzycki")
            toggleWeightTableDialog()
        }

        view.findViewById<Button>(R.id.lombardiButton).setOnClickListener {
            calculateOneRepMax("Lombardi")
            toggleWeightTableDialog()
        }

        return view
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

    private fun calculateOneRepMax(method: String) {
        val weightText = weightEditText.text.toString()
        val weight = weightText.toDoubleOrNull()
        val repetitions = repetitionsNumberPicker.value

        if (weight != null) {
            val oneRepMax = when (method) {
                "Epley" -> calculateEpleyOneRepMax(weight, repetitions)
                "Brzycki" -> calculateBrzyckiOneRepMax(weight, repetitions)
                "Lombardi" -> calculateLombardiOneRepMax(weight, repetitions)
                else -> throw IllegalArgumentException("Invalid method")
            }



            // After calculation, populate the RecyclerView with data
            val weightItems = mutableListOf<WeightItem>()
            for (i in 100 downTo 50 step 5) {
                val weight = calculateWeightForPercentage(oneRepMax, i)
                weightItems.add(WeightItem(i, -1, -1, weight.round(2)))
            }

            val adapter = WeightAdapter(weightItems)
            weightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            weightRecyclerView.adapter = adapter
        } else {
            // Handle invalid weight input
        }
    }

    private fun calculateWeightForPercentage(oneRepMax: Double, percentage: Int): Double {
        return oneRepMax * (percentage / 100.0)
    }

    private fun calculateEpleyOneRepMax(weight: Double, repetitions: Int): Double {
        return weight * (1 + repetitions / 30.0)
    }

    private fun calculateBrzyckiOneRepMax(weight: Double, repetitions: Int): Double {
        return weight * (36.0 / (37.0 - repetitions))
    }

    private fun calculateLombardiOneRepMax(weight: Double, repetitions: Int): Double {
        return weight * repetitions.toDouble().pow(0.10)
    }
}
