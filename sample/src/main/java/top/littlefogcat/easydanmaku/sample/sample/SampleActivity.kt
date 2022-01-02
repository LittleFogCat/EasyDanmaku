package top.littlefogcat.easydanmaku.sample.sample

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import top.littlefogcat.easydanmaku.Danmakus
import top.littlefogcat.easydanmaku.danmakus.DanmakuItem
import top.littlefogcat.easydanmaku.danmakus.views.Danmaku
import top.littlefogcat.easydanmaku.danmakus.views.RLDanmaku
import top.littlefogcat.easydanmaku.sample.DanmakuLoader
import top.littlefogcat.easydanmaku.sample.R
import top.littlefogcat.easydanmaku.sample.util.ScreenSize
import top.littlefogcat.easydanmaku.ui.DanmakuView
import top.littlefogcat.easydanmaku.util.EzLog
import top.littlefogcat.esus.view.util.Timing

class SampleActivity : AppCompatActivity() {

    internal val danmakuView: DanmakuView by lazy { findViewById(R.id.dmView) }
    internal val videoView: VideoView by lazy { findViewById(R.id.video) }
    internal val etMessage: EditText by lazy { findViewById(R.id.etSend) }
    internal val btnStartRandom: TextView by lazy { findViewById(R.id.btnStartRandom) }
    internal val btnSend: Button by lazy { findViewById(R.id.btnSend) }

    private val delegate = UIDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_activity)
        // Initialize views
        delegate.initializeUI()
        initVideoView()
        initDanmakuView()

        videoView.start()

        btnSend.setOnClickListener {
            send()
        }
    }

    private fun initVideoView() {
        videoView.setVideoPath("android.resource://$packageName/${R.raw.sample}")
        videoView.setOnCompletionListener {
            danmakuView.finish()
            videoView.visibility = View.GONE
        }
    }

    @Suppress("deprecation")
    private fun initDanmakuView() {
        val option = Danmakus.Options
        /* --- init options --- */
        option.scrollingDanmakuDuration = 10000
        option.pinnedDanmakuDuration = 5000
        option.antiCoverEnabled = true

        // Set base text size
        Danmakus.Globals.baseTextSize = ScreenSize.of(this).height / 17.2f

        // Show FPS
        danmakuView.setShowFPS(true)
        danmakuView.setShow(true)
        danmakuView.setActionOnFrame {
            /*
             * 重要：尽量避免在主线程使用getCurrentPosition()，其会阻塞当前线程，频繁使用会导致丢帧！
             * 这里仅作演示用。
             */
            Timing.startSection("GetCurrentPosition")
            val progress = videoView.currentPosition
            danmakuView.time = progress.toLong()
            val time = Timing.endSection() / 1000000
            if (time > 1) {
                EzLog.i("DanmakuView", "getCurrentPosition() takes a long time: ${time}ms")
            }
        }

        loadDanmakus()
    }

    // Normal
    private fun loadDanmakus() {
        val danmakus = DanmakuLoader.load(this, resources.openRawResource(R.raw.comments))
        danmakuView.setDanmakus(danmakus)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        delegate.hideImeAndSystemBars()
    }

//    override fun onBackPressed() {
//        delegate.hideImeAndSystemBars()
//        etMessage.clearFocus()
//    }

    // Send a danmaku with an avatar
    private fun send() {
        val text = "This is a danmaku"
        val avatar = ResourcesCompat.getDrawable(resources, R.drawable.head, theme)
        val item = DanmakuItem(text, 0, Danmaku.TYPE_RL, Color.WHITE, Danmakus.Constants.PRIORITY_MAX)
        val danmaku = RLDanmaku(item)
        danmaku.drawableLeft = avatar
        danmakuView.sendDanmaku(danmaku)
    }

}