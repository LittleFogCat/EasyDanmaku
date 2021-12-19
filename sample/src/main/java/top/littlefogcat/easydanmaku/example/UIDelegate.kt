package top.littlefogcat.easydanmaku.example

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import top.littlefogcat.easydanmaku.danmaku.Danmaku
import top.littlefogcat.easydanmaku.danmaku.DanmakuItem
import top.littlefogcat.easydanmaku.danmaku.RLDanmaku
import top.littlefogcat.easydanmaku.ui.DanmakuView
import top.littlefogcat.easydanmaku.util.TAG

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class UIDelegate(private val activity: SampleActivity) {

    private var btnStartRandom: TextView
        get() = activity.btnStartRandom
        set(value) {
            activity.btnStartRandom = value
        }
    private var etMessage: EditText
        get() = activity.etMessage
        set(value) {
            activity.etMessage = value
        }
    private var videoView: VideoView
        get() = activity.videoView
        set(value) {
            activity.videoView = value
        }
    private var danmakuView: DanmakuView
        get() = activity.danmakuView
        set(value) {
            activity.danmakuView = value
        }
    private lateinit var rootView: View
    private lateinit var bottomLayout: View
    private val handler = Handler(Looper.getMainLooper())
    private var isRandomSending: Boolean = false
    private val sendDanmakuTask = object : Runnable {
        override fun run() {
//            danmakuView.sendDanmaku(RLDanmaku("Hello world!", videoView.currentPosition))
//            handler.postDelayed(this, 200)
        }
    }

    fun initializeUI() {
        findViews()
        initialize()
    }

    private fun findViews() {
        activity.apply {
            videoView = findViewById(R.id.video)
            danmakuView = findViewById(R.id.dmView)
            btnStartRandom = findViewById(R.id.btnStartRandom)
            etMessage = findViewById(R.id.etSend)
            rootView = findViewById(R.id.root)
            bottomLayout = findViewById(R.id.bottomLayout)
        }
    }

    private fun initialize() {
        Log.d(TAG, "initialize: ")
        etMessage.setOnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                hideImeAndSystemBars()
            }
        }

//        rootView.setOnClickListener {
//            if (bottomLayout.visibility == View.VISIBLE) hideInteractive()
//            else showInteractive()
//        }

        etMessage.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == 100 || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE) {
                val text: String = etMessage.text.toString()
                val danmaku: Danmaku = RLDanmaku(
                    DanmakuItem(text, videoView.currentPosition, Danmaku.TYPE_RL, Color.WHITE, 0)
                )
                danmakuView.sendDanmaku(danmaku)
                hideImeAndSystemBars()
                etMessage.clearFocus()
                etMessage.setText("")
                true
            } else {
                false
            }
        }
        btnStartRandom.setOnClickListener {
            if (isRandomSending) {
                handler.removeCallbacks(sendDanmakuTask)
                btnStartRandom.text = "开始随机发送弹幕"
                isRandomSending = false
            } else {
                handler.post(sendDanmakuTask)
                btnStartRandom.text = "停止"
                isRandomSending = true
            }
        }
    }

    fun hideImeAndSystemBars() {
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etMessage.windowToken, 0)
        hideSystemBars()
    }

    private fun hideSystemBars() {
        activity.window.apply {
            navigationBarColor = Color.TRANSPARENT
            statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun showInteractive() {
        if (bottomLayout.animation != null && !bottomLayout.animation.hasEnded() || bottomLayout.visibility == View.VISIBLE) {
            return
        }
        etMessage.isEnabled = true
        AlphaAnimation(0f, 1f).apply {
            duration = 300
            fillAfter = true
            bottomLayout.startAnimation(this)
            btnStartRandom.startAnimation(this)
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    bottomLayout.visibility = View.VISIBLE
                    btnStartRandom.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
    }

    private fun hideInteractive() {
        Log.d(TAG, "hideInteractive: ")
        if (bottomLayout.animation != null && !bottomLayout.animation.hasEnded() || bottomLayout.visibility == View.GONE) {
            return
        }
        if (bottomLayout.visibility == View.VISIBLE) {
            AlphaAnimation(1f, 0f).apply {
                duration = 300
                fillAfter = true
                bottomLayout.startAnimation(this)
                btnStartRandom.startAnimation(this)
                etMessage.isEnabled = false
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        bottomLayout.visibility = View.GONE
                        btnStartRandom.visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }
                })
            }
        }
    }

}