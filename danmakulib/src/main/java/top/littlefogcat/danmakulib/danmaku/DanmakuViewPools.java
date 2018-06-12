package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jjy on 2018/6/7.
 * <p>
 * 参照{@link java.util.concurrent.Executors}设计
 */

public class DanmakuViewPools {
    public static DanmakuViewPool newCachedDanmakuViewPool(Context context) {
        return new CachedDanmakuViewPool(context, 10, 100, new LinkedBlockingQueue<>(100));
    }
}
