package top.littlefogcat.danmakulib.utils;

import java.util.Random;

/**
 * Created by jjy on 2018/6/6.
 */

public class RandomUtil {
    private static final Random sRandom = new Random();

    /**
     * from到to的随机整数
     *
     * @param from 包含
     * @param to   不包含
     */
    public static int randomInt(int from, int to) {
        return from + sRandom.nextInt(to - from);
    }
}
