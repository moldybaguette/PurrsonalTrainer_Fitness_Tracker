package za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

/**
 * A custom button with a gradient background and customizable text.
 *
 * @constructor Creates a BubbleButton with the specified attributes.
 * @param context The context in which the button is used.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
 */
class BubbleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        // Set the base drawable as the background
        background = ContextCompat.getDrawable(context, R.drawable.bblbtn_combined)

        // Obtain custom attributes
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BubbleButton,
            0, 0
        ).apply {
            try {
                val startColor = getColor(R.styleable.GradientTextView_startColor, ContextCompat.getColor(context, R.color.default_start_color))
                val endColor = getColor(R.styleable.GradientTextView_endColor, ContextCompat.getColor(context, R.color.default_end_color))
                val buttonText = getString(R.styleable.BubbleButton_buttonText) ?: ""

                setButtonProperties(startColor, endColor, buttonText)
            } finally {
                recycle()
            }
        }
    }

    /**
     * Set the gradient background and text for the button.
     *
     * @param startColor The start color for the gradient.
     * @param endColor The end color for the gradient.
     * @param text The text to be set on the button.
     */
    fun setButtonProperties(startColor: Int, endColor: Int, text: String) {
        // Get the base drawable and modify its gradient
        val layerDrawable = background as LayerDrawable
        val gradientDrawable = layerDrawable.getDrawable(0) as GradientDrawable
        gradientDrawable.colors = intArrayOf(startColor, endColor)

        // Set the text
        setText(text)
    }
}