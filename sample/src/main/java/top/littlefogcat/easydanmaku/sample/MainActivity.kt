package top.littlefogcat.easydanmaku.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import top.littlefogcat.easydanmaku.sample.pressuretest.PressureTestActivity
import top.littlefogcat.easydanmaku.sample.sample.SampleActivity

class MainActivity : AppCompatActivity() {
    private val btnExample: Button by lazy { findViewById(R.id.btnExample) }
    private val btnPressure: Button by lazy { findViewById(R.id.btnPressure) }
    private val seekBar: SeekBar by lazy { findViewById(R.id.seekBar) }
    private val tvPressure: TextView by lazy { findViewById(R.id.tvPressure) }
    private val cbStroke: CheckBox by lazy { findViewById(R.id.cbStroke) }
    private val cbAntiMask: CheckBox by lazy { findViewById(R.id.cbAntiMask) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnExample.setOnClickListener {
            startActivity(Intent(this, SampleActivity::class.java))
        }

        btnPressure.setOnClickListener {
            val size = f(seekBar.progress).toInt()
            val i = Intent(this, PressureTestActivity::class.java)
            i.putExtra("size", size)
            i.putExtra("stroke", cbStroke.isChecked)
            i.putExtra("antiMask", cbAntiMask.isChecked)
            startActivity(i)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val text = "size: " + f(progress).toInt()
                tvPressure.text = text
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun f(x: Int): Float {
        return x * x * x * x / 10002f + 1
    }
}