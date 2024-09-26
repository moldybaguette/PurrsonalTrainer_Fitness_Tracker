// za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.AnalysisBreakdownAdapter.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CategoryAnalysis

class AnalysisBreakdownAdapter(
    private var categoryAnalysisList: List<CategoryAnalysis>
) : RecyclerView.Adapter<AnalysisBreakdownAdapter.AnalysisViewHolder>() {

    // ViewHolder Class
    class AnalysisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorIndicator: View = itemView.findViewById(R.id.colorIndicator)
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val setCountTextView: TextView = itemView.findViewById(R.id.setCountTextView)
        val percentageTextView: TextView = itemView.findViewById(R.id.percentageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_analysis_info, parent, false)
        return AnalysisViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnalysisViewHolder, position: Int) {
        val item = categoryAnalysisList[position]
        holder.categoryNameTextView.text = item.categoryName
        holder.setCountTextView.text = "Sets: ${item.setCount}"
        holder.percentageTextView.text = "${String.format("%.1f", item.percentage)}%"

        // Set the color indicator
        holder.colorIndicator.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, item.color)
        )
        holder.categoryNameTextView.setTextColor(
            ContextCompat.getColor(holder.itemView.context, item.color)
        )
    }

    override fun getItemCount(): Int = categoryAnalysisList.size

    // Method to update data
    fun updateData(newData: List<CategoryAnalysis>) {
        categoryAnalysisList = newData
        notifyDataSetChanged()
    }
}
