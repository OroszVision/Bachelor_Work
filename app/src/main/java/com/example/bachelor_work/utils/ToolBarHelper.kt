package com.example.bachelor_work.utils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bachelor_work.R
import kotlin.math.round

class ToolbarHelper {

    companion object {
        fun setupToolbar(fragment: Fragment, view: View) {
            val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
            (fragment.requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24) // Set your own icon for the back arrow
            toolbar.setNavigationOnClickListener {
                fragment.findNavController().navigateUp()
            }
        }

        fun Double.round(decimals: Int): Double {
                var multiplier = 1.0
                repeat(decimals) { multiplier *= 10 }
                return round(this * multiplier) / multiplier
            }

    }
}

