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

public class DanmakuCreator {
    private static final String TAG = "FakeDanmakuCreator";
    private DanmakuViewPool mPool;

    public DanmakuCreator(Context context) {
        mPool = DanmakuViewPools.newCachedDanmakuViewPool(context);
    }

    public static Danmaku createFakeDanmaku() {
        return new Danmaku(randomString(), randomColor(), 0, 0, randomHead());
    }

    public static Danmaku createFakeDanmaku(String text) {
        return new Danmaku(text, randomColor(), 0, 0, randomHead());
    }

    public void show(ViewGroup root) {
        Danmaku danmaku = new Danmaku(randomString(), randomColor());
        DanmakuView danmakuView = mPool.get();
        if (danmakuView == null) {
            return;
        }
        danmakuView.init(root, danmaku, randomInt(0, root.getHeight() / 3),
                randomInt(12000, 18000));
        danmakuView.show();
    }

    private static int[] mColors = {Color.WHITE, Color.YELLOW, Color.RED};
    private static String[] mStrings = {"假装有弹幕", "富强民主文明和谐自由平等公正法治", "爱国敬业诚信友善", "加油哦", "AcFun是一家弹幕视频网站", "bilibili是国内知名的视频弹幕网站"};
    private static String[] mHeads = {
            "http://mat1.gtimg.com/www/qq2018/imgs/qq_logo_2018x2.png",
            "https://www.baidu.com/img/bd_logo1.png",
            "http://img.alicdn.com/tfs/TB1TGfMcwMPMeJjy1XbXXcwxVXa-80-33.png",
            null, null, null
    };

    public static int randomColor() {
        int r = randomInt(0, mColors.length);
        return mColors[r];
    }

    public static String randomString() {
        int r = randomInt(0, mStrings.length);
        return mStrings[r];
    }

    public static String randomHead() {
        int r = randomInt(0, mStrings.length);
        return mHeads[r];
    }
}
