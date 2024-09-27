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

/**
 * Adapter class for displaying table data in a RecyclerView.
 *
 * @property tableData The list of TableCell objects representing the table data.
 * @property spanCount The number of columns in the table.
 */
class TableAdapter(private val tableData: List<TableCell>,
                   private val spanCount: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_LABEL = 1
        private const val TYPE_DATA = 2
    }

    /**
     * ViewHolder class for header cells.
     *
     * @property binding The binding object for the header cell layout.
     */
    inner class HeaderViewHolder(val binding: ItemHeaderCellBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * ViewHolder class for label cells.
     *
     * @property binding The binding object for the label cell layout.
     */
    inner class LabelViewHolder(val binding: ItemLabelCellBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * ViewHolder class for data cells.
     *
     * @property binding The binding object for the data cell layout.
     */
    inner class DataViewHolder(val binding: ItemDataCellBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Returns the view type of the item at the given position.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The view type of the item at the given position.
     */
    override fun getItemViewType(position: Int): Int {
        return when (tableData[position].type) {
            CellType.HEADER -> TYPE_HEADER
            CellType.LABEL -> TYPE_LABEL
            CellType.DATA -> TYPE_DATA
        }
    }

    /**
     * Creates a new ViewHolder for the given view type.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder that holds a view of the given view type.
     */
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

    /**
     * Binds the data to the ViewHolder at the given position.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cell = tableData[position]

        val font = ResourcesCompat.getFont(holder.itemView.context, R.font.knicknack_medium)

        val textColour = ContextCompat.getColor(holder.itemView.context, R.color.hint_text)

        when(holder) {
            is HeaderViewHolder -> {
                holder.binding.headerTextView.text = cell.text
                // Customize header appearance if needed
                holder.binding.headerTextView.setTypeface(font, Typeface.BOLD)
                holder.binding.headerTextView.gravity = Gravity.CENTER
                holder.binding.headerTextView.setTextColor(textColour)
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
                    holder.binding.dataTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.hint_text))
                } else {
                    holder.binding.dataTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.rmGrey))
                }
                // Customize data cell appearance if needed
                holder.binding.dataTextView.gravity = Gravity.CENTER
            }
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the data set.
     */
    override fun getItemCount(): Int = tableData.size
}