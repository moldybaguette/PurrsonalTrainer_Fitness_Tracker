package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import androidx.core.content.ContextCompat



class CategoryAdapter(
    private val categories: List<String>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(category: String)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.categoryText)
        val imageView: ImageView = itemView.findViewById(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_list, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.textView.text = category

        // Set background color for every second item
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.listGrey))
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }

        // Define color palette for categories
        val colors = listOf(
            ContextCompat.getColor(holder.itemView.context, R.color.categoryPink),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryRed),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryOrange1),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryOrange2),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryYellow),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryLightYellow),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryGreen1),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryGreen2),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryLightBlue),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryBlue),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryDarkBlue),
            ContextCompat.getColor(holder.itemView.context, R.color.categoryPurple),
        )

        val color = colors[position % colors.size]
        holder.textView.setTextColor(color)
        holder.imageView.setColorFilter(color)

        // Set the OnClickListener for the item
        holder.itemView.setOnClickListener {
            listener.onItemClick(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
