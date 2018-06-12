package top.littlefogcat.easydanmaku;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import top.littlefogcat.danmakulib.danmaku.Danmaku;
import top.littlefogcat.danmakulib.danmaku.DanmakuManager;
import top.littlefogcat.danmakulib.danmaku.IDanmakuManager;

public class MainActivity extends AppCompatActivity {
    private FrameLayout mContainer;
    private EditText mEtInput;
    private Button mBtnSend;
    private Button mBtnRandom;

    private IDanmakuManager mDanmakuManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = findViewById(R.id.container);
        mEtInput = findViewById(R.id.etInput);
        mBtnSend = findViewById(R.id.btnSend);
        mBtnRandom = findViewById(R.id.btnRandom);
        mDanmakuManager = DanmakuManager.newInstance(mContainer);

        initView();
    }

    private void initView() {
        mBtnSend.setOnClickListener(v -> {
            String text = mEtInput.getText().toString();
            Danmaku danmaku = new Danmaku(text, FakeDanmakuCreator.randomColor());
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
            Danmaku danmaku = FakeDanmakuCreator.createFakeDanmaku();
            mDanmakuManager.show(danmaku);
            if (mShouldRunTaskAgain) {
                mHandler.postDelayed(this, RANDOM_TASK_INTERVAL);
            }
        }
    };
}
