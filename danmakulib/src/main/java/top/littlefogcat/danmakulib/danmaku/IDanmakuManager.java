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
        private int maxLine = 10;

        public int getMaxLine() {
            return maxLine;
        }

        public void setMaxLine(int maxLine) {
            this.maxLine = maxLine;
        }
    }

    default DanmakuConfig getConfig() {
        return DanmakuConfig.getConfig();
    }
}
