package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R


class ExerciseTypeSpinnerAdapter(
    context: Context,
    private val exerciseTypes: List<String>) : ArrayAdapter<String>(context, 0, exerciseTypes)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        return createViewFromResource(position, convertView, parent, false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        return createViewFromResource(position, convertView, parent, true)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup, isDropdown: Boolean): View
    {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_exercise_spinner, parent, false)
        val exerciseType = exerciseTypes[position]

        val label = view.findViewById<TextView>(R.id.exerciseTypeText)
        label.text = exerciseType

        return view
    }
}