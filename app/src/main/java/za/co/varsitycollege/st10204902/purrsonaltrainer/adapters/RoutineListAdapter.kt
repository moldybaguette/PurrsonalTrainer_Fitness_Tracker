package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.CategoryAdapter.CategoryViewHolder
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.UserRoutine
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities.MadeRoutineActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.screens.workout_activities.StartEmptyWorkoutActivity
import za.co.varsitycollege.st10204902.purrsonaltrainer.services.navigateTo

/**
 * Interface for handling item click events in the routine list.
 */
interface OnRoutineItemClickListener {
    /**
     * Called when a routine item is clicked.
     *
     * @param routine The clicked UserRoutine object.
     */
    fun onItemClick(routine: UserRoutine)
}

/**
 * Adapter class for displaying a list of user routines in a RecyclerView.
 *
 * @property routines The list of UserRoutine objects to display.
 * @property context The context in which the adapter is used.
 * @property listener The listener for handling item click events.
 */
class RoutineListAdapter(
    private var routines: List<UserRoutine>,
    private val context: Context,
    private val listener: OnRoutineItemClickListener,
) : RecyclerView.Adapter<RoutineListAdapter.RoutineViewHolder>()
{
    /**
     * ViewHolder class for holding the views of a routine item.
     *
     * @param itemView The view of the routine item.
     */
    class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routineTitle: TextView = itemView.findViewById(R.id.routineTitle)
        val routineExerciseView: RecyclerView = itemView.findViewById(R.id.routineExercises)
        val routineLayout = itemView.findViewById<LinearLayout>(R.id.routineLayout)
    }

    /**
     * Creates a new ViewHolder for a routine item.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the view.
     * @return A new RoutineViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_workouts_display, parent, false)
        return RoutineViewHolder(view)
    }

    /**
     * Binds the data to the views of a routine item.
     *
     * @param holder The RoutineViewHolder.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routines[position]

        // Routine title
        holder.routineTitle.text = routine.name
        this.setTitleColor(routine.color, holder.routineTitle)
        holder.routineExerciseView.layoutManager = LinearLayoutManager(context)
        holder.routineExerciseView.adapter = RoutineExerciseListAdapter(routine.exercises.values.toList(), context)

        // Navigating to start workout
        holder.routineLayout.setOnClickListener {
            // Adding routineID for which the workout will be created
            val bundle = Bundle()
            bundle.putString("routineID", routine.routineID)
            navigateTo(context, MadeRoutineActivity::class.java, bundle)
        }
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

    fun removeItem(position: Int) {
        val mutableList = routines.toMutableList()
        mutableList.removeAt(position)
        routines = mutableList
        notifyItemRemoved(position)
    }

    fun restoreItem(item: UserRoutine, position: Int) {
        val mutableList = routines.toMutableList()
        mutableList.add(position, item)
        routines = mutableList
        notifyItemInserted(position)
    }

    fun getRoutineAt(position: Int): UserRoutine = routines[position]
    /**
     * Returns the total number of items in the list.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int {
        return routines.count()
    }
}