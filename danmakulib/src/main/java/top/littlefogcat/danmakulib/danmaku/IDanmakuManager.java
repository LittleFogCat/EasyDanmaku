package top.littlefogcat.danmakulib.danmaku;

import android.view.ViewGroup;

/**
 * Created by jjy on 2018/6/6.
 */

public interface IDanmakuManager {
    /**
     * 设置弹幕容器
     *
     * @param rootView father
     */
    void setRootView(ViewGroup rootView);

    /**
     * 显示一条弹幕
     */
    void show(Danmaku danmaku);

    Settings getSettings();

    class Settings {
        public int maxLine = 10;
        public int largeTextSizePx = 52;
        public int middleTextSizePx = 40;
        public int littleTextSizePx = 28;
    }
}
