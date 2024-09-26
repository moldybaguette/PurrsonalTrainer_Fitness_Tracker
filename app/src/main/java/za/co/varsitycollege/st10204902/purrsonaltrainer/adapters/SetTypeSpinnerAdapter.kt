package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

enum class SetType
{
    NORMAL,
    WARMUP,
    FAILURE,
    DROP
}


class SetTypeSpinnerAdapter(
    private val context: Context,
    private val setTypes: List<SetType>,
) : ArrayAdapter<SetType>(context, 0, setTypes)
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
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_set_type, parent, false)
        val setType = setTypes[position]

        val textView = view.findViewById<TextView>(R.id.setTypeText)

        when (setType)
        {
            SetType.NORMAL ->
            {
                textView.text = "N"
                textView.setTextColor(ContextCompat.getColor(context, R.color.normalSet))
            }
            SetType.WARMUP ->
            {
                textView.text = "W"
                textView.setTextColor(ContextCompat.getColor(context, R.color.warmupSet))
            }
            SetType.FAILURE ->
            {
                textView.text = "F"
                textView.setTextColor(ContextCompat.getColor(context, R.color.failureSet))
            }
            SetType.DROP ->
            {
                textView.text = "D"
                textView.setTextColor(ContextCompat.getColor(context, R.color.dropSet))
            }
        }

        return view
    }
}