package top.littlefogcat.easydanmaku.ui

import top.littlefogcat.easydanmaku.danmakus.DanmakuItem
import java.util.*

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class DanmakuResolverImpl : DanmakuResolver {
    private var data = TreeSet<DanmakuItem>(compareBy { danmaku -> danmaku.time })
    private var lastRetrieveTime = 0
    private val startItem = DanmakuItem("", 0, 0, 0, 0)
    private val endItem = DanmakuItem("", 0, 0, 0, 0)

    override fun setData(danmakus: Collection<DanmakuItem>) {
        data.clear()
        data.addAll(danmakus)
    }

    override fun retrieve(time: Int): Collection<DanmakuItem> {
        val startTime = lastRetrieveTime
        lastRetrieveTime = time
        return retrieve(startTime, time)
    }

    override fun retrieve(startTime: Int, endTime: Int): Collection<DanmakuItem> {
        if (endTime <= startTime) {
            return Collections.emptyList()
        }
        startItem.time = startTime
        endItem.time = endTime
        return data.subSet(startItem, endItem)
    }
}