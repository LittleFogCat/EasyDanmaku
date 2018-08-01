package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

@Deprecated
public class DanmakuContainer extends FrameLayout {
    private IDanmakuManager mManager;

    public DanmakuContainer(@NonNull Context context) {
        this(context, null);
    }

    public DanmakuContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmakuContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mManager = DanmakuManager.newInstance(this);
    }

    public void show(Danmaku danmaku) {
        mManager.show(danmaku);
    }
}
