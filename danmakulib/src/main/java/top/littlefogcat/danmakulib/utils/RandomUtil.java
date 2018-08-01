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
     * @param from 随机最小值，包含
     * @param to   随机最大值，不包含
     */
    public static int randomInt(int from, int to) {
        if (from >= to) {
            throw new IllegalArgumentException("from必须小于to");
        }
        return from + sRandom.nextInt(to - from);
    }

    /**
     * from到to的随机整数，但不包括except
     *
     * @param from   随机最小值，包含
     * @param to     随机最大值，不包含
     * @param except 不会随机到该数字
     * @return from到to的随机整数，但不包括except
     */
    public static int randomIntExcept(int from, int to, int except) {
        if (except < from || except >= to) {
            return randomInt(from, to);
        } else if (to - from < 2) {
            throw new IllegalArgumentException("无法找到满足条件的数字");
        } else if (except == from) {
            return randomInt(except + 1, to);
        } else if (except == to - 1) {
            return randomInt(from, except);
        } else {
            for (; ; ) {
                int r = randomInt(from, to);
                if (r != except) {
                    return r;
                }
            }
        }
    }
}
