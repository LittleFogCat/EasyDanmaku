package top.littlefogcat.danmakulib.danmaku;

/**
 * 一个对象池
 * Created by LittleFogCat.
 * @deprecated Do not use this module
 */
public interface Pool<T> {
    /**
     * 从缓存中获取一个T的实例
     */
    T get();

    /**
     * 释放缓存
     */
    void release();

    /**
     * @return 缓存中T实例的数量
     */
    int count();

    void setMaxSize(int max);
}
