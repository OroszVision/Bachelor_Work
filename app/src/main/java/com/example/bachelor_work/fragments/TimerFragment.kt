package com.example.bachelor_work.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bachelor_work.R

class TimerFragment : Fragment() {

    private lateinit var timerTextView: TextView
    private lateinit var millisecondsTextView: TextView
    private lateinit var startButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var resetButton: ImageButton

    private var running: Boolean = false
    private var milliseconds: Int = 0
    private var seconds: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        timerTextView = view.findViewById(R.id.timerTextView)
        millisecondsTextView = view.findViewById(R.id.millisecondsTextView)
        startButton = view.findViewById(R.id.startButton)
        pauseButton = view.findViewById(R.id.pauseButton)
        resetButton = view.findViewById(R.id.resetButton)

        startButton.setOnClickListener { startTimer() }
        pauseButton.setOnClickListener { pauseTimer() }
        resetButton.setOnClickListener { resetTimer() }

        updateTimerText()

        return view
    }

    private fun startTimer() {
        running = true
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        resetButton.visibility = View.VISIBLE
        runnable = object : Runnable {
            override fun run() {
                if (running) {
                    milliseconds++
                    if (milliseconds == 100) {
                        seconds++
                        milliseconds = 0
                    }
                    updateTimerText()
                    handler.postDelayed(this, 10)
                }
            }
        }
        handler.postDelayed(runnable, 10)
    }

    private fun pauseTimer() {
        running = false
        pauseButton.visibility = View.GONE
        startButton.visibility = View.VISIBLE
    }

    private fun resetTimer() {
        running = false
        seconds = 0
        milliseconds = 0
        updateTimerText()
        startButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    private fun updateTimerText() {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        val timeString = String.format("%02d:%02d:%02d", hours, minutes, secs)
        timerTextView.text = timeString
        millisecondsTextView.text = String.format(".%02d", milliseconds)
    }
}
