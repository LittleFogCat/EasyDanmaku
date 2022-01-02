package top.littlefogcat.easydanmaku.sample.pressuretest

import android.graphics.Canvas
import android.graphics.Color
import top.littlefogcat.esus.view.ViewGroup
import top.littlefogcat.esus.view.ViewParent
import top.littlefogcat.esus.widget.TextView
import kotlin.random.Random

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class ScrollingTextView(text: CharSequence, x: Int, y: Int) :
    TextView(text) {
    private val speed: Float

    fun setStrokeEnabled(enable: Boolean) {
        if (enable) {
            stroke = Stroke(Color.BLACK, 2f)
        } else {
            stroke = null
        }
    }

    init {
        translationX = x.toFloat()
        translationY = y.toFloat()
        textSize = 89f
        speed = 9 * Random.Default.nextFloat() + 3
    }

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Long) {
        translationX += speed
        parent as ViewGroup
        if (translationX < -width || translationX >= parent.width) {
            translationX = (-width).toFloat()
        }

        super.onDraw(canvas, parent, time)
    }
}