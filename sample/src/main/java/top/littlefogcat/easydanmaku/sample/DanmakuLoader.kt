package top.littlefogcat.easydanmaku.sample

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import top.littlefogcat.easydanmaku.danmaku.Danmaku
import top.littlefogcat.easydanmaku.danmaku.DanmakuItem
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
object DanmakuLoader {
    fun load(source: InputStream): Collection<DanmakuItem> {
        val danmakus = TreeSet<DanmakuItem> { o1, o2 -> o1.time - o2.time }
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(InputStreamReader(source))
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "d") {
                        val p = parser.getAttributeValue("", "p")
                        // <d p="23.826000213623,1,25,16777215,1422201084,0,057075e9,757076900">我从未见过如此厚颜无耻之猴</d>
                        // 0:时间(弹幕出现时间)
                        // 1:类型(1从右至左滚动弹幕|6从左至右滚动弹幕|5顶端固定弹幕|4底端固定弹幕|7高级弹幕|8脚本弹幕)
                        // 2:字号
                        // 3:颜色
                        // 4:时间戳 ?
                        // 5:弹幕池id
                        // 6:用户hash
                        // 7:弹幕id
                        val splits = p.split(",")
                        val time = (splits[0].toFloat() * 1000).toInt()
                        val type = when (splits[1].toInt()) {
                            1 -> Danmaku.TYPE_RL
                            5 -> Danmaku.TYPE_TOP
                            4 -> Danmaku.TYPE_BOTTOM
                            6 -> Danmaku.TYPE_LR
                            7 -> Danmaku.TYPE_ADVANCED
                            else -> Danmaku.TYPE_UNKNOWN
                        }
                        val textSize = splits[2].toInt()
                        // 保留后24位
                        val color = splits[3].toInt()
                        val t = parser.nextText()
                        val id = splits[7]

                        if (type != Danmaku.TYPE_UNKNOWN && type != Danmaku.TYPE_ADVANCED) {
                            val item = DanmakuItem(t, time, type, color, 0, id, textSize / 25f)
                            danmakus.add(item)
                        }
                    }
                }
                XmlPullParser.END_TAG -> {}
                else -> {}
            }
            parser.next()
        }
        return danmakus
    }
}