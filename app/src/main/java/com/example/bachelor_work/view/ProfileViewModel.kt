package com.example.bachelor_work.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bachelor_work.model.StrengthMetric

class ProfileViewModel : ViewModel() {
    private val _strengthMetrics = MutableLiveData<List<StrengthMetric>>()
    val strengthMetrics: LiveData<List<StrengthMetric>> = _strengthMetrics

    fun setStrengthMetrics(metrics: List<StrengthMetric>) {
        _strengthMetrics.value = metrics
    }
}
