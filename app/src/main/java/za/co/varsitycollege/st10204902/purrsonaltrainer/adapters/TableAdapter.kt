// TableAdapter.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ItemDataCellBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ItemHeaderCellBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.databinding.ItemLabelCellBinding
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CellType
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.TableCell

class TableAdapter(private val tableData: List<TableCell>,
                   private val spanCount: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_LABEL = 1
        private const val TYPE_DATA = 2
    }

    // ViewHolder for Header Cells
    inner class HeaderViewHolder(val binding: ItemHeaderCellBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder for Label Cells
    inner class LabelViewHolder(val binding: ItemLabelCellBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder for Data Cells
    inner class DataViewHolder(val binding: ItemDataCellBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (tableData[position].type) {
            CellType.HEADER -> TYPE_HEADER
            CellType.LABEL -> TYPE_LABEL
            CellType.DATA -> TYPE_DATA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_HEADER -> {
                val binding = ItemHeaderCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            TYPE_LABEL -> {
                val binding = ItemLabelCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LabelViewHolder(binding)
            }
            TYPE_DATA -> {
                val binding = ItemDataCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DataViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cell = tableData[position]

        val font = ResourcesCompat.getFont(holder.itemView.context, R.font.knicknack_medium)

        when(holder) {
            is HeaderViewHolder -> {
                holder.binding.headerTextView.text = cell.text
                // Customize header appearance if needed
                holder.binding.headerTextView.setTypeface(font, Typeface.BOLD)
                holder.binding.headerTextView.gravity = Gravity.CENTER
            }
            is LabelViewHolder -> {
                holder.binding.labelTextView.text = cell.text
                // Customize label appearance if needed
                holder.binding.labelTextView.setTypeface(font, Typeface.BOLD)
                holder.binding.labelTextView.gravity = Gravity.CENTER
                holder.binding.labelTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.comboBoxGrey))
            }
            is DataViewHolder -> {
                holder.binding.dataTextView.text = cell.text
                // Set text color based on cell content
                if (cell.text != "---") {
                    holder.binding.dataTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
                } else {
                    holder.binding.dataTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.rmGrey))
                }
                // Customize data cell appearance if needed
                holder.binding.dataTextView.gravity = Gravity.CENTER
            }
        }
    }

    override fun getItemCount(): Int = tableData.size
}
