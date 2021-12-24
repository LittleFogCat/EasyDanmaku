package top.littlefogcat.easydanmaku.danmakus.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import top.littlefogcat.easydanmaku.Danmakus
import top.littlefogcat.easydanmaku.danmakus.DanmakuItem
import top.littlefogcat.esus.view.ViewGroup
import top.littlefogcat.esus.view.ViewParent
import top.littlefogcat.esus.widget.TextView

/**
 * 定义弹幕实体应该实现的功能。
 * Defines the functions that Danmaku entity should implement.
 *
 *
 * @param item Inherit properties of this danmaku.
 *
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
open class Danmaku(item: DanmakuItem? = null) : TextView() {

    companion object {
        const val TYPE_RL = 0
        const val TYPE_LR = 1
        const val TYPE_TOP = 2
        const val TYPE_BOTTOM = 3
        const val TYPE_ADVANCED = 4
        const val TYPE_DUMMY = 5
        const val TYPE_UNKNOWN = -1
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

        const val MAX_POOL_SIZE_LARGE = 20
        const val MAX_POOL_SIZE_MIDDLE = 10
        const val MAX_POOL_SIZE_SMALL = 5
    }

    /* ===================== item properties ===================== */

    var item: DanmakuItem? = item
        set(value) {
            if (value != null) {
                text = value.text
                textColor = value.color
                textSize = Danmakus.Globals.baseTextSize * Danmakus.Options.textScale * value.textScale
            }
            field = value
        }

    var time: Int
        get() = item?.time ?: 0
        set(value) {
            item?.time = value
        }
    var type: Int
        get() = item?.type ?: 0
        set(value) {
            item?.type = value
        }
    var color: Int
        get() = item?.color ?: 0
        set(value) {
            item?.color = value
        }
    var priority: Int
        get() = item?.priority ?: 0
        set(value) {
            item?.priority = value
        }
    var textScale: Float
        get() = item?.textScale ?: 0f
        set(value) {
            item?.textScale = value
        }
    val disappearTime: Int
        get() = time + duration

    /* ===================== declared members ===================== */

    override val paint: TextPaint = Danmakus.Globals.paint

    /** use for danmaku pool **/
    var next: Danmaku? = null @JvmName("setNext") internal set
    open val duration = 5000
    var isPaused = false
    var lastDrawingTime = -1

    /**
     * Indicates whether more transform should be done.
     * If not, this danmaku will be removed from parent.
     */
    private var more = false

    /**
     * Percent of 0~1
     */
    var process = 0f

    /* ===================== functions ===================== */

    override fun preDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        if (lastDrawingTime != -1 && !isPaused) {
            val diff = time - lastDrawingTime
            process += diff.toFloat() / duration
        }
        lastDrawingTime = time
        more = updateOnProcess(process)
    }

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        val boring = boring ?: return
        val x = 0f
        val y = -boring.ascent.toFloat()
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.textSize = textSize
        canvas.drawText(text, 0, text.length, x, y, paint)
        super.onDraw(canvas, parent, time)
        if (!more && parent is ViewGroup) {
            parent.removeView(this)
        }
    }

    override fun onDetached() {
        if (Danmakus.Options.recycle) {
            // should recycle
            reset()
        }
    }

    private fun reset() {
        process = 0f
        more = false
        next = null
        isPaused = false
        lastDrawingTime = -1
        flags = 0
        needLayout = true
        parent = null
        attachInfo = null
        animation = null
        matrix.reset()
        alpha = 1f
        backgroundColor = Color.TRANSPARENT
    }

    /**
     * Returns if it's still running.
     */
    open fun updateOnProcess(process: Float): Boolean {
        return process < 1
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    override fun toString(): String {
        return "{$text:$time}"
    }
}