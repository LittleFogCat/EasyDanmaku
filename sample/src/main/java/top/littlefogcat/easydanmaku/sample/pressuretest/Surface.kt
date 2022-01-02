package top.littlefogcat.easydanmaku.sample.pressuretest

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import top.littlefogcat.esus.EsusSurfaceView
import top.littlefogcat.esus.view.TouchEvent
import top.littlefogcat.esus.view.ViewGroup
import kotlin.random.Random

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class Surface : EsusSurfaceView {

    override var rootView: ViewGroup = RootView()
    private var size = -1
    private var stroke = false
    private var rootViewSet = false
    private var added = false

    /**
     * 开启遮罩，可实现人像防挡，但需要关闭硬件加速
     */
    var enableAntiMask = false
        set(enable) {
            setLayerType(if (enable) LAYER_TYPE_SOFTWARE else LAYER_TYPE_HARDWARE, null)
            field = enable
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        super.surfaceChanged(holder, format, width, height)
        if (!added && size != -1) {
            generateViewsInner()
        } else {
            rootViewSet = true
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val ev = TouchEvent()
        ev.x = event.x
        ev.y = event.y
        ev.actionType = event.action
        val result = rootView.dispatchTouchEvent(ev)
        return result || super.dispatchTouchEvent(event)
    }

    fun generateViews(size: Int, stroke: Boolean) {
        this.size = size
        this.stroke = stroke
        if (rootViewSet) {
            generateViewsInner()
        }
    }

    private fun generateViewsInner() {
        val r = Random.Default
        repeat(size) {
            val view = ScrollingTextView(
                "Hello World",
                r.nextInt(-300, 1800),
                r.nextInt(-30, height - 88)
            )
            rootView.addView(view.also {
                if (stroke) {
                    it.setStrokeEnabled(stroke)
                }
                it.textColor = when (r.nextInt() % 10) {
                    1, 2 -> Color.RED
                    3 -> Color.BLUE
                    4 -> Color.GREEN
                    5 -> Color.GRAY
                    6, 7 -> Color.YELLOW
                    else -> Color.WHITE
                }
            })
        }
        rootView.addView(SpecialView())
        added = true
    }
}