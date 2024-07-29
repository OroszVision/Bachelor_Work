package com.example.bachelor_work.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import com.example.bachelor_work.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFont
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
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
            // Load fonts
            val titleFont: PdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
            val bodyFont: PdfFont = PdfFontFactory.createFont(StandardFonts.HELVETICA)

            // Add document title
            val title = Paragraph("Profile Data")
                .setFont(titleFont)
                .setFontSize(24f)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setMarginBottom(20f)
                .setMarginTop(30f)
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setPadding(10f)
            document.add(title)

            // Add profile data details
            addSectionHeader(document, "Profile Details", titleFont)
            addParagraph(document, "Body Weight: ${profileData.bodyWeight}", bodyFont)
            addParagraph(document, "Body Fat Percentage: ${profileData.bodyFatPercentage}", bodyFont)
            addParagraph(document, "Height: ${profileData.height}", bodyFont)
            addParagraph(document, "Age: ${profileData.age}", bodyFont)
            addParagraph(document, "Blood Pressure: ${profileData.bloodPressure}", bodyFont)
            addParagraph(document, "Heart Rate: ${profileData.heartRate}", bodyFont)
            addEmptyLine(document, 10F)

            // Add strength metrics
            addSectionHeader(document, "Strength Metrics", titleFont)
            profileData.strengthMetrics.forEach {
                addParagraph(document, "${it.exerciseName}: ${it.maxLift}", bodyFont)
            }
            addEmptyLine(document, 10F)

            // Add health metrics
            addSectionHeader(document, "Health Metrics", titleFont)
            profileData.healthMetrics.forEach {
                addParagraph(document, "${it.metricName}: ${it.value}", bodyFont)
            }
            addEmptyLine(document, 10F)

            // Add injury metrics
            addSectionHeader(document, "Injury Metrics", titleFont)
            profileData.injuryMetrics.forEach {
                addParagraph(document, it.details, bodyFont)
            }
            addEmptyLine(document, 10F)

            // Add allergies metrics
            addSectionHeader(document, "Allergies Metrics", titleFont)
            profileData.allergiesMetrics.forEach {
                addParagraph(document, it.type, bodyFont)
            }
            addEmptyLine(document, 10F)

            // Add personal notes
            addSectionHeader(document, "Personal Notes", titleFont)
            profileData.personalNotes.forEach {
                addParagraph(document, "${it.title}: ${it.content}", bodyFont)
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

    private fun addSectionHeader(document: Document, headerText: String, font: PdfFont) {
        val header = Paragraph(headerText)
            .setFont(font)
            .setFontSize(18f)
            .setTextAlignment(TextAlignment.LEFT)
            .setMarginBottom(5f)
            .setBold()
            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            .setPadding(5f)
        document.add(header)
    }

    private fun addParagraph(document: Document, text: String, font: PdfFont) {
        val paragraph = Paragraph(Text(text).setFont(font))
            .setFontSize(12f)
            .setMarginBottom(10f)
        document.add(paragraph)
    }

    private fun addEmptyLine(document: Document, height: Float) {
        val emptyLine = Paragraph().setMarginBottom(height)
        document.add(emptyLine)
    }
    // In ProfileDataHandler
    fun getStrengthMetricsWithHistory(): List<StrengthMetric> {
        val profileData = loadProfileData()
        return profileData.strengthMetrics
    }

    fun getStatisticsData(): Map<String, List<Pair<String, Float>>> {
        val profileData = loadProfileData()
        val statisticsData = mutableMapOf<String, List<Pair<String, Float>>>()

        // Prepare data for each metric type
        val strengthMetrics = profileData.strengthMetrics.map { it.exerciseName to it.maxLift.toFloat() }
        val healthMetrics = profileData.healthMetrics.map { it.metricName to it.value.toFloat() }
        val injuryMetrics = profileData.injuryMetrics.map { it.details to 1.0f }
        val allergiesMetrics = profileData.allergiesMetrics.map { it.type to 1.0f }

        statisticsData["Strength Metrics"] = strengthMetrics
        statisticsData["Health Metrics"] = healthMetrics
        statisticsData["Injury Metrics"] = injuryMetrics
        statisticsData["Allergies Metrics"] = allergiesMetrics

        return statisticsData
    }

    companion object {
        private const val TAG = "ProfileDataHandler"
    }
}
