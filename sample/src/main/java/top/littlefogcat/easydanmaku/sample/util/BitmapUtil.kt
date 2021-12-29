package top.littlefogcat.easydanmaku.sample.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.min

/**
 * @author littlefogcat
 * @email littlefogcat@foxmail.com
 */
object BitmapUtil {
    fun decodeResourceWithSize(resources: Resources, id: Int, targetWidth: Float, targetHeight: Float): Bitmap {
        // 仅加载尺寸
        val opt = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, id, opt)
        // 原图尺寸
        val bmpw = opt.outWidth
        val bmph = opt.outHeight
        // 目标尺寸
        // 缩放比例
        val targetScale = min(targetWidth / bmpw, targetHeight / bmph)

        // 计算inSampleSize
        var inSample = 1
        while (bmpw / inSample > targetWidth || bmph / inSample > targetHeight) {
            inSample *= 2
        }
        opt.inSampleSize = inSample

        // 计算inTargetDensity
        // 输出图片的宽高 = 原图片的宽高 * (inTargetDensity / inDensity) / inSampleSize
        // 所以：inTargetDensity = 输出图片的宽高 / 原图片的宽高 * inDensity * inSampleSize
        opt.inTargetDensity = (targetScale * opt.inDensity * opt.inSampleSize).toInt()
        opt.inJustDecodeBounds = false
        // 按照需要的尺寸加载最终bmp
        return BitmapFactory.decodeResource(resources, id, opt)
    }

    /** 获取Bitmap尺寸 **/
    fun measureResource(resources: Resources, id: Int): BitmapFactory.Options {
        val opt = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(resources, id, opt)
        return opt
    }
}