package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import top.littlefogcat.danmakulib.utils.RandomUtil;
import top.littlefogcat.danmakulib.utils.ScreenUtil;

/**
 * Created by jjy on 2018/6/6.
 * <p>
 * 使用方法：<br/>
 * IDanmakuManager idm = DanmakuManager.getInstance(rootView);<br/>
 * idm.show(danmaku);
 */

public class DanmakuManager implements IDanmakuManager {
    private static final String TAG = "DanmakuManager";
    private ViewGroup mRootView;
    private Settings mSettings;
    private DanmakuViewPool mPool;

    private List<Integer> mLines = new ArrayList<>(); // 每一行弹幕的数量

    private DanmakuManager(Context context) {
        ScreenUtil.init(context, 1920, 1080);
        mPool = DanmakuViewPools.newCachedDanmakuViewPool(context);
        mSettings = new Settings();
    }

    public static IDanmakuManager newInstance(ViewGroup rootView) {
        DanmakuManager manager = new DanmakuManager(rootView.getContext());
        manager.setRootView(rootView);
        return manager;
    }

    @Override
    public void setRootView(ViewGroup rootView) {
        mRootView = rootView;
    }

    @Override
    public void show(Danmaku danmaku) {
        DanmakuView view = mPool.get();
        if (view == null) {
            return;
        }
        int y = RandomUtil.randomInt(0, mSettings.getMaxLine());
        int duration = RandomUtil.randomInt(12000, 18000);
        view.init(mRootView, danmaku, y, duration, null, null);
        view.start();
    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

    public void release() {
    }

}
