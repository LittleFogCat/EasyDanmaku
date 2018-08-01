package top.littlefogcat.danmakulib.danmaku;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

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
        return new Danmaku(randomString(), randomColor(), 0, 0, randomHead());
    }

    public void show(ViewGroup root) {
        Danmaku danmaku = new Danmaku(randomString(), randomColor(), 0, 0);
        DanmakuView danmakuView = mPool.get();
        if (danmakuView == null) {
            return;
        }
        danmakuView.init(root, danmaku, randomInt(0, root.getHeight() / 3), randomInt(12000, 18000));
        danmakuView.show();
    }


    private static int randomColor() {
        return RandomHelper.randomColor();
    }

    private static String randomString() {
        return RandomHelper.randomString();
    }

    private static String randomHead() {
        return RandomHelper.randomHeadUrl();
    }

    public static class RandomHelper {
        public static Integer[] mColors = {Color.WHITE, Color.YELLOW, Color.RED};
        private static String[] mStrings = {"假装有弹幕", "富强民主文明和谐自由平等公正法治爱国敬业诚信友善", "加油哦", "AcFun是一家弹幕视频网站", "bilibili是国内知名的视频弹幕网站"};
        private static String[] mHeads = {
                "http://weather.cleartv.cn/00.png",
                "http://weather.cleartv.cn/01.png",
                "http://weather.cleartv.cn/02.png",
                "http://weather.cleartv.cn/03.png",
                "http://weather.cleartv.cn/04.png",
                "http://weather.cleartv.cn/05.png",
                "http://weather.cleartv.cn/06.png",
                "http://weather.cleartv.cn/07.png",
                "http://weather.cleartv.cn/08.png",
                "http://weather.cleartv.cn/09.png",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        };

        static Integer randomColor() {
            return arrayRandom(mColors);
        }

        static String randomString() {
            return arrayRandom(mStrings);
        }

        static String randomHeadUrl() {
            return arrayRandom(mHeads);
        }

        static <T> T arrayRandom(T[] ts) {
            int r = randomInt(0, ts.length);
            return ts[r];
        }
    }
}
