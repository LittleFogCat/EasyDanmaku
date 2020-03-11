package top.littlefogcat.sample;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import top.littlefogcat.danmakulib.danmaku.Danmaku;
import top.littlefogcat.danmakulib.utils.ScreenUtil;

/**
 * Created by LittleFogCat on 2019/3/29.
 */
public class DanmakuCreator {
    private char[] mCharArr;
    private String[] mColors = {
            "#FFFFFFFF", "#FFFF0000", "#FFFFFF00", "#FF00FF00"
    };

    DanmakuCreator(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.word);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (s == null || s.length() == 0) {
            s = "赵钱孙李周吴郑王";
        }
        mCharArr = s.toCharArray();
    }

    public Danmaku create() {
        Danmaku danmaku = new Danmaku();
        danmaku.text = randomText();
        danmaku.mode = Danmaku.Mode.scroll;
        danmaku.color = randomColor();
        danmaku.size = ScreenUtil.autoSize(56, 36);

        return danmaku;
    }

    private static final String TAG = "DanmakuCreator";

    private String randomText() {
        int length = (int) (26 - Math.sqrt(RandomUtil.nextInt(625)));

        int arrLen = mCharArr.length;
        StringBuilder s = new StringBuilder();
        while (length > 0) {
            char c = mCharArr[RandomUtil.nextInt(arrLen)];
            s.append(c);
            length--;
        }

        return s.toString();
    }

    private String randomColor() {
        int i = RandomUtil.nextInt(10);
        if (i <= 5) {
            return "#FFFFFFFF";
        } else if (i == 6) {
            return "#FF0000FF";
        } else if (i == 7) {
            return "#FFFF0000";
        } else if (i == 8) {
            return "#FFFFFF00";
        } else {
            return "#FF00FF00";
        }
    }
}
