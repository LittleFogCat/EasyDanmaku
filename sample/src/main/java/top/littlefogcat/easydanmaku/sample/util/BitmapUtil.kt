package top.littlefogcat.easydanmaku.sample.util

import android.content.res.Resources
import android.graphics.*
import android.util.Log
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

    /**
     * 将原bitmap裁剪成正方形
     *
     * @param src 原bitmap
     * @return 正方形的bitmap
     */
    fun centerCropSquare(src: Bitmap): Bitmap {
        val w = src.width
        val h = src.height
        println("$w,$h")
        return if (w == h) {
            src
        } else if (w > h) {
            Bitmap.createBitmap(src, (w - h) / 2, 0, h, h)
        } else {
            Bitmap.createBitmap(src, 0, (h - w) / 2, w, w)
        }
    }

    /**
     * 创建圆形的Bitmap
     *
     * @param src 原Bitmap
     * @return 创建的圆形Bitmap
     */
    fun createRoundBitmap(src: Bitmap): Bitmap {
        val squareBmp = if (src.width == src.height) {
            src
        } else {
            centerCropSquare(src) // 裁剪成正方形
        }
        val size = squareBmp.width
        val radius = size / 2f
        val circleBmp = Bitmap.createBitmap(size, size, squareBmp.config)
        circleBmp.density = squareBmp.density // <======= 必须保证二者density相同，否则显示异常！
        val canvas = Canvas(circleBmp)
        val paint = Paint().apply {
            isAntiAlias = true
        }
        canvas.drawCircle(radius, radius, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(squareBmp, 0f, 0f, paint)
        return circleBmp
    }

}