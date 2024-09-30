// File: adapters/WorkoutsAdapter.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutsAdapter(
    private val workouts: List<UserWorkout>,
    private val onItemClick: (UserWorkout) -> Unit, // Lambda for click handling
    private val context: Context
) : RecyclerView.Adapter<WorkoutsAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(itemView: View, private val onItemClick: (UserWorkout) -> Unit, private val workouts: List<UserWorkout>) : RecyclerView.ViewHolder(itemView) {
    val workoutDayOfWeek: TextView = itemView.findViewById(R.id.workoutDayOfWeek)
    val workoutDayOfMonth: TextView = itemView.findViewById(R.id.workoutDayOfMonth)
    val workoutDuration: TextView = itemView.findViewById(R.id.workoutDuration)
    val workoutTitle: TextView = itemView.findViewById(R.id.workoutTitle)
    val exercisesRecyclerView: RecyclerView = itemView.findViewById(R.id.workoutExersises)
    init {
        // Set click listener on the entire item view
        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClick(workouts[position])
            }
        }
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_single_workout, parent, false)
        return WorkoutViewHolder(view, onItemClick, workouts)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        val dateFormatDayOfWeek = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormatDayOfMonth = SimpleDateFormat("d", Locale.getDefault())

        holder.workoutDayOfWeek.text = dateFormatDayOfWeek.format(workout.date)
        holder.workoutDayOfMonth.text = dateFormatDayOfMonth.format(workout.date)
        holder.workoutDuration.text = "${workout.durationSeconds / 60} min"
        holder.workoutTitle.text = workout.name
        this.setTitleColor(workout.color, holder.workoutTitle)

        val exercises = workout.workoutExercises.values.toList()
        holder.exercisesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.exercisesRecyclerView.adapter = RoutineExerciseListAdapter(exercises, holder.itemView.context)
        holder.exercisesRecyclerView.setHasFixedSize(true)
        holder.exercisesRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setTitleColor(color: String, title: TextView)
    {
        when (color)
        {
            "blue" -> title.setTextColor(ContextCompat.getColor(context, R.color.blue_end))
            "red" -> title.setTextColor(ContextCompat.getColor(context, R.color.red_end))
            "orange" -> title.setTextColor(ContextCompat.getColor(context, R.color.orange_end))
            "yellow" -> title.setTextColor(ContextCompat.getColor(context, R.color.yellow_end))
            "green" -> title.setTextColor(ContextCompat.getColor(context, R.color.green_end))
            "purple" -> title.setTextColor(ContextCompat.getColor(context, R.color.purple_end))
            else -> title.setTextColor(ContextCompat.getColor(context, R.color.sso_gradient_end)) // make grey
        }
    }

    override fun getItemCount(): Int = workouts.size
}
