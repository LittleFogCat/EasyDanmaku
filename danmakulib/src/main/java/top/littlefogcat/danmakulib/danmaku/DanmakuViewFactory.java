package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.view.ViewGroup;

import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.MODE_IMG_TEXT;
import static top.littlefogcat.danmakulib.danmaku.DanmakuConstant.MODE_TEXT_ONLY;

public class DanmakuViewFactory {

    public static DanmakuView create(Context context) {
        int type = DanmakuConfig.getConfig().mMode;
        switch (type) {
            case MODE_IMG_TEXT:
                return new ImgTextDanmakuView(context);
            case MODE_TEXT_ONLY:
            default:
                return new TextDanmakuView(context);
        }
    }

    public static DanmakuView create(int type,
                                     Context context,
                                     ViewGroup rootView,
                                     Danmaku danmaku,
                                     int y,
                                     int duration,
                                     DanmakuView.OnEnterListener onEnterListener,
                                     DanmakuView.OnExitListener onDismissListener) {
        DanmakuView view;
        switch (type) {
            case MODE_IMG_TEXT:
                view = new ImgTextDanmakuView(context);
                break;
            case MODE_TEXT_ONLY:
            default:
                view = new TextDanmakuView(context);
                break;
        }
        view.init(rootView, danmaku, y, duration);
        view.addOnEnterListener(onEnterListener);
        view.addOnExitListener(onDismissListener);

        return view;
    }
}
