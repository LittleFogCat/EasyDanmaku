package top.littlefogcat.danmakulib.danmaku;

/**
 * Created by LittleFogCat.
 */
public interface Pool<T> {
    T get();

    void release();

    int count();
}
