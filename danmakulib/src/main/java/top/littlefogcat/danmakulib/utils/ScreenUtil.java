package top.littlefogcat.danmakulib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by jjy on 2018/6/6.
 * <p>
 * 自动适配屏幕像素的工具类
 */

public class ScreenUtil {
    private static final String TAG = "ScreenUtil";

    private static boolean sInit;
    private static int sScreenWidth;
    private static int sScreenHeight;
    private static int sDesignWidth;
    private static int sDesignHeight;

    /**
     * @param designW 设计图基准的宽度，如1920
     * @param designH 设计图基准的高度，如1080
     */
    public static void init(Context context, int designW, int designH) {
        if (sInit) {
            return;
        }
        sDesignWidth = designW;
        sDesignHeight = designH;

        DisplayMetrics m = context.getResources().getDisplayMetrics();

        sScreenWidth = m.widthPixels;
        sScreenHeight = m.heightPixels;

        Log.d(TAG, "init: " + sScreenWidth + ", " + sScreenHeight);

        if (sScreenWidth == 0 || sScreenHeight == 0) {
            sScreenWidth = designW;
            sScreenHeight = designH;
        }
        sInit = true;
    }

    /**
     * 根据屏幕分辨率自适应宽度
     *
     * @param origin 设计图中的宽度，像素
     * @return 实际屏幕中的宽度，像素
     */
    public static int autoWidth(int origin) {
        int autoSize = origin * sScreenWidth / sDesignWidth;
        if (origin != 0 && autoSize == 0) {
            return 1;
        }
        return autoSize;
    }

    /**
     * 根据屏幕分辨率自适应高度
     *
     * @param origin 设计图中的高度，像素
     * @return 实际屏幕中的高度，像素
     */
    public static int autoHeight(int origin) {
        return origin * sScreenHeight / sDesignHeight;
    }

    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static void setScreenWidth(int w) {
        sScreenWidth = w;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static void setScreenHeight(int h) {
        sScreenHeight = h;
    }
}
