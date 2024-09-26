package za.co.varsitycollege.st10204902.purrsonaltrainer.frontend_logic

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

/**
 * A custom EditText with gradient text and stroke.
 *
 * @constructor Creates a GradientEditText with the specified attributes.
 * @param context The context in which the EditText is used.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
 */
class GradientEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val strokePaint = Paint()
    private var startColor: Int
    private var endColor: Int
    private var strokeColor: Int
    private var strokeWidth: Float

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GradientTextView,
            0, 0
        ).apply {
            try {
                startColor = getColor(R.styleable.GradientTextView_startColor, context.getColor(R.color.default_start_color))
                endColor = getColor(R.styleable.GradientTextView_endColor, context.getColor(R.color.default_end_color))
                strokeColor = getColor(R.styleable.GradientTextView_strokeColor, context.getColor(R.color.default_stroke_color))
                strokeWidth = getDimension(R.styleable.GradientTextView_strokeWidth, 30f)
            } finally {
                recycle()
            }
        }

        val shader = LinearGradient(
            0f, 0f, 0f, textSize,
            intArrayOf(startColor, endColor),
            floatArrayOf(0.50f, 1.0f),
            Shader.TileMode.CLAMP
        )
        paint.shader = shader

        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth
        strokePaint.color = strokeColor
        strokePaint.isAntiAlias = true

        setPadding(strokeWidth.toInt(), strokeWidth.toInt(), strokeWidth.toInt(), strokeWidth.toInt())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Ensure the superclass method is called
        return super.onTouchEvent(event)
    }

    /**
     * Draws the text with gradient and stroke.
     * @param canvas The canvas on which the background will be drawn.
     */
    override fun onDraw(canvas: Canvas) {
        val text = text.toString()
        val x = ((width - paint.measureText(text)) / 2)
        val y = (height / 2) - ((strokePaint.descent() + strokePaint.ascent()) / 2)

        strokePaint.textSize = textSize
        strokePaint.typeface = typeface
        strokePaint.textAlign = paint.textAlign

        canvas.drawText(text, x, y, strokePaint)

        paint.style = Paint.Style.FILL
        canvas.drawText(text, x, y, paint)
    }
}