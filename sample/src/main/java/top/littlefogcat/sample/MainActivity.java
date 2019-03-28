package top.littlefogcat.sample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import top.littlefogcat.danmakulib.danmaku.Danmaku;
import top.littlefogcat.danmakulib.danmaku.DanmakuManager;
import top.littlefogcat.danmakulib.utils.ScreenUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FrameLayout mContainer;
    private EditText mEtSend;
    private Button mBtnRandom;

    private DanmakuManager mManager;

    private Handler mHandler = new Handler();
    private Runnable mRandomDanmakuTask;
    private DanmakuCreator mDanmakuCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDanmaku();

        hideIme();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");
        hideStatusBar();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void hideIme() {
        Log.d(TAG, "hideIme: ");
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mEtSend.getWindowToken(), 0);
        }
        hideStatusBar();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mContainer = findViewById(R.id.danmakuContainer);
        mEtSend = findViewById(R.id.etSend);
        mBtnRandom = findViewById(R.id.btnStartRandom);

        mBtnRandom.setOnClickListener(v -> {
            if (mBtnRandom.getText().toString().startsWith("开始")) {
                mHandler.post(mRandomDanmakuTask);
                mBtnRandom.setText(R.string.stop_random_danmaku);
            } else {
                mHandler.removeCallbacksAndMessages(null);
                mBtnRandom.setText(R.string.start_random_danmaku);
            }
        });
        mRandomDanmakuTask = new Runnable() {
            @Override
            public void run() {
                Danmaku danmaku = mDanmakuCreator.create();
                if (!mPause) {
                    mManager.send(danmaku);
                }
                mHandler.postDelayed(this, RandomUtil.nextInt(500, 1000));
            }
        };

        mEtSend.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideIme();
            }
        });
        mEtSend.setOnEditorActionListener((v, actionId, event) -> {
            Log.d(TAG, "initView: " + actionId);
            if (actionId == 100 || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE) {
                Danmaku danmaku = mDanmakuCreator.create();
                danmaku.text = mEtSend.getText().toString();
                mManager.send(danmaku);
                hideIme();
                mEtSend.clearFocus();
                mEtSend.setText("");
                return true;
            }
            return false;
        });
    }

    private void initDanmaku() {
        mManager = DanmakuManager.getInstance();
        mManager.init(this); // 必须调用init方法
        mManager.setLogEnabled(true); // 设置打开日志
        mManager.setDanmakuContainer(mContainer); // 设置弹幕容器，必须是FrameLayout
        mManager.setMaxDanmakuSize(120); // 设置同屏最大弹幕数

        DanmakuManager.Config config = mManager.getConfig(); // 弹幕相关设置
        config.setDurationTop(5000); // 设置顶部弹幕显示时长，默认6秒
        config.setDurationScroll(5000); // 设置滚动字幕显示时长，默认10秒
        config.setMaxScrollLine(12); // 设置滚动字幕最大行数
        config.setTextSizeNormal(ScreenUtil.autoWidth(40)); // 设置标准弹幕字体大小
        config.setTextSizeSmall(ScreenUtil.autoWidth(28)); // 设置小弹幕字体大小

        mDanmakuCreator = new DanmakuCreator(this);
    }

    private boolean mPause;

    @Override
    protected void onResume() {
        super.onResume();
        mPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPause = true;
    }
}
