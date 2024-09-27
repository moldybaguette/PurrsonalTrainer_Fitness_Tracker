// File: adapters/WorkoutsAdapter.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutsAdapter(
    private val workouts: List<UserWorkout>,
    private val onItemClick: (UserWorkout) -> Unit // Lambda for click handling
) : RecyclerView.Adapter<WorkoutsAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(itemView: View, private val onItemClick: (UserWorkout) -> Unit, private val workouts: List<UserWorkout>) : RecyclerView.ViewHolder(itemView) {
    val workoutDayOfWeek: TextView = itemView.findViewById(R.id.workoutDayOfWeek)
    val workoutDayOfMonth: TextView = itemView.findViewById(R.id.workoutDayOfMonth)
    val workoutDuration: TextView = itemView.findViewById(R.id.workoutDuration)
    val workoutTitle: TextView = itemView.findViewById(R.id.workoutTitle)
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

        // TODO: If you want to display exercises, initialize and set an adapter for workoutExercises RecyclerView
        /*
        val exercisesRecyclerView: RecyclerView = holder.itemView.findViewById(R.id.workoutExersises)
        exercisesRecyclerView.adapter = ExercisesAdapter(workout.workoutExercises.values.toList())
        */
    }

    override fun getItemCount(): Int = workouts.size
}
