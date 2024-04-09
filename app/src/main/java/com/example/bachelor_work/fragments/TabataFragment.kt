package com.example.bachelor_work.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bachelor_work.R
import com.example.bachelor_work.utils.ToolbarHelper

class TabataFragment : Fragment() {

    private lateinit var setsPicker: NumberPicker
    private lateinit var workoutTimePicker: NumberPicker
    private lateinit var restTimePicker: NumberPicker
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var setsRemainingTextView: TextView
    private lateinit var setsTextView: TextView
    private lateinit var workoutTimeLabel : TextView
    private lateinit var restTimeTextView: TextView

    private var timer: CountDownTimer? = null
    private var preparationTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var isPreparationTimeRunning = false
    private var timeLeftInMillis: Long = 0
    private var setsRemaining: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tabata, container, false)

        setsPicker = view.findViewById(R.id.setsPicker)
        workoutTimePicker = view.findViewById(R.id.workoutTimePicker)
        restTimePicker = view.findViewById(R.id.restTimePicker)
        startButton = view.findViewById(R.id.startButton)
        pauseButton = view.findViewById(R.id.pauseButton)
        stopButton = view.findViewById(R.id.stopButton)
        timerTextView = view.findViewById(R.id.timerTextView)
        setsRemainingTextView = view.findViewById(R.id.setsRemainingTextView)
        setsTextView = view.findViewById(R.id.setsTextView)
        workoutTimeLabel = view.findViewById(R.id.workoutTimeLabel)
        restTimeTextView = view.findViewById(R.id.restTimeLabel)

        setsPicker.minValue = 1
        setsPicker.maxValue = 50

        workoutTimePicker.minValue = 10
        workoutTimePicker.maxValue = 60

        restTimePicker.minValue = 10
        restTimePicker.maxValue = 60

        setsRemaining = setsPicker.value // Initialize setsRemaining here

        startButton.setOnClickListener {
            if (!isTimerRunning) {
                startPreparationTimer()
            } else {
                pauseTabata()
            }
        }

        super.onViewCreated(view, savedInstanceState)
        ToolbarHelper.setupToolbar(this, view)

        stopButton.setOnClickListener {
            stopTabata()
        }

        return view
    }

    private fun startPreparationTimer() {
        isPreparationTimeRunning = true
        startButton.isEnabled = false
        setsPicker.isEnabled = false
        workoutTimePicker.isEnabled = false
        restTimePicker.isEnabled = false

        preparationTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI to show the preparation time countdown
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                timerTextView.text = secondsLeft.toString()
            }

            override fun onFinish() {
                // Start the Tabata timer after preparation time is over
                startTabata()
            }
        }.start()
    }

    private fun startTabata() {
        preparationTimer?.cancel()
        isPreparationTimeRunning = false

        setsPicker.visibility = View.GONE
        workoutTimePicker.visibility = View.GONE
        restTimePicker.visibility = View.GONE

        setsTextView.visibility = View.GONE
        workoutTimeLabel.visibility = View.GONE
        restTimeTextView.visibility = View.GONE

        val workoutTimeSeconds = workoutTimePicker.value * 1000L
        val restTimeSeconds = restTimePicker.value * 1000L
        val totalSets = setsPicker.value

        setsRemainingTextView.text = "Sets remaining: $setsRemaining"
        setsRemainingTextView.visibility = View.VISIBLE

        startButton.isEnabled = true
        startButton.text = "Pause"
        pauseButton.visibility = View.VISIBLE
        stopButton.visibility = View.VISIBLE

        timer = object : CountDownTimer(workoutTimeSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountdownText()
            }

            override fun onFinish() {
                setsRemaining--
                setsRemainingTextView.text = "Sets remaining: $setsRemaining"
                if (setsRemaining > 0) {
                    rest()
                } else {
                    resetTabata()
                }
            }
        }.start()

        isTimerRunning = true
    }

    private fun pauseTabata() {
        timer?.cancel()
        isTimerRunning = false
        startButton.text = "Resume"
    }

    private fun stopTabata() {
        preparationTimer?.cancel()
        timer?.cancel()
        resetTabata()
    }

    private fun resetTabata() {
        isTimerRunning = false
        setsPicker.isEnabled = true
        workoutTimePicker.isEnabled = true
        restTimePicker.isEnabled = true
        setsPicker.visibility = View.VISIBLE
        workoutTimePicker.visibility = View.VISIBLE
        restTimePicker.visibility = View.VISIBLE
        setsTextView.visibility = View.VISIBLE
        workoutTimeLabel.visibility = View.VISIBLE
        restTimeTextView.visibility = View.VISIBLE
        startButton.isEnabled = true
        startButton.text = "Start"
        pauseButton.visibility = View.GONE
        stopButton.visibility = View.GONE
        timeLeftInMillis = 0
        timerTextView.text = "00:00"
        setsRemaining = setsPicker.value // Reset setsRemaining to default value
        setsRemainingTextView.text = ""
        setsRemainingTextView.visibility = View.GONE
    }

    private fun rest() {
        timer = object : CountDownTimer(restTimePicker.value * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountdownText()
            }

            override fun onFinish() {
                if (setsRemaining > 0) {
                    startTabata()
                }
            }
        }.start()
    }

    private fun updateCountdownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeLeftFormatted
    }
}
