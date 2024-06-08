package com.example.bachelor_work.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.bachelor_work.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProfileDataHandler(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("ProfileData", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveProfileData(profileData: ProfileData) {
        val editor = sharedPreferences.edit()
        editor.putString("bodyWeight", profileData.bodyWeight)
        editor.putString("bodyFatPercentage", profileData.bodyFatPercentage)
        editor.putString("height", profileData.height)
        editor.putString("age", profileData.age)
        editor.putString("bloodPressure", profileData.bloodPressure)
        editor.putString("heartRate", profileData.heartRate)

        editor.putString("strengthMetrics", gson.toJson(profileData.strengthMetrics))
        editor.putString("healthMetrics", gson.toJson(profileData.healthMetrics))
        editor.putString("injuryMetrics", gson.toJson(profileData.injuryMetrics))
        editor.putString("allergiesMetrics", gson.toJson(profileData.allergiesMetrics))
        editor.putString("personalNotes", gson.toJson(profileData.personalNotes))

        editor.apply()
    }

    fun loadProfileData(): ProfileData {
        val bodyWeight = sharedPreferences.getString("bodyWeight", "") ?: ""
        val bodyFatPercentage = sharedPreferences.getString("bodyFatPercentage", "") ?: ""
        val height = sharedPreferences.getString("height", "") ?: ""
        val age = sharedPreferences.getString("age", "") ?: ""
        val bloodPressure = sharedPreferences.getString("bloodPressure", "") ?: ""
        val heartRate = sharedPreferences.getString("heartRate", "") ?: ""

        val strengthMetricsJson = sharedPreferences.getString("strengthMetrics", "[]")
        val healthMetricsJson = sharedPreferences.getString("healthMetrics", "[]")
        val injuryMetricsJson = sharedPreferences.getString("injuryMetrics", "[]")
        val allergiesMetricsJson = sharedPreferences.getString("allergiesMetrics", "[]")
        val personalNotesJson = sharedPreferences.getString("personalNotes", "[]")

        val strengthMetricsType = object : TypeToken<List<StrengthMetric>>() {}.type
        val healthMetricsType = object : TypeToken<List<HealthMetric>>() {}.type
        val injuryMetricsType = object : TypeToken<List<InjuryMetric>>() {}.type
        val allergiesMetricsType = object : TypeToken<List<AllergiesMetric>>() {}.type
        val personalNotesType = object : TypeToken<List<PersonalNote>>() {}.type

        val strengthMetrics: List<StrengthMetric> = gson.fromJson(strengthMetricsJson, strengthMetricsType)
        val healthMetrics: List<HealthMetric> = gson.fromJson(healthMetricsJson, healthMetricsType)
        val injuryMetrics: List<InjuryMetric> = gson.fromJson(injuryMetricsJson, injuryMetricsType)
        val allergiesMetrics: List<AllergiesMetric> = gson.fromJson(allergiesMetricsJson, allergiesMetricsType)
        val personalNotes: List<PersonalNote> = gson.fromJson(personalNotesJson, personalNotesType)

        return ProfileData(
            bodyWeight,
            bodyFatPercentage,
            height,
            age,
            bloodPressure,
            heartRate,
            strengthMetrics.toMutableList(),
            healthMetrics.toMutableList(),
            injuryMetrics.toMutableList(),
            allergiesMetrics.toMutableList(),
            personalNotes.toMutableList()
        )
    }
}