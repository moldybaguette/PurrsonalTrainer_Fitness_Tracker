package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.CategoryAnalysis

class AnalysisBreakdownAdapter(
    private var categoryAnalysisList: List<CategoryAnalysis>
) : RecyclerView.Adapter<AnalysisBreakdownAdapter.AnalysisViewHolder>() {

    class AnalysisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorCircle: View = itemView.findViewById(R.id.colorCircle)
        val categoryDisplay: TextView = itemView.findViewById(R.id.categoryDisplay)
        val setAmountDisplay: TextView = itemView.findViewById(R.id.setAmountDisplay)
        val piePercentageDisplay: TextView = itemView.findViewById(R.id.piePercentageDisplay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_analysis_info, parent, false)
        return AnalysisViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnalysisViewHolder, position: Int) {
        val item = categoryAnalysisList[position]
        holder.categoryDisplay.text = item.categoryName
        holder.setAmountDisplay.text = "${item.setCount} Sets"
        holder.piePercentageDisplay.text = "${String.format("%.1f", item.percentage)}%"

        // Set the color circle. Assuming 'color' is a color resource ID.
        holder.colorCircle.setBackgroundResource(item.color)
    }

    override fun getItemCount(): Int = categoryAnalysisList.size

    fun updateData(newData: List<CategoryAnalysis>) {
        categoryAnalysisList = newData
        notifyDataSetChanged()
    }
}