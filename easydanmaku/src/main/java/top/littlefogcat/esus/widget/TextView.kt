package top.littlefogcat.esus.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.BoringLayout
import android.text.TextPaint
import top.littlefogcat.esus.view.View
import top.littlefogcat.esus.view.ViewParent

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
open class TextView() : View() {

    var text: CharSequence = ""
        set(value) {
            field = value
            requestLayout()
        }
    var textSize = 60f
        set(value) {
            field = value
            requestLayout()
        }
    var textColor = Color.BLACK
    override val paint = TextPaint(super.paint)
    protected var boring: BoringLayout.Metrics? = null

    constructor(text: CharSequence) : this() {
        this.text = text
    }

    override fun onMeasure(width: Int, height: Int) {
        /*
         * top = -1.22size
         * bottom = 0.488size
         * ascent = -0.928size
         * descent = 0.244size
         *
         * height = 1.
         */
        paint.textSize = textSize
        val boring = BoringLayout.isBoring(text, paint)
        boring.apply {
//            Log.d(TAG, "onMeasure: $top, $bottom, $ascent, $descent, $width")
        }
        this.boring = boring
        setMeasuredDimensions(boring.width, boring.bottom - boring.top)
    }

    private val opacityMask = -0x1000000
    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        val boring = boring ?: return
        val x = 0f
        val y = -boring.ascent.toFloat()
        paint.color = textColor or opacityMask
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        canvas.drawText(text, 0, text.length, x, y, paint)
    }

    override fun toString(): String {
        return "TextView(text='$text' $x, $y, ${x + width}, ${y + height})"
    }

}