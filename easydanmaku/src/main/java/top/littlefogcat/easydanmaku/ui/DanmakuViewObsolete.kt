package top.littlefogcat.easydanmaku.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.MediaController
import top.littlefogcat.easydanmaku.Danmakus
import top.littlefogcat.easydanmaku.R
import top.littlefogcat.easydanmaku.danmaku.Danmaku
import top.littlefogcat.easydanmaku.danmaku.DanmakuItem
import top.littlefogcat.easydanmaku.danmaku.RLDanmaku
import top.littlefogcat.easydanmaku.util.Progress
import top.littlefogcat.esus.view.ISurface
import top.littlefogcat.esus.view.View
import top.littlefogcat.esus.view.ViewRootImpl
import java.util.*

/**
 * # DanmakuView
 *
 * `DanmakuView` is the UI widget containing danmakus.
 *
 * ## Initial danmakus data
 *
 * Use [setData] to initial danmakus in this DanmakuView.
 * This function accepts one [Collection<Danmaku>] parameter. Make sure the collection is sorted by [Danmaku.time]
 * in ascending.
 *
 * ## Attach a media player
 *
 * In order to know the progress of the playing media, one of [setGetProgressAction] and [attachTo] must be called.
 * If none of them are invoked, the progress will stop at 0, and danmakus won't be updated.
 *
 * ## Sample
 * ```kotlin
 * val videoView = findView()
 * val danmakuView = findView()
 *
 * // equals to: danmakuView.attach(videoView)
 * danmakuView.setGetProgressAction { videoView.currentPosition }
 * danmakuView.setData(getData())
 *
 * videoView.setVideoPath(videoUrl)
 * videoView.start()
 * ```
 *
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
@Deprecated("No longer used", ReplaceWith("DanmakuView"))
class DanmakuViewObsolete : SurfaceView, SurfaceHolder.Callback, IDanmakuView, ISurface {

    private val option = Danmakus.Options

    private lateinit var viewRootImpl: ViewRootImpl
    private val container = DanmakuContainer()
    private val progress: Progress = Progress()
    private var showFps = false
    private var running = false

    override var time: Int = 0
    override var w: Int = 0
    override var h: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DanmakuView)
        val antiCoverEnabled = a.getBoolean(R.styleable.DanmakuView_antiCoverEnabled, true)
        val showFPS = a.getBoolean(R.styleable.DanmakuView_showFps, false)
        a.recycle()

        option.antiCoverEnabled = antiCoverEnabled
        option.showFPS = showFPS
    }

    init {
        setZOrderMediaOverlay(true)
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSLUCENT)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        viewRootImpl = ViewRootImpl(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        w = width
        h = height
        viewRootImpl.setView(container)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        viewRootImpl.die()
        progress.terminal()
    }

    override fun setDanmakus(danmakus: Collection<DanmakuItem>) {
        container.setDanmakus(danmakus)
    }

    override fun setShowFPS(show: Boolean) {
        showFps = show
    }

    override fun setShow(show: Boolean) {
        val v = if (show) View.VISIBLE else View.INVISIBLE
        container.setVisibility(v)
    }

    override fun addDanmaku(danmaku: Danmaku) {
        container.addDanmaku(danmaku)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun pause(danmaku: Danmaku) {
        danmaku.pause()
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun resume(danmaku: Danmaku) {
        TODO("Not yet implemented")
    }

    override fun attachTo(control: MediaController.MediaPlayerControl) {
        progress.setGetProgressAction { control.currentPosition }
    }

    override fun setGetProgressAction(action: () -> Int) {
        progress.setGetProgressAction(action)
    }

    override fun setActionOnFrame(action: ((Long) -> Unit)?) {
        TODO("Not yet implemented")
    }

    override fun setOnDanmakuClickListener(action: (Danmaku) -> Boolean) {
        container.setOnDanmakuClickListener(action)
    }

    override fun setDanmakuOpacity(opacity: Float) {
        TODO("Not yet implemented")
    }

    override fun setDisplayArea(area: Float) {
        TODO("Not yet implemented")
    }

    override fun setDisplayArea(area: Rect) {
        TODO("Not yet implemented")
    }

    override fun setScrollDuration(duration: Int) {
        TODO("Not yet implemented")
    }

    override fun setPinnedDuration(duration: Int) {
        TODO("Not yet implemented")
    }

    override fun setTextScale(scale: Float) {
        TODO("Not yet implemented")
    }

}