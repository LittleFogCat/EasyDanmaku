package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import top.littlefogcat.danmakulib.utils.L;
import top.littlefogcat.danmakulib.utils.ScreenUtil;

/**
 * 用法示例：
 * DanmakuManager dm = DanmakuManager.getInstance();
 * dm.init(getContext());
 * dm.show(new Danmaku("test"));
 * <p>
 * Created by LittleFogCat.
 */
public class DanmakuManager {
    private static final String TAG = DanmakuManager.class.getSimpleName();
    public static final int RESULT_OK = 0;
    public static final int RESULT_NULL_ROOT_VIEW = 1;
    public static final int RESULT_FULL_POOL = 2;
    public static final int TOO_MANY_DANMAKU = 2;

    private static DanmakuManager sInstance;

    /**
     * 弹幕容器
     */
    private WeakReference<FrameLayout> mDanmakuContainer;
    /**
     * 弹幕池
     */
    private DanmakuViewPool mDanmakuViewPool;

    private boolean mInit = false;


    /**
     * 一些配置
     */
    public class Config {
        /**
         * 标准弹幕字体大小
         */
        private int textSizeNormal;
        /**
         * 小号弹幕字体大小
         */
        private int textSizeSmall;

        /**
         * 滚动弹幕显示时长
         */
        private int durationScroll;
        /**
         * 顶部弹幕显示时长
         */
        private int durationTop;
        /**
         * 底部弹幕的显示时长
         */
        private int durationBottom;

        /**
         * 滚动弹幕的最大行数
         */
        private int maxScrollLine;

        /**
         * @return 标准弹幕字体大小，如果没有设置，则为1920x1080屏幕的40像素大小（按比例缩放）
         */
        public int getTextSizeNormal() {
            if (textSizeNormal == 0) {
                textSizeNormal = ScreenUtil.autoWidth(40);
            }
            return textSizeNormal;
        }

        public void setTextSizeNormal(int textSizeNormal) {
            this.textSizeNormal = textSizeNormal;
        }

        public int getTextSizeSmall() {
            if (textSizeNormal == 0) {
                textSizeNormal = ScreenUtil.autoWidth(28);
            }
            return textSizeSmall;
        }

        public void setTextSizeSmall(int textSizeSmall) {
            this.textSizeSmall = textSizeSmall;
        }

        public int getDurationScroll() {
            if (durationScroll == 0) {
                durationScroll = 10000;
            }
            return durationScroll;
        }

        public void setDurationScroll(int durationScroll) {
            this.durationScroll = durationScroll;
        }

        public int getDurationTop() {
            if (durationTop == 0) {
                durationTop = 5000;
            }
            return durationTop;
        }

        public void setDurationTop(int durationTop) {
            this.durationTop = durationTop;
        }

        public int getDurationBottom() {
            if (durationBottom == 0) {
                durationBottom = 5000;
            }
            return durationBottom;
        }

        public void setDurationBottom(int durationBottom) {
            this.durationBottom = durationBottom;
        }

        public int getMaxDanmakuLine() {
            if (maxScrollLine == 0) {
                maxScrollLine = 12;
            }
            return maxScrollLine;
        }

        public void setMaxScrollLine(int maxScrollLine) {
            this.maxScrollLine = maxScrollLine;
        }
    }

    private Config mConfig;

    /**
     * 用于计算弹幕位置，来保证弹幕不重叠又不浪费空间。
     */
    private class DanmakuPositionCalculator {
        private int lineHeight;
        private int parentWidth;
        private int parentHeight;
        List<DanmakuView> lastOnesOnEachLine = new ArrayList<>();// 保存每一行最后一个弹幕消失的时间
        boolean[] top;
        boolean[] bottom;

        DanmakuPositionCalculator() {
            int textSize = getConfig().getTextSizeNormal();
            lineHeight = (int) (textSize * 1.35);
            parentWidth = mDanmakuContainer.get().getWidth();
            parentHeight = mDanmakuContainer.get().getHeight();
            L.d(TAG, "DanmakuPositionCalculator: lineHeight = " + lineHeight);

            int maxLine = getConfig().getMaxDanmakuLine();
            top = new boolean[maxLine];
            bottom = new boolean[maxLine];
        }

