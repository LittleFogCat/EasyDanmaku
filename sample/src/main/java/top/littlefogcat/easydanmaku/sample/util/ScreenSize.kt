package top.littlefogcat.easydanmaku.sample.util

import android.app.Activity
import android.util.DisplayMetrics

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class ScreenSize(val width: Int, val height: Int) {

    companion object {
        fun of(activity: Activity): ScreenSize {
            val m = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(m)
            return ScreenSize(m.widthPixels, m.heightPixels)
        }
    }
}