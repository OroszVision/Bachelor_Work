package com.example.bachelor_work.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.adapter.CalculatorAdapter
import com.example.bachelor_work.model.Calculator

class HomeFragment : Fragment() {

    private lateinit var calculators: List<Calculator>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve calculator names and descriptions from resources
        val calculatorNames = resources.getStringArray(R.array.calculator_names)
        val calculatorDescriptions = resources.getStringArray(R.array.calculator_descriptions)

        // Combine names and descriptions into a list of Calculator objects
        calculators = calculatorNames.zip(calculatorDescriptions) { name, description ->
            Calculator(name, description)
        }

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = CalculatorAdapter(calculators) { calculator ->
            // Handle item click by navigating to the appropriate destination
            when (calculator.name) {
                calculatorNames[0] -> { // One Rep Max Calculator
                    findNavController().navigate(R.id.action_homeFragment_to_oneRepMaxCalculatorFragment)
                }
                calculatorNames[1] -> { // Drop-set Calculator
                    findNavController().navigate(R.id.action_homeFragment_to_dropSetCalculatorFragment)
                }
                calculatorNames[3] -> { // Reverse Pyramid Calculator
                    findNavController().navigate(R.id.action_homeFragment_to_reversePyramidCalculatorFragment)
                }
                calculatorNames[4] -> { // Pyramid Calculator
                    findNavController().navigate(R.id.action_homeFragment_to_basicPyramidCalculatorFragment)
                }
                calculatorNames[6] -> { // Tabata
                    findNavController().navigate(R.id.action_homeFragment_to_tabataFragment)
                }
                // Add more cases for each calculator card
            }
        }
    }
}
