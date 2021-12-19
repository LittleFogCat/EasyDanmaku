package top.littlefogcat.easydanmaku.danmaku

import top.littlefogcat.easydanmaku.Danmakus

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
open class PinnedDanmaku(item: DanmakuItem? = null) :
    Danmaku(item) {
    override val duration: Int
        get() = Danmakus.Options.pinnedDanmakuDuration

}