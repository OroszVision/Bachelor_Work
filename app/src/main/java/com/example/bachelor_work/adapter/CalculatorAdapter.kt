package com.example.bachelor_work.adapter

import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.model.Calculator

class CalculatorAdapter(
    private val calculators: List<Calculator>,
    private val onItemClick: (Calculator) -> Unit
) :
    RecyclerView.Adapter<CalculatorAdapter.CalculatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculatorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CalculatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalculatorViewHolder, position: Int) {
        val calculator = calculators[position]
        holder.bind(calculator)
    }

    override fun getItemCount(): Int {
        return calculators.size
    }

    inner class CalculatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val calculatorName: TextView = itemView.findViewById(R.id.calculator_name)
        private val infoButton: ImageButton = itemView.findViewById(R.id.info_button)

        fun bind(calculator: Calculator) {
            calculatorName.text = calculator.name
            itemView.setOnClickListener {
                onItemClick(calculator)
            }

            infoButton.setOnClickListener {
                showPopup(itemView, calculator.description)
            }
        }

        private fun showPopup(itemView: View, description: String) {
            val context = itemView.context
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.popup_description)

            val descriptionTextView: TextView = dialog.findViewById(R.id.description_text)
            descriptionTextView.text = description

            val closeButton: Button = dialog.findViewById(R.id.close_button)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams

            dialog.show()
        }
    }
}