        int getY(DanmakuView view) {
            switch (view.getDanmaku().mode) {
                case scroll:
                    return getScrollY(view);
                case top:
                    return getTopY(view);
                case bottom:
                    return getBottomY(view);
            }
            return -1;
        }

        int getTopY(DanmakuView view) {
            for (int i = 0; i < top.length; i++) {
                boolean isShowing = top[i];
                if (!isShowing) {
                    final int finalI = i;
                    top[finalI] = true;
                    view.addOnExitListener(new DanmakuView.OnExitListener() {
                        @Override
                        public void onExit(DanmakuView view) {
                            top[finalI] = false;
                        }
                    });
                    return i * lineHeight;
                }
            }
            return -1;
        }

        int getBottomY(DanmakuView view) {
            for (int i = 0; i < bottom.length; i++) {
                boolean isShowing = bottom[i];
                if (!isShowing) {
                    final int finalI = i;
                    bottom[finalI] = true;
                    view.addOnExitListener(new DanmakuView.OnExitListener() {
                        @Override
                        public void onExit(DanmakuView view) {
                            bottom[finalI] = false;
                        }
                    });
                    return parentHeight - (i + 1) * lineHeight;
                }
            }
            return -1;
        }

        int getScrollY(DanmakuView view) {
            if (lastOnesOnEachLine.size() == 0) {
                lastOnesOnEachLine.add(view);
                return 0;
            }

            int i;
            for (i = 0; i < lastOnesOnEachLine.size(); i++) {
                DanmakuView lastOneOfThisLine = lastOnesOnEachLine.get(i);
                int timeToDisappear = getTimeToDisappear(lastOneOfThisLine);
                int timeToArrive = getTimeToArrive(view);
                boolean isFullyShown = isFullyShown(lastOneOfThisLine);
                if (timeToDisappear <= timeToArrive && isFullyShown) {
                    // 如果最后一个弹幕在这个弹幕到达之前消失，并且最后一个字已经显示完毕，
                    // 那么新的弹幕就可以在这一行显示
                    lastOnesOnEachLine.set(i, view);
                    return i * lineHeight;
                }
            }
            int maxLine = getConfig().getMaxDanmakuLine();
            if (maxLine == 0 || i < maxLine) {
                lastOnesOnEachLine.add(view);
                return i * lineHeight;
            }

            return -1;
        }

        /**
         * 这条弹幕是否已经全部出来了。如果没有的话，
         * 后面的弹幕不能出来，否则就重叠了。
         */
        private boolean isFullyShown(DanmakuView view) {
            if (view == null) {
                return true;
            }
            int scrollX = view.getScrollX();
            int textLength = view.getTextLength();
            return textLength - scrollX < parentWidth;
        }

        /**
         * 这条弹幕还有多少毫秒彻底消失。
         */
        private int getTimeToDisappear(DanmakuView view) {
            if (view == null) {
                return 0;
            }
            float speed = getSpeed(view);
            int scrollX = view.getScrollX();
            int textLength = view.getTextLength();
            int wayToGo = textLength - scrollX;
            return (int) (wayToGo / speed);
        }

        /**
         * 这条弹幕还要多少毫秒抵达屏幕边缘。
         */
        private int getTimeToArrive(DanmakuView view) {
            float speed = getSpeed(view);
            int wayToGo = parentWidth;
            return (int) (wayToGo / speed);
        }

        /**
         * 这条弹幕的速度。单位：px/ms
         */
        private float getSpeed(DanmakuView view) {
            int textLength = view.getTextLength();
            int width = parentWidth;
            float s = textLength + width + 0.0f;
            int t = getRealDisplayDuration(view.getDanmaku());// 这个时候还没有设置duration，所以不能用getDuration
            return s / t;
        }

    }

    private DanmakuPositionCalculator mPositionCal;

    private DanmakuManager() {
    }

