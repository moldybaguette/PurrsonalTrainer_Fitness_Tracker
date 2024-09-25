package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.fragments.ChooseCategoryFragment
import java.io.InputStreamReader
import java.lang.Thread.sleep


class CategoryAdapter(private val categories: List<String>, private val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.categoryText)
        val imageView: ImageView = itemView.findViewById(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_category_list, parent, false)
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

        //HANDLES GETTING ALL EXERCISES FOR A CATEGORY WHEN CLICKING ON A CATEGORY
        holder.itemView.setOnClickListener {
           println("clicked on $category")
            CoroutineScope(Dispatchers.Main).launch {
                val exerciseList = async(Dispatchers.IO) {
                    val inputStream = context.assets.open("globalExercises.json")
                    val reader = InputStreamReader(inputStream)
                    val gson = Gson()
                    val exerciseListType = object : TypeToken<List<Exercise>>() {}.type
                    val allExercises = gson.fromJson<List<Exercise>>(reader, exerciseListType)
                    allExercises.filter { it.category == category }
                }.await()
                //just print the list of exercises for now
                println("exerciseList: $exerciseList")
            }
        }
    }
    
    override fun getItemCount(): Int {
        return categories.size
    }
}