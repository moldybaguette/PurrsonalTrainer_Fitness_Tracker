package za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

class BubbleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        // Set the base drawable as the background
        background = ContextCompat.getDrawable(context, R.drawable.bblbtn_combined)
    }

    /**
     * Set the gradient background and text for the button.
     *
     * @param startColorRes The resource ID of the start color for the gradient.
     * @param endColorRes The resource ID of the end color for the gradient.
     * @param textRes The resource ID of the text to be set on the button.
     */
    fun setButtonProperties(startColorRes: Int, endColorRes: Int, textRes: Int) {
        // Get the base drawable and modify its gradient
        val layerDrawable = background as LayerDrawable
        val gradientDrawable = layerDrawable.getDrawable(0) as GradientDrawable
        gradientDrawable.colors = intArrayOf(
            ContextCompat.getColor(context, startColorRes),
            ContextCompat.getColor(context, endColorRes)
        )

        // Set the text
        setText(textRes)
    }
}
