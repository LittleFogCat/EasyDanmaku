package top.littlefogcat.danmakulib.utils

import android.util.Log

/**
 * Created by LittleFogCat.
 */
object EasyL {
    private var sEnabled = false

    @JvmStatic
    fun setEnabled(enabled: Boolean) {
        sEnabled = enabled
    }

    @JvmStatic
    fun v(tag: String, msg: String) {
        if (sEnabled) {
            Log.v(tag, msg)
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (sEnabled) {
            Log.d(tag, msg)
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (sEnabled) {
            Log.i(tag, msg)
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (sEnabled) {
            Log.w(tag, msg)
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (sEnabled) {
            Log.e(tag, msg)
        }
    }

    @JvmStatic
    fun d(tag: String, vararg args: Any?) {
        if (sEnabled) {
            val msg = StringBuilder()
            for (arg in args) {
                msg.append(arg).append(" ")
            }
            Log.d(tag, msg.toString())
        }
    }
}