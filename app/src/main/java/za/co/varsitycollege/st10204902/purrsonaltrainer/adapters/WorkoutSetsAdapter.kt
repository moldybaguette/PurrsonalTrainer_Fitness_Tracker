package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R
import za.co.varsitycollege.st10204902.purrsonaltrainer.adapters.RoutineSetsAdapter.RoutineSetsViewHolder
import za.co.varsitycollege.st10204902.purrsonaltrainer.models.WorkoutSet

class WorkoutSetsAdapter(
    private val sets: MutableList<WorkoutSet>,
    private val context: Context
) : RecyclerView.Adapter<WorkoutSetsAdapter.WorkoutSetsViewHolder>()
{
    class WorkoutSetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val setType = itemView.findViewById<Spinner>(R.id.setType)
        val previousWeight = itemView.findViewById<TextView>(R.id.previousWeight)
        val weightInput = itemView.findViewById<EditText>(R.id.weightInput)
        val repsInput = itemView.findViewById<EditText>(R.id.repsInput)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutSetsViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_tracking_reps, parent, false)
        return WorkoutSetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutSetsViewHolder, position: Int)
    {
        val set = sets[position]

        // setType
        when (set.setType)
        {
            SetType.WARMUP.name ->
            {
                holder.setType.adapter = SetTypeSpinnerAdapter(context, listOf(SetType.WARMUP, SetType.NORMAL, SetType.FAILURE, SetType.DROP))
            }
            SetType.FAILURE.name ->
            {
                holder.setType.adapter = SetTypeSpinnerAdapter(context, listOf(SetType.FAILURE, SetType.NORMAL, SetType.WARMUP, SetType.DROP))
            }
            SetType.DROP.name ->
            {
                holder.setType.adapter = SetTypeSpinnerAdapter(context, listOf(SetType.DROP, SetType.NORMAL, SetType.WARMUP, SetType.FAILURE))
            }
            else ->
            {
                holder.setType.adapter = SetTypeSpinnerAdapter(context, listOf(SetType.NORMAL, SetType.WARMUP, SetType.FAILURE, SetType.DROP))
            }
        }
        // previousWeight
        // weightInput
        if (set.weight != null)
            holder.weightInput.setText(set.weight.toString())
        // repsInput
        if (set.reps != null)
            holder.repsInput.setText(set.reps.toString())
    }

    override fun getItemCount(): Int
    {
        return sets.count()
    }
}