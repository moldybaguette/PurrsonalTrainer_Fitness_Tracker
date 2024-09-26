package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val context: Context,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(exercise: Exercise)
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseNameText: TextView = itemView.findViewById(R.id.categoryText)
        val exerciseIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_list, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseNameText.text = exercise.exerciseName

        // Set background color for every second item
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.listGrey))
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }

        if(exercise.isCustom) {
            holder.exerciseIcon.setImageResource(R.drawable.svg_custom_exercise)
        } else {
            holder.exerciseIcon.setImageResource(R.drawable.svg_saved_exercise_info)
        }

        // Define color palette for exercises
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
        holder.exerciseNameText.setTextColor(color)
        holder.exerciseIcon.setColorFilter(color)

        // Set the OnClickListener for the item
        holder.itemView.setOnClickListener {
            listener.onItemClick(exercise)
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}