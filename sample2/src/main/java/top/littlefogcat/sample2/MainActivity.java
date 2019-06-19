package top.littlefogcat.sample2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import top.littlefogcat.danmakulib.danmaku.DanmakuLayout;

public class MainActivity extends AppCompatActivity {

    private EditText mEtSend;
    private DanmakuLayout mDanmakuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtSend = findViewById(R.id.etSend);
        mDanmakuLayout = findViewById(R.id.danmakuLayout);

        mEtSend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEND) {
                    mDanmakuLayout.send(mEtSend.getText().toString(), "#FF333333");
                    mEtSend.setText("");
                    mEtSend.clearFocus();
                    hideIme();
                    return true;
                }
                return false;
            }
        });
    }

    private void hideIme() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && mEtSend != null) {
            imm.hideSoftInputFromWindow(mEtSend.getWindowToken(), 0);
        }
    }
}
