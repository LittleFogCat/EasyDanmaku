package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import top.littlefogcat.danmakulib.utils.L;

/**
 * DanmakuView池。
 * Created by LittleFogCat.
 */
public class DanmakuViewPool implements Pool<DanmakuView> {

    private static final String TAG = "DanmakuViewPool";
    private Context mContext;
    private int mInUseSize = 0;// 正在使用中的View
    private int mCoreSize;// 弹幕池核心数
    private int mMaxSize;// 弹幕池最大容量
    private int mKeepAliveTime = 60000;// todo 回收
    private BlockingQueue<DanmakuView> mDanmakuQueue;// 空闲队列

    public DanmakuViewPool(Context context) {
        this(context, 5, 100, new LinkedBlockingQueue<DanmakuView>(100));
    }

    public DanmakuViewPool(Context context, int coreSize, int maxSize, BlockingQueue<DanmakuView> workQueue) {
        mContext = context;
        mCoreSize = coreSize;
        mMaxSize = maxSize;
        mDanmakuQueue = workQueue;
    }

    public void setMaxSize(int maxSize) {
        int max = maxSize == -1 || maxSize > 1000 ? 1000 : maxSize;// FIXME: 2019/3/28 无限制？这里强制小于1000
        if (max != mMaxSize) {
            mMaxSize = max;
            mDanmakuQueue = new LinkedBlockingQueue<>(max);
            System.gc();
        }
    }

    /**
     * 获取一个DanmakuView。
     */
    @Override
    public DanmakuView get() {
        DanmakuView view;
        if (count() < mCoreSize) {
            // 如果总弹幕量小于弹幕池核心数，直接新建
            L.v(TAG, "get: 总弹幕量小于弹幕池核心数，直接新建");
            view = createView();
            mInUseSize++;
        } else if (count() <= mMaxSize) {
            // 如果总弹幕量大于弹幕池核心数，但小于最大值，那么尝试从空闲队列中取，取不到则新建
            view = mDanmakuQueue.poll();
            if (view == null) {
                L.v(TAG, "get: 总弹幕量大于弹幕池核心数，空闲队列中取不到，新建");
                view = createView();
            } else {
                view.restore();
                L.v(TAG, "get: 总弹幕量大于弹幕池核心数，从空闲队列取");
            }
            mInUseSize++;
        } else {
            // 如果总弹幕量超过了最大值，那么就丢弃请求，返回Null
            L.v(TAG, "get: 总弹幕量超过了最大值，那么就丢弃请求，返回Null");
            return null;
        }
        view.addOnExitListener(new DanmakuView.OnExitListener() {
            @Override
            public void onExit(DanmakuView v) {
                recycle(v);
            }
        });
        return view;
    }

    private DanmakuView createView() {
        return DanmakuViewFactory.createDanmakuView(mContext);
    }

    @Override
    public void release() {
        while (mDanmakuQueue.poll() != null) {
            L.v(TAG, "release: remain " + mDanmakuQueue.size());
        }
    }

    @Override
    public int count() {
        return mInUseSize + mDanmakuQueue.size();
    }

    public void recycle(DanmakuView view) {
        view.restore();
        boolean offer = mDanmakuQueue.offer(view);
        L.v(TAG, "recycle: " + (offer ? "success" : "fail"));
        mInUseSize--;
    }

}
