package com.example.bachelor_work.utils

import android.content.Context
import android.util.Log
import com.example.bachelor_work.model.DialogInfo
import com.google.gson.Gson
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
            dialogList.add(dialogInfo)
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
}
