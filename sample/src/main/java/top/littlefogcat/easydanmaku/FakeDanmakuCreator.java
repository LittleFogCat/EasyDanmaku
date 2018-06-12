package top.littlefogcat.easydanmaku;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import top.littlefogcat.danmakulib.danmaku.Danmaku;
import top.littlefogcat.danmakulib.danmaku.DanmakuView;
import top.littlefogcat.danmakulib.danmaku.DanmakuViewPool;
import top.littlefogcat.danmakulib.danmaku.DanmakuViewPools;

import static top.littlefogcat.danmakulib.utils.RandomUtil.randomInt;

/**
 * Created by jjy on 2018/6/6.
 */

public class FakeDanmakuCreator {
    private static final String TAG = "FakeDanmakuCreator";
    private DanmakuViewPool mPool;

    public FakeDanmakuCreator(Context context) {
        mPool = DanmakuViewPools.newCachedDanmakuViewPool(context);
    }

    public static Danmaku createFakeDanmaku() {
        return new Danmaku(randomString(), randomColor(), 0, 0);
    }

    public void show(ViewGroup root) {
        Danmaku danmaku = new Danmaku(randomString(), randomColor(), 0, 0);
        DanmakuView danmakuView = mPool.get();
        if (danmakuView == null) {
            return;
        }
        danmakuView.init(root, danmaku, randomInt(0, root.getHeight() / 3),
                randomInt(12000, 18000), null, null);
        danmakuView.start();
    }

    private static int[] mColors = {Color.WHITE, Color.YELLOW, Color.RED};
    private static String[] mStrings = {"假装有弹幕", "富强民主文明和谐自由平等公正法治爱国敬业诚信友善", "加油哦", "AcFun是一家弹幕视频网站", "bilibili是国内知名的视频弹幕网站"};

    public static int randomColor() {
        int r = randomInt(0, mColors.length);
        return mColors[r];
    }

    public static String randomString() {
        int r = randomInt(0, mStrings.length);
        return mStrings[r];
    }
}
