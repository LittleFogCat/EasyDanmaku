package top.littlefogcat.easydanmaku.example.plain.views

import android.graphics.Canvas
import android.graphics.Color
import android.os.SystemClock
import top.littlefogcat.easydanmaku.danmaku.Danmaku
import top.littlefogcat.esus.view.View
import top.littlefogcat.esus.view.ViewParent
import top.littlefogcat.esus.view.util.FPS
import top.littlefogcat.esus.view.util.Timing
import top.littlefogcat.esus.widget.FrameLayout

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class RootView : FrameLayout() {
    private val fps = FPS()

    override fun onAttached(info: AttachInfo) {
        super.onAttached(info)
        backgroundColor = Color.BLACK
    }

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        super.onDraw(canvas, parent, time)
        fps.onFrame(SystemClock.elapsedRealtimeNanos())
    }

    override fun afterDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        val text = " FPS: ${fps}, current: $childCount, frame render: ${Timing.lastFrameTime}ms "

        val l = 100f
        val r = 100f + paint.measureText(text)
        val b = height - 50f
        val t = b - 65f

        paint.color = Color.parseColor("#88000000")
        canvas.drawRect(l - 40, t - 20, r + 40, b + 20, paint)

        paint.color = Color.RED
        paint.textSize = 65f
        canvas.drawText(text, l, b, paint)
    }
}