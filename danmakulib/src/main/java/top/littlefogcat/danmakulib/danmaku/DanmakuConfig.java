package top.littlefogcat.danmakulib.danmaku;

import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.DEFAULT_MAX_LINE;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.HEAD_CIRCLE;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.HEAD_SQUARE;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.MODE_IMG_TEXT;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.MODE_TEXT_ONLY;

public class DanmakuConfig {
    private static DanmakuConfig sConfig;

    int mHead = HEAD_CIRCLE;
    int mMode = MODE_TEXT_ONLY;
    int mMaxLine = DEFAULT_MAX_LINE;
    int designWidth = 1920;
    int designHeight = 1080;

    static DanmakuConfig getConfig() {
        if (sConfig == null) {
            sConfig = new DanmakuConfig();
        }
        return sConfig;
    }

    public DanmakuConfig useTextOnlyMode() {
        mMode = MODE_TEXT_ONLY;
        return this;
    }

    public DanmakuConfig useImgTextMode() {
        mMode = MODE_IMG_TEXT;
        return this;
    }

    public DanmakuConfig useCircleHead() {
        mHead = HEAD_CIRCLE;
        return this;
    }

    public DanmakuConfig useSquareHead() {
        mHead = HEAD_SQUARE;
        return this;
    }

    public DanmakuConfig setMaxLine(int line) {
        mMaxLine = line;
        return this;
    }

    public int getMaxLine() {
        return mMaxLine;
    }

    public DanmakuConfig setDesignSize(int w, int h) {
        designWidth = w;
        designHeight = h;
        return this;
    }
}
