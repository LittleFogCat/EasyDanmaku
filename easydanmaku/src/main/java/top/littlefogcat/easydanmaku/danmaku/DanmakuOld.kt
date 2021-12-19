package top.littlefogcat.easydanmaku.danmaku

import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import androidx.core.util.Pools
import top.littlefogcat.easydanmaku.Danmakus
import top.littlefogcat.esus.view.View
import top.littlefogcat.esus.view.ViewGroup
import top.littlefogcat.esus.view.ViewParent

/**
 * 定义弹幕实体应该实现的功能。
 * Defines the functions that Danmaku entity should implement.
 *
 * 字体大小是根据3个参数决定的：
 * 1. [textScale]：弹幕本身缩放比例，为固定值，在发送时确定，普通弹幕为1；
 * 2. [Danmakus.Global.baseTextSize]：基准字体大小，该参数由应用根据实际情况决定；
 * 3. [Danmakus.Options.textScale]：该参数为用户设定缩放值，默认为1；
 *
 * 最终字体大小为以上3个参数相乘。
 *
 * @param textScale Inherit text scale.
 *
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
abstract class DanmakuOld(
    open var text: String,
    open var time: Int,
    open var type: Int,
    open var color: Int,
    open var priority: Int,
    open var textScale: Float = 1f
) : View() {
    companion object {
        const val TYPE_RL = 0
        const val TYPE_LR = 1
        const val TYPE_TOP = 2
        const val TYPE_BOTTOM = 3
        const val TYPE_ADVANCED = 4
        const val TYPE_DUMMY = 5
        private const val TYPE_COUNT = 6

        const val STATE_PENDING = 0
        const val STATE_SHOWING = 1
        const val STATE_INVISIBLE = 2
        const val STATE_DISCARDED = 2
        const val STATE_HIDDEN = 3

        const val FLAG_MEASURED = 0x00000001
        const val FLAG_LOCATED = 0x00000002
        const val FLAG_DRAWN = 0x00000004
        const val FLAG_ANIMATING = 0x00000008
        const val FLAG_PREPARED = 0x00000010
        const val FLAG_SHOWING = 0x00010000
        const val FLAG_PAUSED = 0x00020000
        const val FLAG_DISCARDED = 0x00040000

        private const val MAX_POOL_SIZE_LARGE = 20
        private const val MAX_POOL_SIZE_MIDDLE = 10
        private const val MAX_POOL_SIZE_SMALL = 5

        private class Pool(type: Int) : Pools.Pool<DanmakuOld> {
            private var head: DanmakuOld? = null
            var size = 0
            private val maxPoolSize = when (type) {
                TYPE_RL, TYPE_LR -> MAX_POOL_SIZE_LARGE
                TYPE_TOP, TYPE_BOTTOM -> MAX_POOL_SIZE_MIDDLE
                else -> MAX_POOL_SIZE_SMALL
            }

            override fun acquire(): DanmakuOld {
                if (size == 0) {
                    throw NoSuchElementException()
                }
                val danmaku = head
                if (danmaku == null) {
                    throw NullPointerException()
                } else {
                    head = danmaku.next
                    size--
                    return danmaku
                }
            }

            override fun release(instance: DanmakuOld): Boolean {
                if (size >= maxPoolSize) {
                    return false
                }
                instance.next = head
                head = instance
                size++
                return true
            }

        }

        private var pool: Array<Pool> = Array(TYPE_COUNT) { Pool(it) }
//
//        fun obtain(
//            text: String = "",
//            time: Int = 0,
//            type: Int = TYPE_DUMMY,
//            color: Int = Color.WHITE,
//            priority: Int = 0,
//            textScale: Float = 1f
//        ): DanmakuOld {
//            if (type < TYPE_COUNT && pool[type].size != 0) {
//                val danmaku = pool[type].acquire()
//                danmaku.text = text
//                danmaku.time = time
//                danmaku.color = color
//                danmaku.priority = priority
//                danmaku.textScale = textScale
//                return danmaku
//            }
//            return when (type) {
//                TYPE_RL -> RLDanmaku(text, time, color, priority, textScale)
//                TYPE_LR -> LRDanmaku(text, time, color, priority, textScale)
//                TYPE_TOP -> TopPinnedDanmaku(text, time, color, priority, textScale)
//                TYPE_BOTTOM -> BottomPinnedDanmaku(text, time, color, priority, textScale)
//                TYPE_ADVANCED -> AdvancedDanmaku(text, time, color, priority, textScale)
//                else -> DummyDanmaku()
//            }
//        }
    }

    /* ===================== declared members ===================== */

    override val paint get() = Danmakus.Global.paint
    internal val strokePaint get() = Danmakus.Global.strokePaint
    abstract val duration: Int
    private var next: DanmakuOld? = null
    override var paddingTop: Int = 10
    override var paddingBottom: Int = 10
    private var pauseTime = 0
    internal var pausedTime = 0

    /* ===================== calculated values ===================== */

    val textSize: Float
        get() = Danmakus.Global.baseTextSize * Danmakus.Options.textScale * textScale

    val disappearTime: Int
        get() = time + duration
    val isPrepared: Boolean
        get() = hasFlag(FLAG_PREPARED)

    /**
     * Indicates whether this danmaku is showing.
     */
    val isShowing: Boolean
        get() = hasFlag(FLAG_SHOWING)

    /**
     * Indicate whether this danmaku is showing but paused.
     */
    var isPaused: Boolean = false

    /**
     * Indicate whether this danmaku is discarded because of no room for display.
     */
    val isDiscarded: Boolean
        get() = hasFlag(FLAG_DISCARDED)

    /* ===================== declared functions ===================== */

    private fun hasFlag(flag: Int): Boolean {
        return flags and flag != 0
    }

    fun pause() {
        if (!isPaused) {
            isPaused = true
            if (attachInfo != null) {
                pauseTime = attachInfo!!.drawingTime
            }
        }
    }

    fun resume() {
        if (isPaused) {
            isPaused = false
            if (attachInfo != null) {
                pausedTime += attachInfo!!.drawingTime - pauseTime
            }
        }
    }

    /* ===================== Override functions ===================== */

    override fun onMeasure(width: Int, height: Int) {
        val textSize = textSize
        paint.textSize = textSize
        setMeasuredDimensions(
            paint.measureText(text).toInt() + paddingLeft + paddingRight,
            textSize.toInt() + paddingTop + paddingBottom
        )
    }

    /**
     * 在此绘制。
     * 如果只需要实现位置变换，则重写[doTransform]，不重写这个方法。
     */
    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        super.onDraw(canvas, parent, time)
        if (time < this.time || time >= disappearTime) {
            /* --- outside --- */
            Log.d(TAG, "onDraw: 1")
            return
        }
        if (!isShowing) {
            Log.d(TAG, "onDraw: 2")
            return
        }
        Log.i(TAG, "onDraw: 3")
        val more = doTransform(time)
        paint.textSize = textSize
        paint.color = color
        canvas.drawText(text, x, y + height, paint)
        strokePaint.textSize = textSize
        canvas.drawText(text, x, y + height, strokePaint)

        if (!more && parent is ViewGroup) {
            parent.removeView(this)
        }
    }

    /**
     * 因为只有一个canvas，无法进行变换，所以直接在这里进行动画。
     * 如果不使用默认效果，则重写[onDraw]
     *
     * @return 动画是否还需要继续进行
     */
    open fun doTransform(time: Int): Boolean {
        val ai = attachInfo ?: return false
        return ai.drawingTime < disappearTime
    }

    override fun onDetached() {
        super.onDetached()
        recycle()
    }

    open fun recycle() {
        if (Danmakus.Options.recycle) {
            isPaused = false
            alpha = 1f
            animation = null
            parent = null
            attachInfo = null
            pool[type].release(this)
            setPaddings(0, 0, 0, 0)
        }
    }

    override fun toString(): String {
        return "{$text:$time}"
    }
}