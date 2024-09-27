// File: adapters/MonthsAdapter.kt
package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.MonthWorkout
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserWorkout

class MonthsAdapter(
    private val months: List<MonthWorkout>,
    private val onWorkoutClick: (UserWorkout) -> Unit // Lambda to handle workout clicks
) : RecyclerView.Adapter<MonthsAdapter.MonthViewHolder>() {

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutItemDate: TextView = itemView.findViewById(R.id.workout_item_date)
        val workoutCount: TextView = itemView.findViewById(R.id.workout_count)
        val previousWorkoutsList: RecyclerView = itemView.findViewById(R.id.previousWorkoutsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_previous_workouts_list, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthWorkout = months[position]
        holder.workoutItemDate.text = monthWorkout.month
        holder.workoutCount.text = "${monthWorkout.workouts.size} Workouts"

        // Initialize the inner RecyclerView
        holder.previousWorkoutsList.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.previousWorkoutsList.adapter = WorkoutsAdapter(monthWorkout.workouts, onWorkoutClick)
        holder.previousWorkoutsList.setHasFixedSize(true)
        holder.previousWorkoutsList.isNestedScrollingEnabled = false
    }

    override fun getItemCount(): Int = months.size

}
