package com.example.bachelor_work.fragments

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.adapter.DialogAdapter
import com.example.bachelor_work.adapter.WeightAdapter
import com.example.bachelor_work.model.DialogInfo
import com.example.bachelor_work.utils.DialogStorage
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class HistoryFragment : Fragment() {

    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var dialogAdapter: DialogAdapter
    private lateinit var dialogStorage: DialogStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogStorage = DialogStorage(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        dialogRecyclerView = view.findViewById(R.id.dialogRecyclerView)
        dialogAdapter = DialogAdapter(dialogStorage.retrieveDialogs())
        dialogRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogRecyclerView.adapter = dialogAdapter

        dialogAdapter.setOnItemClickListener { dialogInfo ->
            // Handle click event here, such as opening a BottomSheetDialog
            showBottomSheetDialog(dialogInfo)
        }

        return view
    }

    private fun showBottomSheetDialog(dialogInfo: DialogInfo) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_weight_table, null)

        // Find views in the dialog layout
        val titleTextView = bottomSheetView.findViewById<TextView>(R.id.dialogTitleTextView)
        val weightRecyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.weightRecyclerView)
        val closeButton = bottomSheetView.findViewById<Button>(R.id.closeButton)
        val exportButton = bottomSheetView.findViewById<Button>(R.id.exportButton)

        // Set data to views
        titleTextView.text = dialogInfo.title

        // Set up RecyclerView adapter and layout manager here if needed
        val adapter = WeightAdapter(dialogInfo.weightItems)
        weightRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        weightRecyclerView.adapter = adapter

        // Set onClickListener for the close button
        closeButton.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Set onClickListener for the export button
        exportButton.setOnClickListener {
            exportData(dialogInfo)
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun exportData(dialogInfo: DialogInfo) {
        val context: Context = requireContext()

        // Use the Documents directory for export
        val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (documentsDir?.exists() != true) {
            documentsDir?.mkdirs()
        }

        val fileName = "exported_data_${System.currentTimeMillis()}.txt"
        val file = File(documentsDir, fileName)

        try {
            FileOutputStream(file).use { fos ->
                OutputStreamWriter(fos).use { writer ->
                    writer.write("Title: ${dialogInfo.title}\n")
                    dialogInfo.weightItems.forEach { weightItem ->
                        writer.write("Weight Item: ${weightItem}\n")
                    }
                    writer.flush()
                }
            }
            // Notify user of successful export
            Toast.makeText(context, "Export successful: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            // Notify user of failure
            Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}
