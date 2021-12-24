package top.littlefogcat.easydanmaku.danmakus

import androidx.core.util.Pools
import top.littlefogcat.easydanmaku.Danmakus
import top.littlefogcat.easydanmaku.danmakus.views.*

object DanmakuPools {
    var size = 0
        private set

    private object RLPool : DanmakuPool<RLDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
        override fun create(): RLDanmaku {
            return RLDanmaku()
        }
    }

    private object LRPool : DanmakuPool<LRDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
        override fun create(): LRDanmaku {
            return LRDanmaku()
        }
    }

    private object TopPool : DanmakuPool<TopPinnedDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
        override fun create(): TopPinnedDanmaku {
            return TopPinnedDanmaku()
        }
    }

    private object BottomPool : DanmakuPool<BottomPinnedDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
        override fun create(): BottomPinnedDanmaku {
            return BottomPinnedDanmaku()
        }
    }

    private object AdvPool : DanmakuPool<AdvancedDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
        override fun create(): AdvancedDanmaku {
            return AdvancedDanmaku()
        }
    }

    fun ofType(type: Int): DanmakuPool<Danmaku> {
        return when (type) {
            Danmaku.TYPE_RL -> RLPool
            Danmaku.TYPE_LR -> LRPool
            Danmaku.TYPE_TOP -> TopPool
            Danmaku.TYPE_BOTTOM -> BottomPool
            Danmaku.TYPE_ADVANCED -> AdvPool
            else -> throw NoSuchElementException()
        } as DanmakuPool<Danmaku>
    }

    abstract class DanmakuPool<T : Danmaku?> internal constructor(private val maxPoolSize: Int) : Pools.Pool<T> {
        private var head: T? = null
        private var size = 0
        override fun acquire(): T {
            if (!Danmakus.Options.recycle) {
                return create()
            }
            val h = head ?: return create()
            head = h.next as T?
            size--
            DanmakuPools.size--
            return h
        }

        override fun release(instance: T): Boolean {
            if (size >= maxPoolSize) {
                return false
            }
            instance!!.next = head
            head = instance
            size++
            DanmakuPools.size++
            return true
        }

        abstract fun create(): T
    }
}
