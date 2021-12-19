package top.littlefogcat.easydanmaku.ui

import top.littlefogcat.easydanmaku.danmaku.DanmakuItem
import java.util.*

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class DanmakuResolverImpl : DanmakuResolver {
    private var data = TreeSet<DanmakuItem>(compareBy { danmaku -> danmaku.time })
    private var lastRetrieveTime = 0
    private val startDummy = DanmakuItem("", 0, 0, 0, 0)
    private val endDummy = DanmakuItem("", 0, 0, 0, 0)

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
        startDummy.time = startTime
        endDummy.time = endTime
        return data.subSet(startDummy, endDummy)
    }
}