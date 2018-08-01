package top.littlefogcat.danmakulib.danmaku;

import android.view.ViewGroup;

public interface DanmakuView {
    /**
     * 初始化DanmakuView
     *
     * @param rootView DanmakuView的容器，一般是一个FrameLayout
     * @param danmaku  显示的弹幕
     * @param y        显示在第几行
     * @param duration 显示的时间长度
     */
    void init(ViewGroup rootView, Danmaku danmaku, int y, int duration);

    /**
     * 显示弹幕
     */
    void show();

    /**
     * 添加弹幕进入时的监听
     */
    void addOnEnterListener(OnEnterListener listener);

    /**
     * 添加弹幕显示完毕的监听
     */
    void addOnExitListener(OnExitListener listener);

    /**
     * 设置DanmakuView的容器
     *
     * @param rootView 父布局，一般是一个FrameLayout
     */
    void setRootView(ViewGroup rootView);

    interface OnEnterListener {
        void onEnter();
    }

    interface OnExitListener {
        void onExit(DanmakuView view);
    }
}
