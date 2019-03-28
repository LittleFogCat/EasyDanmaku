package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.view.LayoutInflater;

import top.littlefogcat.danmakulib.R;

/**
 * Created by LittleFogCat.
 */
public class DanmakuViewFactory {
    public static DanmakuView createDanmakuView(Context context) {
        DanmakuView dv = (DanmakuView) LayoutInflater.from(context)
                .inflate(R.layout.danmaku_view, null, false);
        return dv;
    }
}
