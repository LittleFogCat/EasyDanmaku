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
     *
     * @param danmaku 弹幕
     * @return Success: {@link DanmakuConstant#RESULT_OK},
     * The DanmakuPool is Full: {@link DanmakuConstant#RESULT_FULL_POOL},
     * The RootView is Null: {@link DanmakuConstant#RESULT_NULL_ROOT_VIEW}
     */
    int show(Danmaku danmaku);

    DanmakuConfig getConfig();
}
