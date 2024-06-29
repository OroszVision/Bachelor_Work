package com.example.bachelor_work.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.adapter.AllergiesAdapter
import com.example.bachelor_work.adapter.HealthMetricsAdapter
import com.example.bachelor_work.adapter.InjuryHistoryAdapter
import com.example.bachelor_work.adapter.PersonalNotesAdapter
import com.example.bachelor_work.adapter.StrengthMetricsAdapter
import com.example.bachelor_work.model.AllergiesMetric
import com.example.bachelor_work.model.HealthMetric
import com.example.bachelor_work.model.InjuryMetric
import com.example.bachelor_work.model.PersonalNote
import com.example.bachelor_work.model.ProfileData
import com.example.bachelor_work.model.StrengthMetric
import com.example.bachelor_work.model.StrengthMetricHistoryEntry
import com.example.bachelor_work.utils.ProfileDataHandler
import com.example.bachelor_work.view.ProfileViewModel
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment(),
    StrengthMetricsAdapter.EditableStrengthMetricProvider,
    HealthMetricsAdapter.EditableHealthMetricProvider,
    AllergiesAdapter.EditableAllergiesMetricProvider,
    PersonalNotesAdapter.EditablePersonalNoteProvider {

    private lateinit var strengthMetricsRecyclerView: RecyclerView
    private lateinit var strengthMetricsAdapter: StrengthMetricsAdapter

    private lateinit var healthMetricsRecyclerView: RecyclerView
    private lateinit var healthMetricsAdapter: HealthMetricsAdapter

    private lateinit var injuryMetricsRecyclerView: RecyclerView
    private lateinit var injuryMetricsAdapter: InjuryHistoryAdapter

    private lateinit var allergiesMetricsRecyclerView: RecyclerView
    private lateinit var allergiesMetricsAdapter: AllergiesAdapter

    private lateinit var personalNotesRecyclerView: RecyclerView
    private lateinit var personalNotesAdapter: PersonalNotesAdapter

    private lateinit var bodyWeightEditText: EditText
    private lateinit var bodyFatPercentageEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var bloodPressureEditText: EditText
    private lateinit var heartRateEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var exportButton: Button

    private lateinit var profileDataHandler: ProfileDataHandler
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileDataHandler = ProfileDataHandler(requireContext())

        // Load saved data
        val savedData = profileDataHandler.loadProfileData()

        // Find views by ID
        bodyWeightEditText = view.findViewById(R.id.bodyWeightEditText)
        bodyFatPercentageEditText = view.findViewById(R.id.bodyFatPercentageEditText)
        heightEditText = view.findViewById(R.id.heightEditText)
        ageEditText = view.findViewById(R.id.ageEditText)
        bloodPressureEditText = view.findViewById(R.id.bloodPressureEditText)
        heartRateEditText = view.findViewById(R.id.heartRateEditText)
        saveButton = view.findViewById(R.id.saveButton)
        exportButton = view.findViewById(R.id.exportButton)

        // Set saved data to EditText fields
        bodyWeightEditText.setText(savedData.bodyWeight)
        bodyFatPercentageEditText.setText(savedData.bodyFatPercentage)
        heightEditText.setText(savedData.height)
        ageEditText.setText(savedData.age)
        bloodPressureEditText.setText(savedData.bloodPressure)
        heartRateEditText.setText(savedData.heartRate)

        // Initialize strength metrics RecyclerView
        strengthMetricsRecyclerView = view.findViewById(R.id.strengthMetricsRecyclerView)
        strengthMetricsRecyclerView.layoutManager = LinearLayoutManager(context)
        strengthMetricsAdapter = StrengthMetricsAdapter(savedData.strengthMetrics.toMutableList())
        strengthMetricsAdapter.setEditableStrengthMetricProvider(this)
        strengthMetricsRecyclerView.adapter = strengthMetricsAdapter

        // Initialize health metrics RecyclerView
        healthMetricsRecyclerView = view.findViewById(R.id.healthMetricsRecyclerView)
        healthMetricsRecyclerView.layoutManager = LinearLayoutManager(context)
        healthMetricsAdapter = HealthMetricsAdapter(savedData.healthMetrics.toMutableList())
        healthMetricsAdapter.setEditableHealthMetricProvider(this)
        healthMetricsRecyclerView.adapter = healthMetricsAdapter

        // Initialize injury metrics RecyclerView
        injuryMetricsRecyclerView = view.findViewById(R.id.injuryHistoryRecyclerView)
        injuryMetricsRecyclerView.layoutManager = LinearLayoutManager(context)
        injuryMetricsAdapter = InjuryHistoryAdapter(savedData.injuryMetrics.toMutableList())
        injuryMetricsRecyclerView.adapter = injuryMetricsAdapter

        // Initialize allergies metrics RecyclerView
        allergiesMetricsRecyclerView = view.findViewById(R.id.allergiesRecyclerView)
        allergiesMetricsRecyclerView.layoutManager = LinearLayoutManager(context)
        allergiesMetricsAdapter = AllergiesAdapter(savedData.allergiesMetrics.toMutableList())
        allergiesMetricsAdapter.setEditableAllergiesMetricProvider(this)
        allergiesMetricsRecyclerView.adapter = allergiesMetricsAdapter

        // Initialize personal notes RecyclerView
        personalNotesRecyclerView = view.findViewById(R.id.personalNotesRecyclerView)
        personalNotesRecyclerView.layoutManager = LinearLayoutManager(context)
        personalNotesAdapter = PersonalNotesAdapter(savedData.personalNotes.toMutableList())
        personalNotesRecyclerView.adapter = personalNotesAdapter

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        // Set click listener for "Add Allergies Metric" button
        view.findViewById<Button>(R.id.addAllergyButton).setOnClickListener {
            showAddAllergiesMetricDialog()
        }

        // Set click listener for "Add Personal Note" button
        view.findViewById<Button>(R.id.addPersonalNoteButton).setOnClickListener {
            showAddPersonalNoteDialog()
        }

        // Set click listener for "Add Injury Metric" button
        view.findViewById<Button>(R.id.addInjuryButton).setOnClickListener {
            showAddInjuryMetricDialog()
        }

        // Set click listener for "Add Strength Metric" button
        view.findViewById<Button>(R.id.addStrengthMetricButton).setOnClickListener {
            showAddStrengthMetricDialog()
        }

        // Set click listener for "Add Health Metric" button
        view.findViewById<Button>(R.id.addHealthMetricButton).setOnClickListener {
            showAddHealthMetricDialog()
        }

        // Set item click listener for strength metrics
        strengthMetricsAdapter.setOnItemClickListener(object : StrengthMetricsAdapter.OnItemClickListener {
            override fun onItemClick(strengthMetric: StrengthMetric) {
                showEditStrengthMetricDialog(strengthMetric)
            }
        })

        // Set item click listener for allergies metrics
        allergiesMetricsAdapter.setOnItemClickListener(object : AllergiesAdapter.OnItemClickListener {
            override fun onItemClick(allergiesMetric: AllergiesMetric) {
                showEditAllergiesMetricDialog(allergiesMetric)
            }
        })

        // Set item click listener for injury metrics
        injuryMetricsAdapter.setOnItemClickListener(object : InjuryHistoryAdapter.OnItemClickListener {
            override fun onItemClick(injuryMetric: InjuryMetric) {
                showEditInjuryMetricDialog(injuryMetric)
            }
        })

        // Set item click listener for health metrics
        healthMetricsAdapter.setOnItemClickListener(object : HealthMetricsAdapter.OnItemClickListener {
            override fun onItemClick(healthMetric: HealthMetric) {
                showEditHealthMetricDialog(healthMetric)
            }
        })

        // Set item click listener for personal notes
        personalNotesAdapter.setOnItemClickListener(object : PersonalNotesAdapter.OnItemClickListener {
            override fun onItemClick(personalNote: PersonalNote) {
                showEditPersonalNoteDialog(personalNote)
            }
        })

        // Set click listener for save button to save all data
        saveButton.setOnClickListener {
            saveAllData()
        }

        // Set click listener for export button
        val exportButton = view.findViewById<MaterialButton>(R.id.exportButton)
        exportButton.setOnClickListener {
            exportProfileToPDF()
        }

        val strengthMetrics = strengthMetricsAdapter.getStrengthMetrics()
        profileViewModel.setStrengthMetrics(strengthMetrics)


        return view
    }

    private fun showAddStrengthMetricDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_strength_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val exerciseNameEditText = dialog.findViewById<EditText>(R.id.exerciseNameEditText)
        val maxLiftEditText = dialog.findViewById<EditText>(R.id.maxLiftEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val name = exerciseNameEditText.text.toString()
            val value = maxLiftEditText.text.toString().toDoubleOrNull()

            // Validate entered data
            if (name.isNotEmpty() && value != null) {
                val newStrengthMetric = StrengthMetric(name, value)
                strengthMetricsAdapter.addStrengthMetric(newStrengthMetric)

                // Add history entry
                val historyEntry = StrengthMetricHistoryEntry(getCurrentDateString(), value)
                strengthMetricsAdapter.addHistoryEntry(newStrengthMetric, historyEntry)

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

    private fun getCurrentDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // Show dialog to add a new injury metric
    private fun showAddInjuryMetricDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_other_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val detailsEditText = dialog.findViewById<EditText>(R.id.metricNameEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val details = detailsEditText.text.toString()

            // Validate entered data
            if (details.isNotEmpty()) {
                // Add the new injury metric to the RecyclerView
                val newInjuryMetric = InjuryMetric(details)
                injuryMetricsAdapter.addInjuryMetric(newInjuryMetric)
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

    // Show dialog to add a new personal note
    private fun showAddPersonalNoteDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_personal_note)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val titleEditText = dialog.findViewById<EditText>(R.id.titleEditText)
        val contentEditText = dialog.findViewById<EditText>(R.id.contentEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newPersonalNote = PersonalNote(title, content)
                personalNotesAdapter.addPersonalNote(newPersonalNote)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter title and content", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Show dialog to add a new health metric
    private fun showAddHealthMetricDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_health_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val healthMetricNameEditText = dialog.findViewById<EditText>(R.id.healthMetricNameEditText)
        val valueEditText = dialog.findViewById<EditText>(R.id.valueEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Set the visibility of Save and Cancel buttons
        saveButton.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val name = healthMetricNameEditText.text.toString()
            val value = valueEditText.text.toString().toDoubleOrNull()

            // Validate entered data
            if (name.isNotEmpty() && value != null) {
                // Add the new health metric to the RecyclerView
                val newHealthMetric = HealthMetric(name, value)
                healthMetricsAdapter.addHealthMetric(newHealthMetric)
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

    // Show dialog to add a new allergies metric
    private fun showAddAllergiesMetricDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_other_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val detailsEditText = dialog.findViewById<EditText>(R.id.metricNameEditText)
            val details = detailsEditText.text.toString()

            // Validate entered data
            if (details.isNotEmpty()) {
                val newAllergiesMetric = AllergiesMetric(details)
                allergiesMetricsAdapter.addAllergiesMetric(newAllergiesMetric)
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

    // Show dialog to edit an existing allergies metric
    override fun showEditAllergiesMetricDialog(allergiesMetric: AllergiesMetric) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_other_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val detailsEditText = dialog.findViewById<EditText>(R.id.metricNameEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Set existing data to EditText fields
        detailsEditText.setText(allergiesMetric.type)

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val details = detailsEditText.text.toString()

            // Validate entered data
            if (details.isNotEmpty()) {
                // Update the allergies metric data in the RecyclerView
                val updatedAllergiesMetric = AllergiesMetric(details)
                allergiesMetricsAdapter.updateAllergiesMetric(allergiesMetric, updatedAllergiesMetric)
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

    // Inside ProfileFragment class
    override fun showEditPersonalNoteDialog(personalNote: PersonalNote) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_personal_note)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val titleEditText = dialog.findViewById<EditText>(R.id.titleEditText)
        val contentEditText = dialog.findViewById<EditText>(R.id.contentEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Set existing data to EditText fields
        titleEditText.setText(personalNote.title)
        contentEditText.setText(personalNote.content)

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val updatedPersonalNote = PersonalNote(title, content)
                personalNotesAdapter.updatePersonalNote(personalNote, updatedPersonalNote)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter title and content", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // Show dialog to edit an existing strength metric
    override fun showEditStrengthMetricDialog(strengthMetric: StrengthMetric) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_strength_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val exerciseNameEditText = dialog.findViewById<EditText>(R.id.exerciseNameEditText)
        val maxLiftEditText = dialog.findViewById<EditText>(R.id.maxLiftEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

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

    // Show dialog to edit an existing health metric
    override fun showEditHealthMetricDialog(healthMetric: HealthMetric) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_health_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val healthMetricNameEditText = dialog.findViewById<EditText>(R.id.healthMetricNameEditText)
        val valueEditText = dialog.findViewById<EditText>(R.id.valueEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Set existing data to EditText fields
        healthMetricNameEditText.setText(healthMetric.metricName)
        valueEditText.setText(healthMetric.value.toString())

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val name = healthMetricNameEditText.text.toString()
            val value = valueEditText.text.toString().toDoubleOrNull()

            // Validate entered data
            if (name.isNotEmpty() && value != null) {
                // Update the health metric data in the RecyclerView
                val updatedHealthMetric = HealthMetric(name, value)
                healthMetricsAdapter.updateHealthMetric(healthMetric, updatedHealthMetric)
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

    // Show dialog to edit an existing injury metric
    fun showEditInjuryMetricDialog(injuryMetric: InjuryMetric) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_other_metric)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)
        val cancelButton = dialog.findViewById<Button>(R.id.cancelButton)
        val detailsEditText = dialog.findViewById<EditText>(R.id.metricNameEditText)

        // Set dialog window dimensions
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Set existing data to EditText fields
        detailsEditText.setText(injuryMetric.details)

        saveButton.setOnClickListener {
            // Retrieve entered data from EditText fields
            val details = detailsEditText.text.toString()

            // Validate entered data
            if (details.isNotEmpty()) {
                // Update the injury metric data in the RecyclerView
                val updatedInjuryMetric = InjuryMetric(details)
                injuryMetricsAdapter.updateInjuryMetric(injuryMetric, updatedInjuryMetric)
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

    // Function to save all data
    private fun saveAllData() {
        val profileData = ProfileData(
            bodyWeight = bodyWeightEditText.text.toString(),
            bodyFatPercentage = bodyFatPercentageEditText.text.toString(),
            height = heightEditText.text.toString(),
            age = ageEditText.text.toString(),
            bloodPressure = bloodPressureEditText.text.toString(),
            heartRate = heartRateEditText.text.toString(),
            strengthMetrics = strengthMetricsAdapter.getStrengthMetrics(),
            healthMetrics = healthMetricsAdapter.getHealthMetrics(),
            injuryMetrics = injuryMetricsAdapter.getInjuryMetrics(),
            allergiesMetrics = allergiesMetricsAdapter.getAllergiesMetrics(),
            personalNotes = personalNotesAdapter.getPersonalNotes()
        )

        profileDataHandler.saveProfileData(profileData)
        Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
    }

    private fun exportProfileToPDF() {
        // Load profile data from SharedPreferences or wherever it is stored
        val profileData = profileDataHandler.loadProfileData()

        // Generate PDF file name
        val fileName = "profile_data.pdf"

        // Export profile data to PDF
        profileDataHandler.exportProfileToPDF(profileData, fileName)

        // Optionally, show a message indicating successful export
        // Toast.makeText(requireContext(), "PDF Exported successfully", Toast.LENGTH_SHORT).show()
    }
}