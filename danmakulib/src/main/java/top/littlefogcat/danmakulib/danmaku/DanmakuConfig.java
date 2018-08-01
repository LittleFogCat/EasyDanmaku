package top.littlefogcat.danmakulib.danmaku;

import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.HEAD_CIRCLE;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.HEAD_SQUARE;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.MODE_IMG_TEXT;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.MODE_TEXT_ONLY;

public class DanmakuConfig {
    private static DanmakuConfig sConfig;

    int mHead = HEAD_CIRCLE;
    int mMode = MODE_TEXT_ONLY;

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

}
