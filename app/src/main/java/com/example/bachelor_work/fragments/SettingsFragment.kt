package com.example.bachelor_work.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.bachelor_work.R
import java.util.*

//TODO: CALCULATORS DOESN'T WORK AFTER LOCALIZATION CHANGE TO CZECH
class SettingsFragment : Fragment() {

    private lateinit var languageSpinner: Spinner
    private lateinit var applyButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        languageSpinner = view.findViewById(R.id.language_spinner)
        applyButton = view.findViewById(R.id.apply_button)
        sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val languages = resources.getStringArray(R.array.language_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        val savedLanguageCode = sharedPreferences.getString("LANGUAGE_CODE", "en")
        val languageCodes = resources.getStringArray(R.array.language_codes)
        val selectedIndex = languageCodes.indexOf(savedLanguageCode)
        languageSpinner.setSelection(selectedIndex)

        applyButton.setOnClickListener {
            val selectedLanguage = languageCodes[languageSpinner.selectedItemPosition]
            setLocale(selectedLanguage)
        }

        return view
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = requireContext().resources.configuration
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)

        with(sharedPreferences.edit()) {
            putString("LANGUAGE_CODE", languageCode)
            apply()
        }

        // Restart activity to apply the new language
        requireActivity().recreate()
    }
}
