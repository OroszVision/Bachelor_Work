package com.example.bachelor_work.utils
import android.content.Context
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelor_work.R
import com.example.bachelor_work.adapter.WeightAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itextpdf.io.font.FontConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.property.VerticalAlignment
import java.io.File
import java.io.FileOutputStream
import kotlin.math.round

class ToolbarHelper {

    companion object {
        fun setupToolbar(fragment: Fragment, view: View) {
            val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
            (fragment.requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24) // Set your own icon for the back arrow
            toolbar.setNavigationOnClickListener {
                fragment.findNavController().navigateUp()
            }
        }

        fun Double.round(decimals: Int): Double {
                var multiplier = 1.0
                repeat(decimals) { multiplier *= 10 }
                return round(this * multiplier) / multiplier
            }

        fun exportDialogToPDF(context: Context, weightTableDialog: BottomSheetDialog, adapter: WeightAdapter, filePath: String, fragmentName: String) {

            val file = File(context.filesDir, filePath)
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

            //Log.d("PDFExport", "PDF exported successfully to: ${file.absolutePath}")

            weightTableDialog.dismiss()
        }

        private fun createCell(text: String, isHeader: Boolean): Cell {
            val cell = Cell()
            val displayText = if (text == "-1") "#" else text // Display "#" if the value is -1
            cell.add(Paragraph(displayText))
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE)
            cell.setTextAlignment(TextAlignment.CENTER)
            if (isHeader) {
                cell.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD))
            }
            return cell
        }

    }
}


