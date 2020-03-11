package top.littlefogcat.danmakulib.utils;

import android.util.Log;

/**
 * Created by LittleFogCat.
 */
public class L {
    private static boolean sEnabled = true;

    public static void setEnabled(boolean enabled) {
        sEnabled = enabled;
    }

    public static void v(String tag, String msg) {
        if (sEnabled) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sEnabled) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sEnabled) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sEnabled) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sEnabled) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, Object... args) {
        if (sEnabled) {
            StringBuilder msg = new StringBuilder();
            for (Object arg : args) {
                msg.append(arg).append(" ");
            }
            Log.d(tag, msg.toString());
        }
    }
}
