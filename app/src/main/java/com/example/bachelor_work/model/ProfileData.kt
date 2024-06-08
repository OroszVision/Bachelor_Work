package com.example.bachelor_work.model

data class ProfileData(
    val bodyWeight: String,
    val bodyFatPercentage: String,
    val height: String,
    val age: String,
    val bloodPressure: String,
    val heartRate: String,
    val strengthMetrics: MutableList<StrengthMetric>,
    val healthMetrics: MutableList<HealthMetric>,
    val injuryMetrics: MutableList<InjuryMetric>,
    val allergiesMetrics: MutableList<AllergiesMetric>,
    val personalNotes: MutableList<PersonalNote>
)