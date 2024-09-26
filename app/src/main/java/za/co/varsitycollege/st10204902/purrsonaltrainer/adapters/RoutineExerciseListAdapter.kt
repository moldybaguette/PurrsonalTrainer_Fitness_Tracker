package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineListAdapter.RoutineViewHolder
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.Exercise
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise

class RoutineExerciseListAdapter(
    private val exercises: List<WorkoutExercise>,
    private val context: Context,
) : RecyclerView.Adapter<RoutineExerciseListAdapter.RoutineExerciseViewHolder>()
{
    class RoutineExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseText = itemView.findViewById<TextView>(R.id.exerciseText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineExerciseViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_exersise, parent, false)
        return RoutineExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineExerciseViewHolder, position: Int)
    {
        val exercise = exercises[position]
        val name = exercise.exerciseName

        val sets = exercise.sets.count().toString()
        holder.exerciseText.text = "${sets}x ${name}"
    }

    override fun getItemCount(): Int
    {
        return exercises.count()
    }
}