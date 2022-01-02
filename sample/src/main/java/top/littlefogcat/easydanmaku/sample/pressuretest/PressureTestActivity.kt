package top.littlefogcat.easydanmaku.sample.pressuretest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.littlefogcat.easydanmaku.sample.R
import top.littlefogcat.esus.view.util.Timing

/**
 * 使用EsusSurfaceView进行压力测试
 */
class PressureTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plain_activity)

        Timing.enable = true
        Timing.enableLog = false

        val size = intent.getIntExtra("size", 100)
        val stroke = intent.getBooleanExtra("stroke", false)
        val antiMask = intent.getBooleanExtra("antiMask", false)
        val sf = findViewById<Surface>(R.id.surface)
        sf.generateViews(size, stroke)
        sf.enableAntiMask = antiMask

        title = "${size}弹幕压力测试"
    }

}