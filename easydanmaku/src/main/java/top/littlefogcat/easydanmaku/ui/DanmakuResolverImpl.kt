package top.littlefogcat.easydanmaku.ui

import top.littlefogcat.easydanmaku.danmakus.DanmakuItem
import java.util.*

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class DanmakuResolverImpl : DanmakuResolver {
    // 按时间分组，同一时间戳的弹幕全部保留；TreeMap 保证按时间升序遍历。
    private val data = TreeMap<Long, MutableList<DanmakuItem>>()
    private var lastRetrieveTime = 0L

    override fun setData(danmakus: Collection<DanmakuItem>) {
        data.clear()
        danmakus.forEach { danmaku ->
            data.getOrPut(danmaku.time) { ArrayList() }.add(danmaku)
        }
    }

    override fun retrieve(time: Long): Collection<DanmakuItem> {
        val startTime = lastRetrieveTime
        lastRetrieveTime = time
        return retrieve(startTime, time)
    }

    override fun retrieve(startTime: Long, endTime: Long): Collection<DanmakuItem> {
        if (endTime <= startTime) {
            return Collections.emptyList()
        }
        val result = ArrayList<DanmakuItem>()
        // 与原先 subSet 语义保持一致：区间为 [startTime, endTime)
        for (danmakus in data.subMap(startTime, true, endTime, false).values) {
            result.addAll(danmakus)
        }
        return result
    }
}