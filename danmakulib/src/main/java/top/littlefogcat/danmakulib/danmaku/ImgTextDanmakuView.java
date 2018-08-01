package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import top.littlefogcat.danmakulib.R;
import top.littlefogcat.danmakulib.utils.ImageUtil;
import top.littlefogcat.danmakulib.utils.ScreenUtil;

import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.HEAD_CIRCLE;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.HEAD_SQUARE;

/**
 * 显示头像和文字的DanmakuView
 */
public class ImgTextDanmakuView implements DanmakuView {
    private Context mContext;
    private View mView; // 实际显示的View
    private ViewGroup mRootView; // 父布局，盛装弹幕的容器

    private ImageView mIvUserhead;
    private TextView mTvContent;

    private Danmaku mDanmaku;
    private String mHeadUrl;
    private String mContent;

    private Animation mAnim;
    private ListenerInfo mListenerInfo = new ListenerInfo();

    private int mTextSizeMiddle = ScreenUtil.autoWidth(40);
    private int mTextSizeSmall = ScreenUtil.autoWidth(28);

    ImgTextDanmakuView(Context context) {
        mContext = context;
    }

    @Override
    public void init(ViewGroup rootView, Danmaku danmaku, int y, int duration) {
        setRootView(rootView);
        mView = LayoutInflater.from(mContext).inflate(R.layout.danmaku_view_layout, mRootView, false);
        mIvUserhead = (ImageView) mView.findViewById(R.id.ivUserHead);
        mTvContent = (TextView) mView.findViewById(R.id.tvContent);

        setDanmaku(danmaku);
        initAnimation(y, duration);
    }

    private void initAnimation(int y, int duration) {
        y = (int) (y * (mTvContent.getTextSize() + mView.getPaddingTop() + mView.getPaddingBottom()));
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
                for (OnEnterListener mOnEnterListener : mListenerInfo.mOnEnterListeners) {
                    if (mOnEnterListener != null) mOnEnterListener.onEnter();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                for (OnExitListener mOnExitListener : mListenerInfo.mOnExitListeners) {
                    if (mOnExitListener != null) mOnExitListener.onExit(ImgTextDanmakuView.this);
                }
                mIvUserhead.setImageBitmap(null);
                mRootView.removeView(mView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setDanmaku(Danmaku danmaku) {
        Log.d(TAG, "setDanmaku: content = " + danmaku.getContent() + ", 头像url = " + danmaku.getHeadUrl());
        mDanmaku = danmaku;
        mTvContent.setText(danmaku.getContent());
        mTvContent.setTextColor(danmaku.getColor());
        mTvContent.setShadowLayer(2.5f, 0, 0, Color.BLACK);
        mTvContent.setSingleLine(true);
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, (danmaku.getSize() == 0 ? mTextSizeMiddle : mTextSizeSmall));

        if (danmaku.getHeadUrl() != null) {
            mIvUserhead.setVisibility(View.VISIBLE);

            int head = DanmakuConfig.getConfig().mHead;

            switch (head) {
                case HEAD_CIRCLE:
                    ImageUtil.loadCircleImage(mContext, danmaku.getHeadUrl(), mIvUserhead);
                    break;
                case HEAD_SQUARE:
                    ImageUtil.loadImage(mContext, danmaku.getHeadUrl(), mIvUserhead);
                    break;
            }
        } else {
            mIvUserhead.setImageBitmap(null);
            mIvUserhead.setVisibility(View.GONE);
        }
    }

    private static final String TAG = "ImgTextDanmakuView";

    @Override
    public void show() {
        if (mAnim == null || mRootView == null) {
            return;
        }
        mRootView.addView(mView);
        mView.startAnimation(mAnim);
    }

    @Override
    public void addOnEnterListener(OnEnterListener listener) {
        if (listener != null) mListenerInfo.addOnEnterListener(listener);
    }

    @Override
    public void addOnExitListener(OnExitListener listener) {
        if (listener != null) mListenerInfo.addOnExitListener(listener);
    }

    @Override
    public void setRootView(ViewGroup rootView) {
        mRootView = rootView;
    }

    private static class ListenerInfo {
        private List<OnEnterListener> mOnEnterListeners = new ArrayList<>();
        private List<OnExitListener> mOnExitListeners = new ArrayList<>();

        private void addOnEnterListener(OnEnterListener enterListener) {
            if (mOnEnterListeners.contains(enterListener)) {
                return;
            }
            mOnEnterListeners.add(enterListener);
        }

        private void addOnExitListener(OnExitListener l) {
            if (mOnExitListeners.contains(l)) {
                return;
            }
            mOnExitListeners.add(l);
        }
    }
}
