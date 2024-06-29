package com.example.bachelor_work.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ChartTitle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setup()
    }

    private fun setup() {
        // Customize text appearance
        setTypeface(null, Typeface.BOLD)
    }
}
