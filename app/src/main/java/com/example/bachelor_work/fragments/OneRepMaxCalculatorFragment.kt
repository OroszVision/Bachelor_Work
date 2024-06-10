package com.example.bachelor_work.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
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
import kotlin.math.pow

class OneRepMaxCalculatorFragment : Fragment() {

    private lateinit var weightEditText: EditText
    private lateinit var repetitionsNumberPicker: NumberPicker
    private lateinit var weightTableDialog: BottomSheetDialog
    private lateinit var weightRecyclerView: RecyclerView
    private lateinit var closeButton: Button
    private lateinit var exportButton: Button
    private lateinit var backgroundOverlay: View

    private lateinit var dialogStorage: DialogStorage

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
        exportButton = weightTableDialog.findViewById(R.id.exportButton)!!
        backgroundOverlay = view.findViewById(R.id.backgroundOverlay)

        dialogStorage = DialogStorage(requireContext())

        // Find the TextView for the title
        val dialogTitleTextView = weightTableDialog.findViewById<TextView>(R.id.dialogTitleTextView)
        // Set the title text
        dialogTitleTextView?.text = "One Rep Max Calculator"

        // Set onClickListener for the toggle table button
        view.findViewById<Button>(R.id.toggleTableButton).setOnClickListener {
            toggleWeightTableDialog()
        }

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

        exportButton?.setOnClickListener {
            val adapter = weightRecyclerView.adapter as WeightAdapter
            val fragmentName = "One Rep Max Calculator" // Change this to the name of your fragment
            val timestamp = System.currentTimeMillis().toString() // Use current timestamp as filename
            val fileName = "OneRepMax_$timestamp.pdf" // Construct the filename
            dialogStorage.exportDialogToPDF(requireContext(), adapter, fileName, fragmentName)
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
                val weightPercentage = calculateWeightForPercentage(oneRepMax, i)
                val repetitionsForPercentage = calculateRepetitionsForPercentage(i)
                weightItems.add(WeightItem(i, -1, repetitionsForPercentage, weightPercentage.round(2)))
            }

            val adapter = WeightAdapter(weightItems)
            weightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            weightRecyclerView.adapter = adapter

            // Store dialog with calculation result and timestamp
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val timestampString = dateFormat.format(Date())
            storeDialog(timestampString, weightItems)

        } else {
            // Handle invalid weight input
        }
    }


    private fun calculateRepetitionsForPercentage(percentage: Int): Int {
        return when (percentage) {
            100 -> 1
            95 -> 2
            90 -> 4
            85 -> 6
            80 -> 8
            75 -> 10
            70 -> 12
            65 -> 16
            60 -> 20
            55 -> 24
            50 -> 30
            else -> throw IllegalArgumentException("Invalid percentage")
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

    private fun storeDialog(timestamp: String, weightItems: List<WeightItem>) {
        // Create DialogInfo object with necessary data and store it using dialogStorage
        val dialogInfo = DialogInfo("One Rep Max Calculator", timestamp, weightItems)
        dialogStorage.storeDialog(dialogInfo)
    }


}
