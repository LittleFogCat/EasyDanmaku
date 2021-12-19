package top.littlefogcat.easydanmaku.example.plain.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import top.littlefogcat.esus.view.ViewGroup
import top.littlefogcat.esus.view.ViewParent
import top.littlefogcat.esus.widget.TextView
import kotlin.random.Random

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class ScrollingTextView(text: String, x: Int, y: Int) :
    TextView(text) {
    private val speed: Float
    private var stroke = false

    fun setStrokeEnabled(enable: Boolean) {
        stroke = enable
    }

    init {
        translationX = x.toFloat()
        translationY = y.toFloat()
        textSize = 89f
        speed = 9 * Random.Default.nextFloat() + 3
    }

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        translationX += speed
        parent as ViewGroup
        if (translationX < -width || translationX >= parent.width) {
            translationX = (-width).toFloat()
        }

        if (stroke) {
            val boring = boring ?: return
            val x = 0f
            val y = -boring.ascent.toFloat()
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.textSize = textSize
            paint.strokeWidth = 3f
            canvas.drawText(text, x, y, paint)
        }

        super.onDraw(canvas, parent, time)
    }
}