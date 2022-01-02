package top.littlefogcat.easydanmaku.sample.pressuretest

import android.graphics.*
import android.os.SystemClock
import android.util.Log
import top.littlefogcat.easydanmaku.sample.R
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
    private var mask: Bitmap? = null
    private var rect: Rect? = null
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    override fun onAttached(info: AttachInfo) {
        super.onAttached(info)
        backgroundColor = Color.BLACK
        val context = info.context
        if (context != null) {
            mask = BitmapFactory.decodeResource(context.resources, R.drawable.mask)
        } else {
            Log.d(TAG, "onAttached: ${getSurface()}")
        }
    }

    override fun onLayout(l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(l, t, r, b)
        if (rect == null) {
            mask?.let { mask ->
                Log.d(TAG, "retrieve bitmap size")
                Log.d(TAG, "source size = ${mask.width}x${mask.height}")
                val scale = (height - 200f) / mask.height
                val w = mask.width * scale
                Log.d(TAG, "scaled size = ${w}x${height}")
                rect = Rect((width / 2 - w / 2).toInt(), 100, (width / 2 + w / 2).toInt(), height - 100)
            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Long) {
        super.onDraw(canvas, parent, time)
        fps.onFrame(SystemClock.elapsedRealtimeNanos())
    }

    override fun drawForeground(canvas: Canvas) {
        super.drawForeground(canvas)
        val surface = attachInfo?.viewRootImpl?.surface
        if (surface != null && (surface as Surface).enableAntiMask && rect != null && mask != null) {
            drawMask(canvas)
        }
    }

    fun drawMask(canvas: Canvas) {
        val mode = paint.xfermode
        paint.xfermode = xfermode
        canvas.drawBitmap(mask!!, null, rect!!, paint)
        paint.xfermode = mode
    }

    override fun afterDraw(canvas: Canvas, parent: ViewParent?, time: Long) {
        val text = " FPS: ${fps}, current: $childCount, frameTime: ${Timing.lastFrameTime}ms "

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