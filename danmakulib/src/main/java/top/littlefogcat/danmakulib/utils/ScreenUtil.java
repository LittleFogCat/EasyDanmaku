package top.littlefogcat.danmakulib.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by LittleFogCat.
 * <p>
 * 自动适配屏幕像素的工具类
 */

@SuppressWarnings({"unused", "WeakerAccess", "SuspiciousNameCombination"})
public class ScreenUtil {

    private static boolean sInit;
    /**
     * 屏幕宽度，在调用init()之后通过{@link ScreenUtil#getScreenWidth()}获取
     */
    private static int sScreenWidth = 1920;

    /**
     * 屏幕高度，在调用init()之后通过{@link ScreenUtil#getScreenHeight()} ()}获取
     */
    private static int sScreenHeight = 1080;

    /**
     * 设计宽度。用于{@link ScreenUtil#autoWidth(int)}
     */
    private static int sDesignWidth = 1080;

    /**
     * 设计高度。用于{@link ScreenUtil#autoHeight(int)} (int)}
     */
    private static int sDesignHeight = 1920;

    public static void init(Context context) {
        if (sInit) {
            return;
        }

        DisplayMetrics m = context.getResources().getDisplayMetrics();

        sScreenWidth = m.widthPixels;
        sScreenHeight = m.heightPixels;
        if ((sScreenWidth == 0 || sScreenHeight == 0) && sDesignWidth != 0 && sDesignHeight != 0) {
            sScreenWidth = sDesignWidth;
            sScreenHeight = sDesignHeight;
        }
        if (sScreenWidth > sScreenHeight && sDesignWidth < sDesignHeight) {
            int tmp = sDesignWidth;
            sDesignWidth = sDesignHeight;
            sDesignHeight = tmp;
        }
        sInit = true;
    }

    public static void setDesignWidthAndHeight(int width, int height) {
        sDesignWidth = width;
        sDesignHeight = height;
    }

    /**
     * 根据屏幕分辨率自适应宽度。
     *
     * @param origin 设计图中的宽度，像素
     * @return 实际屏幕中的宽度，像素
     */
    public static int autoWidth(int origin) {
        if (sScreenWidth == 0 || sDesignWidth == 0) {
            return origin;
        }
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
        if (sScreenHeight == 0 || sDesignHeight == 0) {
            return origin;
        }
        int auto = origin * sScreenHeight / sDesignHeight;
        if (origin != 0 && auto == 0) {
            return 1;
        }
        return auto;
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

    public static boolean isInit() {
        return sInit;
    }
}
