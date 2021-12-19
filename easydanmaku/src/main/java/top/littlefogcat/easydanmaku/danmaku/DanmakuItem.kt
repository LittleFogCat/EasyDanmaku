package top.littlefogcat.easydanmaku.danmaku

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
class DanmakuItem(
    open var text: String,
    open var time: Int,
    open var type: Int,
    open var color: Int,
    open var priority: Int,
    open var id: String = "",
    open var textScale: Float = 1f,
) {
    override fun toString(): String {
        return "$text/$time/$type"
    }
}