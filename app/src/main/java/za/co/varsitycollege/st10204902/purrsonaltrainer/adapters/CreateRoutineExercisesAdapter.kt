package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineListAdapter.RoutineViewHolder
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutExercise

class CreateRoutineExercisesAdapter(
    private val exercises: List<WorkoutExercise>,
    private val context: Context
) : RecyclerView.Adapter<CreateRoutineExercisesAdapter.CreateRoutineExercisesViewHolder>()
{
    class CreateRoutineExercisesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val exerciseName = itemView.findViewById<TextView>(R.id.exerciseName)
        val notes = itemView.findViewById<EditText>(R.id.notesEditText)
        val routineSets = itemView.findViewById<RecyclerView>(R.id.routineSetsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRoutineExercisesViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_exercisedetails_display, parent, false)
        return CreateRoutineExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateRoutineExercisesViewHolder, position: Int)
    {
        val exercise = exercises[position]

        // Binding exerciseName
        holder.exerciseName.text = exercise.exerciseName

        // Binding initial notes to exercise notes
        holder.notes.setText(exercise.notes)

        // Binding routineSets
    }

    override fun getItemCount(): Int
    {
        return exercises.count()
    }
}