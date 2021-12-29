package top.littlefogcat.easydanmaku.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.SystemClock
import top.littlefogcat.easydanmaku.Danmakus
import top.littlefogcat.easydanmaku.danmakus.DanmakuItem
import top.littlefogcat.easydanmaku.danmakus.DanmakuPools
import top.littlefogcat.easydanmaku.danmakus.views.*
import top.littlefogcat.easydanmaku.util.IntervalList
import top.littlefogcat.esus.view.View
import top.littlefogcat.esus.view.ViewGroup
import top.littlefogcat.esus.view.ViewParent
import top.littlefogcat.esus.view.util.FPS
import top.littlefogcat.esus.widget.TextView

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class DanmakuContainer : ViewGroup() {
    private val resolver: DanmakuResolver = DanmakuResolverImpl()
    private val locator: DanmakuLocator<Danmaku> = DanmakuLocatorImpl()

    private var onDanmakuClickListener: ((Danmaku) -> Boolean)? = null

    internal val fpsView: TextView = TextView()
    private val fps = FPS()

    private var discarded = 0

    init {
        fpsView.apply {
            textColor = Color.RED
            backgroundColor = Color.parseColor("#66000000")
            paddingLeft = 20
            paddingRight = 20
        }
        addView(fpsView)
    }

    fun setDanmakus(danmakus: Collection<DanmakuItem>) {
        resolver.setData(danmakus)
    }

    fun addDanmaku(danmaku: Danmaku) {
        addView(danmaku)
    }

    fun setOnDanmakuClickListener(l: (Danmaku) -> Boolean) {
        onDanmakuClickListener = l
    }

    /* ===================== Override functions ===================== */

    override fun onViewAdded(view: View) {
        if (onDanmakuClickListener != null) {
            view.setOnClickListener(object : OnClickListener {
                override fun onClick(view: View) {
                    onDanmakuClickListener?.invoke(view as Danmaku)
                }
            })
        }
    }

    override fun onViewRemoved(view: View) {
        view.removeOnClickListener()
        if (view is Danmaku) {
            locator.release(view)
            DanmakuPools.ofType(view.type).release(view)
        }
    }

    override fun onMeasure(width: Int, height: Int) {
        super.onMeasure(width, height)
        fpsView.translationX = 100f
        fpsView.translationY = measuredHeight - fpsView.measuredHeight - 30f
    }

    override fun onLayout(l: Int, t: Int, r: Int, b: Int) {
        if (needLayout || attachInfo?.forceLayout == true) {
            allChildren { child ->
                if (child is Danmaku) {
                    if (child.needLayout) {
                        val success = locator.locate(this, child)
                        if (!success) {
                            child.needLayout = false
                            removeView(child)
                            discarded++
                        } else {
                            child.setVisibility(VISIBLE)
                        }
                    }
                } else {
                    child.layout(0, 0, child.measuredWidth, child.measuredHeight)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        // drawFPS
        if (fpsView.isVisible) {
            fps.onFrame(SystemClock.elapsedRealtimeNanos())
            fpsView.text = " FPS=${fps},time=${String.format("%.1f", time / 1000f)}s," +
                    "danmakus=$childCount,pool=${DanmakuPools.size},discard=$discarded "
        }
    }

    override fun afterDraw(canvas: Canvas, parent: ViewParent?, time: Int) {
        val newDanmakus = resolver.retrieve(time)
        if (!newDanmakus.isEmpty()) {
//            EzLog.d(TAG, "$time, newDanmakus: $newDanmakus")
        }
        newDanmakus.forEach {
            // 从池中取一个view
            val pool = DanmakuPools.ofType(it.type)
            val view = pool.acquire()
            view.setVisibility(GONE)
            view.item = it
            addView(view)
        }
    }
}

/* ===================== Locators ===================== */

/**
 * Danmaku Locators
 *
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */

interface ILocator<in VG : ViewGroup, in V : View> {
    fun locate(parent: VG, view: V): Boolean {
        throw UnsupportedOperationException()
    }

    fun release(view: V)
}

object DanmakuLocators {
    fun getLocator(type: Int): DanmakuLocator<Danmaku> {
        return when (type) {
            Danmaku.TYPE_RL -> rlLocator
            Danmaku.TYPE_LR -> lrLocator
            Danmaku.TYPE_TOP -> topLocator
            Danmaku.TYPE_BOTTOM -> bottomLocator
            else -> throw NoSuchElementException()
        } as DanmakuLocator<Danmaku>
    }

    private val rlLocator = RLLocator()
    private val lrLocator = LRLocator()
    private val topLocator = TopLocator()
    private val bottomLocator = BottomLocator()
}

interface DanmakuLocator<D : Danmaku> : ILocator<DanmakuContainer, D> {
    /**
     * Locates the [danmaku].
     * If success, `danmaku.layout(Int, Int)` should be called.
     */
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun locate(container: DanmakuContainer, danmaku: D): Boolean {
        throw UnsupportedOperationException()
    }

    override fun release(view: D) {}
}

class DanmakuLocatorImpl<T : Danmaku> : DanmakuLocator<T> {

    /**
     * Locates the specified danmaku in display. Returns if there is enough room to show this danmaku.
     */
    override fun locate(container: DanmakuContainer, danmaku: T): Boolean {
        val locator = try {
            DanmakuLocators.getLocator(danmaku.type)
        } catch (e: Exception) {
            return false
        }
        return locator.locate(container, danmaku)
    }

    override fun release(view: T) {
        val locator = try {
            DanmakuLocators.getLocator(view.type)
        } catch (e: Exception) {
            return
        }
        return locator.release(view)
    }
}

internal class RLLocator : DanmakuLocator<RLDanmaku> {
    private var tails: IntervalList<RLDanmaku>? = null

    override fun locate(container: DanmakuContainer, danmaku: RLDanmaku): Boolean {
        val maxHeight: Int = (Danmakus.Options.displayArea * container.height).toInt()
        val tails = this.tails ?: object : IntervalList<RLDanmaku>(0, maxHeight) {
            override fun isAcquirable(interval: Interval, danmaku: RLDanmaku): Boolean {
                return (interval.value ?: return true).let { current ->
                    current.entirelyDisplayTime <= danmaku.time &&
                            current.disappearTime <= danmaku.reachingEdgeTime
                }
            }
        }
        this.tails = tails
        if (tails.start != 0 || tails.end != maxHeight) {
            /* --- Range changes --- */
            tails.setRange(0, maxHeight)
        }
//        Log.d("Locator", "tails: $tails")
//        Log.d("Locator", "acquire: $danmaku")
        val interval = tails.acquire(danmaku)
        if (interval == null) {
            return false
        }
        val t = interval.start
        val b = interval.end
        val l = container.width
        val r = l + danmaku.measuredWidth
        var ex = false
        try {
//            EzLog.d("Locator", "top: $t, tails: $tails")
        } catch (e: Exception) {
            ex = true
        }
        if (ex) {
            var p = tails.first
            while (p != null) {
//                Log.w("Locator", "p = $p")
                p = p.next as IntervalList<RLDanmaku>.Interval?
            }
            throw Exception()
        }
        danmaku.layout(l, t, r, b)
        return true
    }

    override fun release(view: RLDanmaku) {
        tails?.release(view)
    }
}

internal class LRLocator : DanmakuLocator<LRDanmaku> {
    private var tails: IntervalList<LRDanmaku>? = null

    override fun locate(container: DanmakuContainer, danmaku: LRDanmaku): Boolean {
        val maxHeight: Int = (Danmakus.Options.displayArea * container.height).toInt()
        val tails = this.tails ?: object : IntervalList<LRDanmaku>(0, maxHeight) {
            override fun isAcquirable(interval: Interval, danmaku: LRDanmaku): Boolean {
                return (interval.value ?: return true).let { current ->
                    current.entirelyDisplayTime <= danmaku.time &&
                            current.disappearTime <= danmaku.reachingEdgeTime
                }
            }
        }
        if (this.tails == null) {
            this.tails = tails
        }
        if (tails.start != 0 || tails.end != maxHeight) {
            /* --- Range changes --- */
            tails.setRange(0, maxHeight)
        }
        val interval = tails.acquire(danmaku)
        if (interval == null) {
            return false
        }
        val t = interval.start
        val b = interval.end
        val l = -danmaku.measuredWidth
        val r = 0
        danmaku.layout(l, t, r, b)
        return true
    }

    override fun release(view: LRDanmaku) {
        tails?.release(view)
    }
}

internal class TopLocator : DanmakuLocator<TopPinnedDanmaku> {
    private var tails: IntervalList<TopPinnedDanmaku>? = null

    override fun locate(container: DanmakuContainer, danmaku: TopPinnedDanmaku): Boolean {
        val maxHeight: Int = (Danmakus.Options.displayArea * container.height).toInt()
        val tails = this.tails ?: object : IntervalList<TopPinnedDanmaku>(0, maxHeight) {
            override fun isAcquirable(interval: Interval, danmaku: TopPinnedDanmaku): Boolean {
                return interval.value == null
            }
        }
        if (this.tails == null) {
            this.tails = tails
        }
        if (tails.start != 0 || tails.end != maxHeight) {
            /* --- Range changes --- */
            tails.setRange(0, maxHeight)
        }
        val interval = tails.acquire(danmaku)
        if (interval == null) {
            return false
        }
        val t = interval.start
        val b = interval.end
        val l = container.width / 2 - danmaku.measuredWidth / 2
        val r = container.width / 2 + danmaku.measuredWidth / 2
        danmaku.layout(l, t, r, b)
        return true
    }

    override fun release(view: TopPinnedDanmaku) {
        tails?.release(view)
    }
}

internal class BottomLocator : DanmakuLocator<BottomPinnedDanmaku> {
    private var danmakus: Array<BottomPinnedDanmaku?>? = null

    override fun locate(container: DanmakuContainer, danmaku: BottomPinnedDanmaku): Boolean {
        val maxHeight: Int = (Danmakus.Options.displayArea * container.height).toInt()
        val maxLines = maxHeight / danmaku.measuredHeight
        if (danmakus == null || danmakus!!.size < maxLines) {
            val newDanmakus = Array(maxLines) { index ->
                if (danmakus != null && index < danmakus!!.size) {
                    danmakus!![index]
                } else {
                    null
                }
            }
            danmakus = newDanmakus
        }
        val danmakus = danmakus!!
        var index = -1
        for (i in danmakus.indices) {
            if (danmakus[i] == null) {
                index = i
                break
            }
        }
        if (index == -1) {
            return false
        }
        val b = container.height - index * danmaku.measuredHeight
        val t = b - danmaku.measuredHeight
        val l = container.width / 2 - danmaku.measuredWidth / 2
        val r = container.width / 2 + danmaku.measuredWidth / 2
        danmaku.layout(l, t, r, b)
        return true
    }

    override fun release(view: BottomPinnedDanmaku) {
        danmakus?.let {
            for (i in it.indices) {
                if (it[i] == view) {
                    it[i] = null
                    return
                }
            }
        }
    }

}