    public static DanmakuManager getInstance() {
        if (sInstance == null) {
            sInstance = new DanmakuManager();
        }
        return sInstance;
    }

    /**
     * 初始化。在使用之前必须调用该方法。
     */
    public void init(Context context) {
        if (mInit) {
            L.e(TAG, "init: already init");
            return;
        }

        if (mDanmakuViewPool == null) {
            mDanmakuViewPool = new DanmakuViewPool(context);
        }

        if (!ScreenUtil.isInit()) {
            ScreenUtil.init(context);
        }

        mInit = true;
    }

    public Config getConfig() {
        if (mConfig == null) {
            mConfig = new Config();
        }
        return mConfig;
    }

    private DanmakuPositionCalculator getPositionCalculator() {
        if (mPositionCal == null) {
            mPositionCal = new DanmakuPositionCalculator();
        }
        return mPositionCal;
    }

    public void setDanmakuViewPool(DanmakuViewPool pool) {
        if (mDanmakuViewPool != null) {
            mDanmakuViewPool.release();
        }
        mDanmakuViewPool = pool;
    }

    /**
     * 设置允许同时出现最多的弹幕数，如果屏幕上显示的弹幕数超过该数量，那么新出现的弹幕将被丢弃，
     * 直到有旧的弹幕消失。
     *
     * @param max 同时出现的最多弹幕数，-1无限制
     */
    public void setMaxDanmakuSize(int max) {
        if (mDanmakuViewPool == null) {
            return;
        }
        mDanmakuViewPool.setMaxSize(max);
    }

    /**
     * 设置弹幕的容器，所有的弹幕都在这里面显示
     */
    public void setDanmakuContainer(final FrameLayout root) {
        if (mDanmakuContainer != null && mDanmakuContainer.get() != null) {
            // TODO: 2019/3/28
        }

        mDanmakuContainer = new WeakReference<>(root);
    }

    /**
     * 发送一条弹幕
     */
    public int send(Danmaku danmaku) {
        DanmakuView view = mDanmakuViewPool.get();

        if (view == null) {
            L.w(TAG, "show: Too many danmaku, discard");
            return RESULT_FULL_POOL;
        }
        if (mDanmakuContainer.get() == null) {
            L.w(TAG, "show: Root view is null. Didn't call setDanmakuContainer() or root view has been recycled.");
            return RESULT_NULL_ROOT_VIEW;
        }

        view.setDanmaku(danmaku);

        // 字体大小
        int textSize = getRealTextSize(danmaku);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        // 字体颜色
        try {
            int color = Color.parseColor(danmaku.color);
            view.setTextColor(color);
        } catch (Exception e) {
            e.printStackTrace();
            view.setTextColor(Color.WHITE);
        }

        DanmakuPositionCalculator dpc = getPositionCalculator();
        int y = dpc.getY(view);
        if (y == -1) {
            // 装不下了 丢弃
            L.d(TAG, "send: 装不下了 丢弃 " + danmaku);
            return TOO_MANY_DANMAKU;
        }
        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (p == null) {
            p = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        p.topMargin = y;
        view.setLayoutParams(p);
        view.setMinHeight((int) (getConfig().getTextSizeNormal() * 1.35));
        view.show(mDanmakuContainer.get(), getRealDisplayDuration(danmaku));
        return RESULT_OK;
    }

    public void setLogEnabled(boolean enabled) {
        L.setEnabled(enabled);
    }

    private int getRealTextSize(Danmaku danmaku) {
        Config config = getConfig();
        return danmaku.textSize == Danmaku.TextSize.small ? config.getTextSizeSmall() : config.getTextSizeNormal();
    }

    /**
     * @return 返回这个弹幕显示时长
     */
    private int getRealDisplayDuration(Danmaku danmaku) {
        Config config = getConfig();
        int duration;
        switch (danmaku.mode) {
            case top:
                duration = config.getDurationTop();
                break;
            case bottom:
                duration = config.getDurationBottom();
                break;
            case scroll:
            default:
                duration = config.getDurationScroll();
                break;
        }
        return duration;
    }


}
