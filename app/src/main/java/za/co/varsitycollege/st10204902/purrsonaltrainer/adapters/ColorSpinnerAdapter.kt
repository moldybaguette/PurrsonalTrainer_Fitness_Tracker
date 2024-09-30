package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

class ColorSpinnerAdapter(
    context: Context,
    private val colors: List<String>) : ArrayAdapter<String>(context, 0, colors)
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
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_color_picker, parent, false)
        val color = colors[position]

        // Elements needing to be changed
        val layout = view.findViewById<LinearLayout>(R.id.colorPickerItemLayout)
        val colorCircle = view.findViewById<View>(R.id.colorCircle)
        val colorText = view.findViewById<TextView>(R.id.colorText)

        // Setting the spinner items
        when (color)
        {
            "blue" -> { colorText.text = "blue"; colorCircle.setBackgroundResource(R.drawable.color_picker_blue) }
            "red" -> { colorText.text = "red"; colorCircle.setBackgroundResource(R.drawable.color_picker_red) }
            "orange" -> { colorText.text = "orange"; colorCircle.setBackgroundResource(R.drawable.color_picker_orange) }
            "yellow" -> { colorText.text = "yellow"; colorCircle.setBackgroundResource(R.drawable.color_picker_yellow) }
            "green" -> { colorText.text = "green"; colorCircle.setBackgroundResource(R.drawable.color_picker_green) }
            "purple" -> { colorText.text = "purple"; colorCircle.setBackgroundResource(R.drawable.color_picker_purple) }
        }

        return view
    }
}