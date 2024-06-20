package com.example.bachelor_work.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import com.example.bachelor_work.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.io.FileOutputStream

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

    fun exportProfileToPDF(profileData: ProfileData, fileName: String) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(downloadDir, fileName)
        val outputStream = FileOutputStream(file)
        val writer = PdfWriter(outputStream)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        try {
            // Add document title
            val title = Paragraph("Profile Data")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20f)
                .setBold()
                .setMarginBottom(20f)
            document.add(title)

            // Add profile data details
            document.add(Paragraph("Body Weight: ${profileData.bodyWeight}"))
            document.add(Paragraph("Body Fat Percentage: ${profileData.bodyFatPercentage}"))
            document.add(Paragraph("Height: ${profileData.height}"))
            document.add(Paragraph("Age: ${profileData.age}"))
            document.add(Paragraph("Blood Pressure: ${profileData.bloodPressure}"))
            document.add(Paragraph("Heart Rate: ${profileData.heartRate}"))
            document.add(Paragraph("")) // Add empty line for spacing

            // Add strength metrics
            document.add(Paragraph("Strength Metrics:"))
            profileData.strengthMetrics.forEach {
                document.add(Paragraph("${it.exerciseName}: ${it.maxLift}"))
            }
            document.add(Paragraph("")) // Add empty line for spacing

            // Add health metrics
            document.add(Paragraph("Health Metrics:"))
            profileData.healthMetrics.forEach {
                document.add(Paragraph("${it.metricName}: ${it.value}"))
            }
            document.add(Paragraph("")) // Add empty line for spacing

            // Add injury metrics
            document.add(Paragraph("Injury Metrics:"))
            profileData.injuryMetrics.forEach {
                document.add(Paragraph("${it.details}"))
            }
            document.add(Paragraph("")) // Add empty line for spacing

            // Add allergies metrics
            document.add(Paragraph("Allergies Metrics:"))
            profileData.allergiesMetrics.forEach {
                document.add(Paragraph("${it.type}"))
            }
            document.add(Paragraph("")) // Add empty line for spacing

            // Add personal notes
            document.add(Paragraph("Personal Notes:"))
            profileData.personalNotes.forEach {
                document.add(Paragraph("${it.title}: ${it.content}"))
            }

            // Close the document
            document.close()

            Log.d(TAG, "PDF exported successfully to: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "Error exporting PDF: ${e.message}")
        } finally {
            outputStream.close()
        }
    }

    companion object {
        private const val TAG = "ProfileDataHandler"
    }
}

