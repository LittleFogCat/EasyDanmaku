package top.littlefogcat.danmakulib.danmaku;

import android.os.SystemClock;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;

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
    private static DanmakuManager sInstance;
    private SoftReference<ViewGroup> mRootView;
    private DanmakuViewPool mPool; // 弹幕view池，用于复用

    private DanmakuManager() {
    }

    public static IDanmakuManager getInstance() {
        if (sInstance == null) {
            sInstance = new DanmakuManager();
        }
        return sInstance;
    }

    /**
     * @param rootView father
     */
    @Override
    public void setRootView(ViewGroup rootView) {
        mRootView = new SoftReference<>(rootView);
        mPool = DanmakuViewPools.newCachedDanmakuViewPool(rootView.getContext());
        // fixme
        if (!ScreenUtil.isInit()) {
            ScreenUtil.init(rootView.getContext(), getConfig().designWidth, getConfig().designHeight);
        }
    }

    @Override
    public DanmakuConfig getConfig() {
        return DanmakuConfig.getConfig();
    }

    @Override
    public int show(Danmaku danmaku) {
        DanmakuView view = mPool.get();
        if (view == null) {
            Log.w(TAG, "show: Too many danmaku, discard");
            return DanmakuConstant.RESULT_FULL_POOL;
        }
        if (mRootView.get() == null) {
            Log.w(TAG, "show: Root view is null. Didn't call setRootView() or root view has been recycled.");
            return DanmakuConstant.RESULT_NULL_ROOT_VIEW;
        }
        int y = getYEnsureNoOverlapping();
        int duration = RandomUtil.randomInt(12000, 18000);
        Log.d(TAG, "show: " + danmaku);
        view.init(mRootView.get(), danmaku, y, duration);
        view.show();
        return DanmakuConstant.RESULT_OK;
    }

    private int mLastY = 0;
    private long mLastSend = SystemClock.currentThreadTimeMillis();

    private int getYEnsureNoOverlapping() {
        long currentMillis = SystemClock.currentThreadTimeMillis();
        int y;
        if (currentMillis - mLastSend < 6000) {
            y = RandomUtil.randomIntExcept(0, getConfig().getMaxLine(), mLastY);// 随机行数
        } else {
            y = RandomUtil.randomInt(0, getConfig().getMaxLine());// 随机行数
        }
        mLastY = y;
        mLastSend = currentMillis;
        return y;
    }

}
