package top.littlefogcat.easydanmaku;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import top.littlefogcat.danmakulib.danmaku.Danmaku;
import top.littlefogcat.danmakulib.danmaku.DanmakuManager;
import top.littlefogcat.danmakulib.danmaku.IDanmakuManager;

public class MainActivity extends AppCompatActivity {
    private ViewGroup mDanmakuContainer;
    private EditText mEtInput;
    private Button mBtnSend;
    private Button mBtnRandom;

    private IDanmakuManager mDanmakuManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initDanmaku();
        initView();
    }

    private void findView() {
        mDanmakuContainer = findViewById(R.id.container);
        mEtInput = findViewById(R.id.etInput);
        mBtnSend = findViewById(R.id.btnSend);
        mBtnRandom = findViewById(R.id.btnRandom);
    }

    private void initDanmaku() {
        mDanmakuManager = DanmakuManager.getInstance();
        mDanmakuManager.setRootView(mDanmakuContainer);
        mDanmakuManager.getConfig()
                .useImgTextMode()
                .useCircleHead()
                .setMaxLine(6);
    }

    private void initView() {
        mBtnSend.setOnClickListener(v -> {
            String text = mEtInput.getText().toString();
            Danmaku danmaku = DanmakuCreator.createFakeDanmaku(text);
            mDanmakuManager.show(danmaku);
        });
        mBtnRandom.setOnClickListener(v -> {
            if (!mShouldRunTaskAgain) {
                startSendRandomDanmaku();
                mBtnRandom.setText(R.string.stop_random);
            } else {
                stopSendRandomDanmaku();
                mBtnRandom.setText(R.string.start_random);
            }
        });

        mBtnSend.requestFocus();
    }

    private void startSendRandomDanmaku() {
        mShouldRunTaskAgain = true;
        mHandler.post(mRandomTask);
    }

    private void stopSendRandomDanmaku() {
        mShouldRunTaskAgain = false;
    }

    private static final int RANDOM_TASK_INTERVAL = 500;
    private boolean mShouldRunTaskAgain = false;
    private Handler mHandler = new Handler();
    private Runnable mRandomTask = new Runnable() {
        @Override
        public void run() {
            Danmaku danmaku = DanmakuCreator.createFakeDanmaku();
            mDanmakuManager.show(danmaku);
            if (mShouldRunTaskAgain) {
                mHandler.postDelayed(this, RANDOM_TASK_INTERVAL);
            }
        }
    };
}
