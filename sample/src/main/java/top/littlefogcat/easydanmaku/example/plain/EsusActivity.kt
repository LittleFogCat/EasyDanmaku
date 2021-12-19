package top.littlefogcat.easydanmaku.example.plain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import top.littlefogcat.easydanmaku.example.R
import top.littlefogcat.easydanmaku.example.plain.views.Surface
import top.littlefogcat.esus.view.util.Timing

/**
 * 使用EsusSurfaceView
 */
class EsusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plain_activity)

        Timing.enable = true
        Timing.enableLog = false

        val size = intent.getIntExtra("size", 100)
        val stroke = intent.getBooleanExtra("stroke", false)
        val sf = findViewById<Surface>(R.id.surface)
        sf.addRandomView(size, stroke)

        title = "${size}弹幕压力测试"
    }

}