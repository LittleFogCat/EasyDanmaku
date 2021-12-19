package top.littlefogcat.easydanmaku.danmaku

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class DummyDanmaku(item: DanmakuItem = DanmakuItem("", 0, 0, 0, 0)) :
    Danmaku(item) {
    override val duration: Int = 0
}