package top.littlefogcat.easydanmaku.danmaku

import androidx.core.util.Pools
import top.littlefogcat.easydanmaku.Danmakus
import java.util.NoSuchElementException

object DanmakuPools {
    var size = 0
        private set

    private val rlPool: BasePool<RLDanmaku> =
        object : BasePool<RLDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
            override fun create(): RLDanmaku {
                return RLDanmaku()
            }
        }
    private val lrPool: BasePool<LRDanmaku> =
        object : BasePool<LRDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
            override fun create(): LRDanmaku {
                return LRDanmaku()
            }
        }
    private val topPool: BasePool<TopPinnedDanmaku> =
        object : BasePool<TopPinnedDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
            override fun create(): TopPinnedDanmaku {
                return TopPinnedDanmaku()
            }
        }
    private val bottomPool: BasePool<BottomPinnedDanmaku> =
        object : BasePool<BottomPinnedDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
            override fun create(): BottomPinnedDanmaku {
                return BottomPinnedDanmaku()
            }
        }
    private val advPool: BasePool<AdvancedDanmaku> =
        object : BasePool<AdvancedDanmaku>(Danmaku.MAX_POOL_SIZE_LARGE) {
            override fun create(): AdvancedDanmaku {
                return AdvancedDanmaku()
            }
        }

    fun ofType(type: Int): BasePool<Danmaku> {
        return when (type) {
            Danmaku.TYPE_RL -> rlPool
            Danmaku.TYPE_LR -> lrPool
            Danmaku.TYPE_TOP -> topPool
            Danmaku.TYPE_BOTTOM -> bottomPool
            Danmaku.TYPE_ADVANCED -> advPool
            else -> throw NoSuchElementException()
        } as BasePool<Danmaku>
    }

    abstract class BasePool<T : Danmaku?> internal constructor(private val maxPoolSize: Int) : Pools.Pool<T> {
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
