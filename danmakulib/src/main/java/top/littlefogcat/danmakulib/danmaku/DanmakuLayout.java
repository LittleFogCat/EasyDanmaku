package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * This class is not complete, take caution to use it!
 * <p>
 * Created by LittleFogCat.
 */
// FIXME: This class is not complete, take caution to use it!
public class DanmakuLayout extends FrameLayout {
    private DanmakuManager mManager;

    public DanmakuLayout(Context context) {
        super(context);
        init();
    }

    public DanmakuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mManager = DanmakuManager.getInstance();
        mManager.init(getContext());
        mManager.setDanmakuContainer(this);
    }

    public void send(Danmaku danmaku) {
        mManager.send(danmaku);
    }

    @Deprecated
    public void send(String text) {
        send(new Danmaku(text, Danmaku.TextSize.normal, Danmaku.Mode.scroll, Danmaku.COLOR_WHITE));
    }

    @Deprecated
    public void send(String text, String color) {
        send(new Danmaku(text, Danmaku.TextSize.normal, Danmaku.Mode.scroll, color));
    }

    @Deprecated
    public void send(String text, Danmaku.TextSize textSize, Danmaku.Mode mode, String color) {
        send(new Danmaku(text, textSize, mode, color));
    }
}
