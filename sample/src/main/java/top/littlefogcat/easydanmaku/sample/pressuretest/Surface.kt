package top.littlefogcat.easydanmaku.sample.pressuretest

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import top.littlefogcat.easydanmaku.sample.GlobalValues
import top.littlefogcat.easydanmaku.sample.sample.SpecialView
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

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        if (GlobalValues.enableMask) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        GlobalValues.w = width
        GlobalValues.h = height
        super.surfaceChanged(holder, format, width, height)
        if (!added && size != -1) {
            addRandomViewInner()
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

    fun addRandomView(size: Int, stroke: Boolean) {
        this.size = size
        this.stroke = stroke
        if (rootViewSet) {
            addRandomViewInner()
        }
    }

    private fun addRandomViewInner() {
        rootView.addView(SpecialView())
        val r = Random.Default
        repeat(size) {
            val view = ScrollingTextView(
                "Hello World",
                r.nextInt(-300, 1800),
                r.nextInt(-30, GlobalValues.h - 88)
            )
            rootView.addView(view.apply {
                if (stroke) {
                    setStrokeEnabled(stroke)
                }
                textColor = when (r.nextInt() % 10) {
                    1, 2 -> Color.RED
                    3 -> Color.BLUE
                    4 -> Color.GREEN
                    5 -> Color.GRAY
                    6, 7 -> Color.YELLOW
                    else -> Color.WHITE
                }
            })
        }
        added = true
    }
}