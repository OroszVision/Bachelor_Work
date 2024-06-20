package com.example.bachelor_work.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.bachelor_work.adapter.WeightAdapter
import com.example.bachelor_work.model.DialogInfo
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment

import java.io.File
import java.io.FileOutputStream

class DialogStorage(private val context: Context) {
    companion object {
        private const val TAG = "DialogStorage"
        private const val FILE_NAME = "dialog_history.json"
    }

    fun storeDialog(dialogInfo: DialogInfo) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            val dialogList = retrieveDialogs().toMutableList()

            // Add the new dialog to the beginning of the list
            dialogList.add(0, dialogInfo)

            // Limit the list to 10 newest dialogs
            if (dialogList.size > 10) {
                // Sort the list based on timestamp (assuming timestamp is a comparable property)
                dialogList.sortByDescending { it.timestamp }

                // Remove excess dialogs beyond the 5th position
                val excessDialogs = dialogList.subList(10, dialogList.size)
                excessDialogs.forEach {
                    // Implement logic to delete dialog from file or perform any cleanup needed
                    // For example, you can perform any necessary cleanup related to the dialog here
                }
                dialogList.removeAll(excessDialogs)
            }

            val jsonString = Gson().toJson(dialogList)
            FileOutputStream(file).use { it.write(jsonString.toByteArray()) }
            Log.d(TAG, "Dialog stored successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error storing dialog: ${e.message}")
        }
    }


    fun retrieveDialogs(): List<DialogInfo> {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) {
                return emptyList()
            }
            val jsonString = file.readText()
            val dialogList = Gson().fromJson(jsonString, Array<DialogInfo>::class.java).toList()
            Log.d(TAG, "Dialogs retrieved successfully")
            return dialogList
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving dialogs: ${e.message}")
            return emptyList()
        }
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    fun exportDialogToPDF(context: Context, adapter: WeightAdapter, fileName: String, fragmentName: String) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(downloadDir, fileName)
        val writer = PdfWriter(FileOutputStream(file))
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        // Add fragment name as title
        val title = Paragraph(fragmentName)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(20f)
            .setBold()
            .setMarginBottom(20f)
        document.add(title)

        // Add table header
        val table = Table(UnitValue.createPercentArray(4)).useAllAvailableWidth()
        table.addCell(createCell("Percentage", true))
        table.addCell(createCell("Sets", true))
        table.addCell(createCell("Reps", true))
        table.addCell(createCell("Weight", true))

        // Add table rows
        for (item in adapter.getItems()) {
            table.addCell(createCell("${item.percentage}%", false))
            table.addCell(createCell(item.sets.toString(), false))
            table.addCell(createCell(item.reps.toString(), false))
            table.addCell(createCell("${item.weight} kg", false))
        }

        document.add(table)

        document.close()

        Log.d(TAG, "PDF exported successfully to: ${file.absolutePath}")
    }


    private fun createCell(text: String, isHeader: Boolean): Cell {
        val cell = Cell()
        val displayText = if (text == "-1") "#" else text // Display "#" if the value is -1
        cell.add(Paragraph(displayText))
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE)
        cell.setTextAlignment(TextAlignment.CENTER)
        if (isHeader) {
            cell.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
        }
        return cell
    }

}
