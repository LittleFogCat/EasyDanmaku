package top.littlefogcat.danmakulib.danmaku;

import android.os.SystemClock;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;

import top.littlefogcat.danmakulib.utils.RandomUtil;

/**
 * Created by jjy on 2018/6/6.
 * <p>
 * 使用方法：<br/>
 * IDanmakuManager idm = DanmakuManager.getInstance(rootView);<br/>
 * idm.show(danmaku);
 */

public class DanmakuManager implements IDanmakuManager {
    private static final String TAG = "DanmakuManager";
    private static DanmakuManager sInstance;
    private SoftReference<ViewGroup> mRootView;
    private Settings mSettings;
    private DanmakuViewPool mPool; // 弹幕view池，用于复用

    private DanmakuManager() {
    }

    public static IDanmakuManager getInstance() {
        if (sInstance == null) {
            sInstance = new DanmakuManager();
        }
        return sInstance;
    }

    @Deprecated
    public static IDanmakuManager newInstance(ViewGroup rootView) {
        DanmakuManager manager = new DanmakuManager();
        manager.setRootView(rootView);
        return manager;
    }

    @Override
    public void setRootView(ViewGroup rootView) {
        mRootView = new SoftReference<>(rootView);
        mPool = DanmakuViewPools.newCachedDanmakuViewPool(rootView.getContext());
        mSettings = new Settings();
    }

    @Override
    public void show(Danmaku danmaku) {
        DanmakuView view = mPool.get();
        if (view == null) {
            Log.w(TAG, "show: Too many danmaku, discard");
            return;
        }
        if (mRootView.get() == null) {
            Log.w(TAG, "show: Root view is null. Didn't call setRootView() or root view has been recycled.");
            return;
        }
        int y = getYEnsureNoOverlapping();
        int duration = RandomUtil.randomInt(12000, 18000);
        Log.d(TAG, "show: " + danmaku);
        view.init(mRootView.get(), danmaku, y, duration);
        view.show();
    }

    private int mLastY = 0;
    private long mLastSend = SystemClock.currentThreadTimeMillis();

    private int getYEnsureNoOverlapping() {
        long currentMillis = SystemClock.currentThreadTimeMillis();
        int y;
        if (currentMillis - mLastSend < 6000) {
            y = RandomUtil.randomIntExcept(0, mSettings.getMaxLine(), mLastY);// 随机行数
        } else {
            y = RandomUtil.randomInt(0, mSettings.getMaxLine());// 随机行数
        }
        mLastY = y;
        mLastSend = currentMillis;
        return y;
    }

    @Override
    public Settings getSettings() {
        return mSettings;
    }

}
