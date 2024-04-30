    package com.example.bachelor_work.fragments

    import com.example.bachelor_work.adapter.StrengthMetricsAdapter
    import android.app.Dialog
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import androidx.fragment.app.Fragment
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.bachelor_work.R
    import com.example.bachelor_work.model.StrengthMetric

    class ProfileFragment : Fragment(), StrengthMetricsAdapter.EditableStrengthMetricProvider {
        private lateinit var strengthMetricsRecyclerView: RecyclerView
        private lateinit var strengthMetricsAdapter: StrengthMetricsAdapter

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_profile, container, false)

            // Initialize RecyclerView
            strengthMetricsRecyclerView = view.findViewById(R.id.strengthMetricsRecyclerView)
            strengthMetricsRecyclerView.layoutManager = LinearLayoutManager(context)

            // Initialize adapter with sample data
            strengthMetricsAdapter = StrengthMetricsAdapter(getSampleStrengthMetrics())
            strengthMetricsAdapter.setEditableStrengthMetricProvider(this)
            strengthMetricsRecyclerView.adapter = strengthMetricsAdapter

            // Set click listener for "Add Strength Metric" button
            view.findViewById<Button>(R.id.addStrengthMetricButton).setOnClickListener {
                showAddStrengthMetricDialog()
            }

            strengthMetricsAdapter.setOnItemClickListener(object : StrengthMetricsAdapter.OnItemClickListener {
                override fun onItemClick(strengthMetric: StrengthMetric) {
                    showEditStrengthMetricDialog(strengthMetric)
                }
            })


            return view
        }

        // Function to provide sample data for strength metrics
        private fun getSampleStrengthMetrics(): MutableList<StrengthMetric> {
            val strengthMetricsList = mutableListOf<StrengthMetric>()
            strengthMetricsList.add(StrengthMetric("Bench Press", 100.0))
            strengthMetricsList.add(StrengthMetric("Squat", 120.0))
            // Add more sample strength metrics as needed
            return strengthMetricsList
        }

        private fun showAddStrengthMetricDialog() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_add_strength_metric)
            val saveButton = dialog.findViewById<Button>(R.id.saveButton)
            val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

            saveButton.setOnClickListener {
                // Retrieve entered data from EditText fields
                val exerciseNameEditText = dialog.findViewById<EditText>(R.id.exerciseNameEditText)
                val maxLiftEditText = dialog.findViewById<EditText>(R.id.maxLiftEditText)
                val name = exerciseNameEditText.text.toString()
                val value = maxLiftEditText.text.toString().toDoubleOrNull()

                // Validate entered data
                if (name.isNotEmpty() && value != null) {
                    // Add the new strength metric to the RecyclerView
                    val newStrengthMetric = StrengthMetric(name, value)
                    strengthMetricsAdapter.addStrengthMetric(newStrengthMetric)
                    dialog.dismiss()
                } else {
                    // Show error message if data is invalid
                    Toast.makeText(requireContext(), "Invalid input. Please enter valid data.", Toast.LENGTH_SHORT).show()
                }
            }

            cancelButton.setOnClickListener {
                // Dismiss the dialog if Cancel button is clicked
                dialog.dismiss()
            }

            dialog.show()
        }

        override fun showEditStrengthMetricDialog(strengthMetric: StrengthMetric) {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_add_strength_metric)
            val saveButton = dialog.findViewById<Button>(R.id.saveButton)
            val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
            val exerciseNameEditText = dialog.findViewById<EditText>(R.id.exerciseNameEditText)
            val maxLiftEditText = dialog.findViewById<EditText>(R.id.maxLiftEditText)

            // Set existing data to EditText fields
            exerciseNameEditText.setText(strengthMetric.exerciseName)
            maxLiftEditText.setText(strengthMetric.maxLift.toString())

            saveButton.setOnClickListener {
                // Retrieve entered data from EditText fields
                val name = exerciseNameEditText.text.toString()
                val value = maxLiftEditText.text.toString().toDoubleOrNull()

                // Validate entered data
                if (name.isNotEmpty() && value != null) {
                    // Update the strength metric data in the RecyclerView
                    val updatedStrengthMetric = StrengthMetric(name, value)
                    strengthMetricsAdapter.updateStrengthMetric(strengthMetric, updatedStrengthMetric)
                    dialog.dismiss()
                } else {
                    // Show error message if data is invalid
                    Toast.makeText(requireContext(), "Invalid input. Please enter valid data.", Toast.LENGTH_SHORT).show()
                }
            }

            cancelButton.setOnClickListener {
                // Dismiss the dialog if Cancel button is clicked
                dialog.dismiss()
            }

            dialog.show()
        }


    }
