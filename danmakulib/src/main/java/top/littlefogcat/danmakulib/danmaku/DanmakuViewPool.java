package top.littlefogcat.danmakulib.danmaku;

/**
 * Created by jjy on 2018/6/7.
 */

public interface DanmakuViewPool {
    /**
     * 获取一个DanmakuView
     */
    DanmakuView get();

    /**
     * 释放资源
     */
    void release();

    /**
     * 返回DanmakuView的总数，包括使用中的和缓存的
     */
    int count();
}
