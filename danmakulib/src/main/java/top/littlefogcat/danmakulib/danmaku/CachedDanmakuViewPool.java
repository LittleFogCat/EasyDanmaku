package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

/**
 * Created by jjy on 2018/6/6.
 * 弹幕池，为了复用，节省开销
 */

public class CachedDanmakuViewPool implements DanmakuViewPool {
    private static final String TAG = "DanmakuViewPool";
    private Context mContext;
    private int mInUseSize = 0;// 正在使用中的View
    private int mCoreSize = 5;
    private int mMaxSize = 100;
    private int mKeepAliveTime = 60000;
    private BlockingQueue<DanmakuView> mDanmakuQueue;

    CachedDanmakuViewPool(Context context, int coreSize, int maxSize, BlockingQueue<DanmakuView> workQueue) {
        mContext = context;
        mCoreSize = coreSize;
        mMaxSize = maxSize;
        mDanmakuQueue = workQueue;
    }

    public void setMax(int max) {
        mMaxSize = max;
    }

    /**
     * 获取一个脏的DanmakuView。
     */
    @Nullable
    private DanmakuView getDirty() {
        Log.v(TAG, "getDirty: in use = " + mInUseSize + ", idle = " + mDanmakuQueue.size());

        if (count() < mCoreSize) {
            // 如果总弹幕量小于弹幕池核心数，直接新建
            DanmakuView view = new DanmakuView(mContext);
            view.addOnExitListener(this::recycle);
            mInUseSize++;
            return view;
        } else if (count() <= mMaxSize) {
            // 如果总弹幕量大于弹幕池核心数，但小于最大值，那么尝试从空闲队列中取，取不到则新建
            DanmakuView view = mDanmakuQueue.poll();
            if (view != null) {
                mInUseSize++;
                return view;
            }
            view = new DanmakuView(mContext);
            view.addOnExitListener(this::recycle);
            mInUseSize++;
            return view;
        } else {
            // 如果总弹幕量超过了最大值，那么就丢弃请求，返回Null
            return null;
        }
    }

    @Override
    public DanmakuView get() {
        return getDirty();
    }

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public void release() {
        while (mDanmakuQueue.poll() != null) {
        }
    }

    public int count() {
        return mInUseSize + mDanmakuQueue.size();
    }

    private void recycle(DanmakuView view) {
        boolean offer = mDanmakuQueue.offer(view);
        Log.v(TAG, "recycle: " + (offer ? "success" : "fail"));
        mInUseSize--;
    }

}
