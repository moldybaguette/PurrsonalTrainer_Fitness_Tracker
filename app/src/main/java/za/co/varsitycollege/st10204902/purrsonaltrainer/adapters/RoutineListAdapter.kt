package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CategoryAdapter.CategoryViewHolder
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine

interface OnRoutineItemClickListener {
    fun onItemClick(routine: UserRoutine)
}

class RoutineListAdapter(
    private val routines: List<UserRoutine>,
    private val context: Context,
    private val listener: OnRoutineItemClickListener
) : RecyclerView.Adapter<RoutineListAdapter.RoutineViewHolder>()
{
    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routineTitle = itemView.findViewById<TextView>(R.id.routineTitle)
        val routineExerciseView = itemView.findViewById<RecyclerView>(R.id.routineExercises)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_workouts_display, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int)
    {
        val routine = routines[position]

        // Routine title
        holder.routineTitle.text = routine.name
        holder.routineExerciseView.layoutManager = LinearLayoutManager(context)
        holder.routineExerciseView.adapter = RoutineExerciseListAdapter(routine.exercises.values.toList(), context)
    }

    override fun getItemCount(): Int
    {
        return routines.count()
    }
}