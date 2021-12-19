package top.littlefogcat.easydanmaku.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import top.littlefogcat.easydanmaku.example.plain.EsusActivity

class MainActivity : AppCompatActivity() {
    lateinit var btnExample: Button
    lateinit var btnPressure: Button
    lateinit var seekBar: SeekBar
    lateinit var tvPressure: TextView
    lateinit var cbStroke: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnExample = findViewById(R.id.btnExample)
        btnPressure = findViewById(R.id.btnPressure)
        seekBar = findViewById(R.id.seekBar)
        tvPressure = findViewById(R.id.tvPressure)
        cbStroke = findViewById(R.id.cbStroke)

        btnExample.setOnClickListener {
            startActivity(Intent(this, SampleActivity::class.java))
        }

        btnPressure.setOnClickListener {
            val size = f(seekBar.progress).toInt()
            val i = Intent(this, EsusActivity::class.java)
            i.putExtra("size", size)
            i.putExtra("stroke", cbStroke.isChecked)
            startActivity(i)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvPressure.text = "size: " + f(progress).toInt()
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