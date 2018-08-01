package top.littlefogcat.danmakulib.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

public class ImageUtil {
    public static final String TAG = "imageUtil";

    private static RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            // important to return false so the error placeholder can be placed
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    public static void loadImageNotCache(Context context, String url, ImageView view) {
        Glide.with(context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(view);
    }

    public static void loadImageNotCacheInMeory(Context context, String url, ImageView view) {
        Glide.with(context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
    }

    public static void loadImage(Context context, String url, ImageView view) {
        Log.v(TAG, "loadImage: " + url);
        if (context == null || url == null || view == null) {
            Log.w(TAG, String.format("loadImage: (%s, %s, %s)", context, url, view));
            return;
        }
        if (url.toLowerCase().endsWith("gif")) {
            Glide.with(context).load(url).asGif().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
        } else {
            Glide.with(context).load(url).listener(requestListener).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
        }
    }

    public static void loadImage(Context context, String url, Target<GlideDrawable> target) {
        Glide.with(context).load(url).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(target);
    }

    public static void loadImageOverride(Context context, String url, ImageView view, int width, int height) {
        Glide.with(context).load(url).override(width, height)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadImageCorners(Context context, String url, ImageView view, int radius) {
        Glide.with(context).load(url)
                .transform(new CornersTransform(context, radius))
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadCircleImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
                .transform(new GlideCircleTransform(context))
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadImageBlur(Context context, String url, final ImageView view, int radius, Priority priority) {
        Glide.with(context)
                .load(url)
                .transform(new BlurTransformation(context, radius, 4))
                .priority(priority)
                .skipMemoryCache(true)
                .crossFade(150)
                .into(new CustomTarget(view));
    }

    public static void loadImageBlurWithHighPriority(Context context, String url, ImageView view, int radius) {
        loadImageBlur(context, url, view, radius, Priority.HIGH);
    }

    public static void loadImageThumbnail(Context context, String url, ImageView view, float thumbnail) {
        Glide.with(context)
                .load(url).thumbnail(thumbnail)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadImageWithAnimate(Context context, String url, ImageView view, int resAnimate) {
        Glide.with(context)
                .load(url).animate(resAnimate)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadImageRotate(Context context, String url, ImageView view, float rotateRotationAngle) {
        Glide.with(context)
                .load(url).transform(new RotateTransformation(context, rotateRotationAngle))
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }

    public static void loadImageTransform(Context context, String url, ImageView view, BitmapTransformation... transforms) {
        Glide.with(context)
                .load(url).transform(transforms)
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }


    public static class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    public static class CropCircleTransformation extends BitmapTransformation {

        public CropCircleTransformation(Context context) {
            super(context);
        }

        private Bitmap cropCircle(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());

            int width = (source.getWidth() - size) / 2;
            int height = (source.getHeight() - size) / 2;
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            BitmapShader shader =
                    new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            if (width != 0 || height != 0) {
                Matrix matrix = new Matrix();
                matrix.setTranslate(-width, -height);
                shader.setLocalMatrix(matrix);
            }
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return "CropCircleTransformation()";
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return cropCircle(pool, toTransform);
        }
    }

    public static class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        public RotateTransformation(Context context, float rotateRotationAngle) {
            super(context);

            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotateRotationAngle);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public String getId() {
            return "rotate" + rotateRotationAngle;
        }
    }

    public static class RSBlur {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        public static Bitmap blur(Context context, Bitmap bitmap, int radius) throws RSRuntimeException {
            RenderScript rs = null;
            Allocation input = null;
            Allocation output = null;
            ScriptIntrinsicBlur blur = null;
            try {
                rs = RenderScript.create(context);
                rs.setMessageHandler(new RenderScript.RSMessageHandler());
                input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
                output = Allocation.createTyped(rs, input.getType());
                blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

                blur.setInput(input);
                blur.setRadius(radius);
                blur.forEach(output);
                output.copyTo(bitmap);
            } finally {
                if (rs != null) {
                    rs.destroy();
                }
                if (input != null) {
                    input.destroy();
                }
                if (output != null) {
                    output.destroy();
                }
                if (blur != null) {
                    blur.destroy();
                }
            }

            return bitmap;
        }
    }

    public static class CustomTarget extends SimpleTarget<GlideDrawable> {

        private ImageView view;

        public CustomTarget(ImageView view) {
            this.view = view;
        }

        @Override
        public void onResourceReady(GlideDrawable resource,
                                    GlideAnimation<? super GlideDrawable> glideAnimation) {
            glideAnimation.animate(resource, new GlideAnimation.ViewAdapter() {

                @Override
                public void setDrawable(Drawable arg0) {
                    view.setImageDrawable(arg0);
                }

                @Override
                public View getView() {
                    return view;
                }

                @Override
                public Drawable getCurrentDrawable() {
                    return view.getDrawable() == null ? new ColorDrawable(0x0) : view.getDrawable();
                }
            });
        }


    }

    public static class CornersTransform extends BitmapTransformation {
        private float radius;

        public CornersTransform(Context context) {
            super(context);
            radius = 10;
        }

        public CornersTransform(Context context, float radius) {
            super(context);
            this.radius = radius;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return cornersCrop(pool, toTransform);
        }

        private Bitmap cornersCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    public static class VodGlideModule implements GlideModule {

        @Override
        public void applyOptions(Context context, GlideBuilder builder) {
            //设置磁盘缓存大小
            MemorySizeCalculator calculator = new MemorySizeCalculator(context);
            int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
            int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

            int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
            int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

            builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
            builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

            String dirName = "glide_images";
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, dirName, 2097152));
        }

        @Override
        public void registerComponents(Context context, Glide glide) {

        }
    }

    public static class BlurTransformation extends BitmapTransformation {

        private static int MAX_RADIUS = 25;
        private static int DEFAULT_DOWN_SAMPLING = 1;

        private Context mContext;
        private BitmapPool mBitmapPool;

        private int mRadius;
        private int mSampling;

        public BlurTransformation(Context context) {
            this(context, Glide.get(context).getBitmapPool(), MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
        }

        public BlurTransformation(Context context, BitmapPool pool) {
            this(context, pool, MAX_RADIUS, DEFAULT_DOWN_SAMPLING);
        }

        public BlurTransformation(Context context, BitmapPool pool, int radius) {
            this(context, pool, radius, DEFAULT_DOWN_SAMPLING);
        }

        public BlurTransformation(Context context, int radius) {
            this(context, Glide.get(context).getBitmapPool(), radius, DEFAULT_DOWN_SAMPLING);
        }

        public BlurTransformation(Context context, int radius, int sampling) {
            this(context, Glide.get(context).getBitmapPool(), radius, sampling);
        }

        public BlurTransformation(Context context, BitmapPool pool, int radius, int sampling) {
            super(context);
            mContext = context.getApplicationContext();
            mBitmapPool = pool;
            mRadius = radius;
            mSampling = sampling;
        }

        @Override
        public Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return toBlur(pool, toTransform);
        }

        private Bitmap toBlur(BitmapPool pool, Bitmap source) {
            int width = source.getWidth();
            int height = source.getHeight();
            int scaledWidth = width / mSampling;
            int scaledHeight = height / mSampling;

            Bitmap bitmap = mBitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(source, 0, 0, paint);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                try {
                    bitmap = RSBlur.blur(mContext, bitmap, mRadius);
                } catch (RSRuntimeException e) {
                    bitmap = FastBlur.blur(bitmap, mRadius, true);
                }
            } else {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            }

            return bitmap;
        }

        @Override
        public String getId() {
            return "BlurTransformation(radius=" + mRadius + ", sampling=" + mSampling + ")";
        }

    }

    public static class FastBlur {

        public static Bitmap blur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

            Bitmap bitmap;
            if (canReuseInBitmap) {
                bitmap = sentBitmap;
            } else {
                bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            }

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            bitmap.setPixels(pix, 0, w, 0, 0, w, h);

            return (bitmap);
        }
    }
}
