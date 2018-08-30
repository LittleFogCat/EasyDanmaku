package top.littlefogcat.danmakulib.danmaku;

import android.annotation.SuppressLint;
import android.content.Context;
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
 * 仅显示文字的DanmakuView
 * <p>
 * Created by jjy on 2018/6/6.
 * <p>
 * 弹幕View，用init设置属性，然后startAnimation()
 * <p>
 * 或者直接调用create()静态方法生成一个DanmakuView
 */

@SuppressLint("AppCompatCustomView")
public class TextDanmakuView extends TextView implements DanmakuView {
    private ViewGroup mRootView;
    private Danmaku mDanmaku;
    private Animation mAnim;

    private int mTextSizeMiddle = ScreenUtil.autoWidth(40);
    private int mTextSizeSmall = ScreenUtil.autoWidth(28);

    private List<OnEnterListener> mOnEnterListeners = new ArrayList<>();
    private List<OnExitListener> mOnExitListeners = new ArrayList<>();

    TextDanmakuView(Context context) {
        this(context, null);
    }

    private TextDanmakuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private TextDanmakuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Deprecated
    public static TextDanmakuView create(Context context,
                                         ViewGroup rootView,
                                         Danmaku danmaku,
                                         int y,
                                         int duration,
                                         OnEnterListener onEnterListener,
                                         OnExitListener onDismissListener) {
        TextDanmakuView view = new TextDanmakuView(context);
        view.init(rootView, danmaku, y, duration);
        view.addOnEnterListener(onEnterListener);
        view.addOnExitListener(onDismissListener);
        return view;
    }


    /**
     * @param rootView 弹幕容器
     * @param danmaku  弹幕
     * @param y        显示在第几行
     * @param duration 弹幕动画时长
     */
    public void init(ViewGroup rootView, Danmaku danmaku, int y, int duration) {
        setRootView(rootView);
        setDanmaku(danmaku);
        initAnimation(y, duration);
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
                        mOnExitListener.onExit(TextDanmakuView.this);
                    }
                }
                mRootView.removeView(TextDanmakuView.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void addOnEnterListener(OnEnterListener listener) {
        if (listener != null) {
            mOnEnterListeners.add(listener);
        }
    }

    @Override
    public void addOnExitListener(OnExitListener listener) {
        if (listener != null) {
            mOnExitListeners.add(listener);
        }
    }

    public void show() {
        if (mAnim == null || mRootView == null) {
            return;
        }
        mRootView.addView(this);
        startAnimation(mAnim);
    }
}
