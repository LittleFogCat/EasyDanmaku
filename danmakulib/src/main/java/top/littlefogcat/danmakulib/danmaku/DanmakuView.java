package top.littlefogcat.danmakulib.danmaku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import top.littlefogcat.danmakulib.utils.ScreenUtil;

/**
 * Created by jjy on 2018/6/6.
 * <p>
 * 弹幕View，用init设置属性，然后startAnimation()
 * <p>
 * 或者直接调用create()静态方法生成一个DanmakuView
 */

@SuppressLint("AppCompatCustomView")
public class DanmakuView extends TextView {
    private ViewGroup mRootView;
    private Danmaku mDanmaku;
    private Animation mAnim;

    private static final int TEXT_SIZE_MIDDLE = 40;
    private static final int TEXT_SIZE_SMALL = 28;
    private int mTextSizeMiddle = ScreenUtil.autoWidth(TEXT_SIZE_MIDDLE);
    private int mTextSizeSmall = ScreenUtil.autoWidth(TEXT_SIZE_SMALL);

    private List<OnEnterListener> mOnEnterListeners = new ArrayList<>();
    //    private OnExitListener mOnExitListener;
    private List<OnExitListener> mOnExitListeners = new ArrayList<>();

    public DanmakuView(Context context) {
        this(context, null);
    }

    public DanmakuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmakuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static DanmakuView create(Context context,
                                     ViewGroup rootView,
                                     Danmaku danmaku,
                                     int y,
                                     int duration,
                                     OnEnterListener onEnterListener,
                                     OnExitListener onDismissListener) {
        DanmakuView view = new DanmakuView(context);
        view.init(rootView, danmaku, y, duration, onEnterListener, onDismissListener);
        return view;
    }


    /**
     * @param rootView          弹幕容器
     * @param danmaku           弹幕
     * @param y                 显示在第几行
     * @param duration          弹幕动画时长
     * @param onEnterListener   开始动画监听
     * @param onAnimEndListener 结束动画监听
     */
    public void init(ViewGroup rootView, Danmaku danmaku, int y, int duration,
                     OnEnterListener onEnterListener,
                     OnExitListener onAnimEndListener) {
        setRootView(rootView);
        setDanmaku(danmaku);
        initAnimation(y, duration);
        addOnEnterListener(onEnterListener);
        addOnExitListener(onAnimEndListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setRootView(ViewGroup rootView) {
        mRootView = rootView;
    }

    public void setDanmaku(Danmaku danmaku) {
        mDanmaku = danmaku;
        setText(danmaku.getContent());
        setTextColor(danmaku.getColor());
        setShadowLayer(2.5f, 0, 0, Color.BLACK);
        setSingleLine(true);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, (danmaku.getSize() == 0 ? mTextSizeMiddle : mTextSizeSmall));
    }

    public Danmaku getDanmaku() {
        return mDanmaku;
    }

    public void initAnimation(int y, int duration) {
        y = (int) (y * getTextSize());
        mAnim = new TranslateAnimation(Animation.ABSOLUTE, mRootView.getWidth(),
                Animation.ABSOLUTE, -mRootView.getWidth(),
                Animation.ABSOLUTE, y,
                Animation.ABSOLUTE, y);
        mAnim.setDuration(duration);
        mAnim.setFillAfter(false);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                for (OnEnterListener mOnEnterListener : mOnEnterListeners) {
                    if (mOnEnterListener != null) {
                        mOnEnterListener.onEnter();
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!mOnExitListeners.isEmpty()) {
                    for (OnExitListener mOnExitListener : mOnExitListeners) {
                        mOnExitListener.onExit(DanmakuView.this);
                    }
                }
                mRootView.removeView(DanmakuView.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public interface OnEnterListener {
        void onEnter();
    }

    public interface OnExitListener {
        void onExit(DanmakuView view);
    }

    public void addOnEnterListener(OnEnterListener listener) {
        if (listener != null) {
            mOnEnterListeners.add(listener);
        }
    }

    public void addOnExitListener(OnExitListener listener) {
        if (listener != null) {
            mOnExitListeners.add(listener);
        }
    }

    public void start() {
        if (mAnim == null || mRootView == null) {
            return;
        }
        mRootView.addView(this);
        startAnimation(mAnim);
    }
}
