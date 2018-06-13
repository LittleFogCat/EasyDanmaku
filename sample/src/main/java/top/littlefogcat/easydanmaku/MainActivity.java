package top.littlefogcat.easydanmaku;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import top.littlefogcat.danmakulib.danmaku.Danmaku;
import top.littlefogcat.danmakulib.danmaku.DanmakuContainer;

public class MainActivity extends AppCompatActivity {
    private DanmakuContainer mDanmakuContainer;
    private EditText mEtInput;
    private Button mBtnSend;
    private Button mBtnRandom;
    private VideoView mVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initView();
    }

    private void findView() {
        mDanmakuContainer = findViewById(R.id.container);
        mEtInput = findViewById(R.id.etInput);
        mBtnSend = findViewById(R.id.btnSend);
        mBtnRandom = findViewById(R.id.btnRandom);
        mVV = findViewById(R.id.vv);
    }

    private void initView() {
        mBtnSend.setOnClickListener(v -> {
            String text = mEtInput.getText().toString();
            Danmaku danmaku = new Danmaku(text, FakeDanmakuCreator.randomColor());
            mDanmakuContainer.show(danmaku);
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

        mVV.setVideoPath("http://video.699pic.com/videos/95/14/21/eB24X8qGe6bB1516951421.mp4");
        mVV.setFocusable(false);
        mVV.setFocusableInTouchMode(false);
        mVV.setOnPreparedListener(mp -> mp.setLooping(true));
        mVV.start();

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
            Danmaku danmaku = FakeDanmakuCreator.createFakeDanmaku();
            mDanmakuContainer.show(danmaku);
            if (mShouldRunTaskAgain) {
                mHandler.postDelayed(this, RANDOM_TASK_INTERVAL);
            }
        }
    };
}
