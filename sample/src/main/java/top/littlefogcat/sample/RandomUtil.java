package top.littlefogcat.sample;

import java.util.Random;

/**
 * Created by LittleFogCat on 2019/3/29.
 */
public class RandomUtil {
    private static Random sRandom;

    public static Random getRandom() {
        if (sRandom == null) {
            sRandom = new Random();
        }
        return sRandom;
    }

    public static int nextInt(int min, int max) {
        return getRandom().nextInt(max - min) + min;
    }

    public static int nextInt(int max) {
        return nextInt(0, max);
    }
}
