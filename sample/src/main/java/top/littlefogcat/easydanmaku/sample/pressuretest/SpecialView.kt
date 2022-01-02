package top.littlefogcat.easydanmaku.sample.pressuretest

import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import top.littlefogcat.esus.view.View
import top.littlefogcat.esus.view.ViewGroup
import top.littlefogcat.esus.view.ViewParent
import kotlin.math.abs

/**
 * 高级弹幕参考实现
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class SpecialView : View() {
    private val camera = Camera()

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Long) {
        parent as ViewGroup

        // draw 1
        // y = 0.5x
        canvas.save()
        var frac = time / 2.5f % parent.width
        var dx = frac - 300
        var dy = dx / 2 + 100
        matrix.preRotate(5f)
        matrix.preTranslate(dx, dy)
        canvas.concat(matrix)
        matrix.reset()

        paint.textSize = 200f
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        canvas.drawText("Hello world!", 0f, 0f, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = Color.BLACK
        canvas.drawText("Hello world!", 0f, 0f, paint)

        canvas.restore()

        // draw 2
        canvas.save()
        frac = time / 2f % parent.width
        dx = parent.width - frac
        dy = frac / 2
        matrix.preRotate(-5f)
        matrix.preTranslate(dx, dy)
        canvas.concat(matrix)
        matrix.reset()

        paint.textSize = 200f
        paint.color = Color.CYAN
        paint.style = Paint.Style.FILL
        canvas.drawText("Hello world!", 0f, 0f, paint)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = Color.BLACK
        canvas.drawText("Hello world!", 0f, 0f, paint)

        canvas.restore()

        // draw 2
        canvas.save()
        camera.save()
        camera.rotateZ(20f)
        camera.rotateY(-20f)
        camera.getMatrix(matrix)
        camera.restore()
        canvas.concat(matrix)
        matrix.reset()

        paint.textSize = abs(2000 - time % 4000) / 10 + 50f
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = Color.BLACK
        canvas.drawText("你好世界！", -100f, 600f, paint)
        paint.style = Paint.Style.FILL
        paint.color = Color.YELLOW
        canvas.drawText("你好世界！", -100f, 600f, paint)

        canvas.restore()
    }
}