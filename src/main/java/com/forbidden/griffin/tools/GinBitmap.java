package com.forbidden.griffin.tools;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore.Video.Thumbnails;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * 图片工具（裁剪等）
 */
public class GinBitmap {
    private GinBitmap() {
    }

    /**
     * 把图片保存到本地
     *
     * @param bitmap
     * @param path
     */
    public static String saveBitmapToPath(Bitmap bitmap, String path, int quality, Bitmap.CompressFormat compressFormat) {
        if (null == bitmap) {
            return null;
        }
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            if (!file.getParentFile().exists()) {
                GinFile.createDir(file.getParentFile().getPath());
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(compressFormat, quality, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 把图片保存到本地(默认成png)
     *
     * @param bitmap
     * @param path
     */
    public static String saveBitmapToPath(Bitmap bitmap, String path, int quality) {
        return saveBitmapToPath(bitmap, path, quality, Bitmap.CompressFormat.PNG);
    }

    /**
     * 从View的DrawingCache中得到图片 传入一个View视图，然后把视图中的图片保存成Bitmap返回
     *
     * @param view 从该view中拿到图片
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    /**
     * 获取视频的缩略图 传入一个视频的地址，然后返回该视频的一个Bitmap对象的缩略图
     *
     * @param path 视频地址
     * @return
     */
    public static Bitmap getVideoThumbnail(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, Thumbnails.FULL_SCREEN_KIND);
    }

    /**
     * 按照给定大小缩放图片 把一个图片按照给定尺寸缩放
     *
     * @param bitmap 源图片
     * @param size   缩放后的大小
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int size) {
        return scaleBitmap(bitmap, size, size);
    }

    /**
     * 按照给定大小缩放图片 把一个图片按照给定尺寸缩放
     *
     * @param bitmap
     * @param tagWidth
     * @param tagHeight
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int tagWidth, int tagHeight) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) tagWidth) / width;
        float scaleHeight = ((float) tagHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBmp;
    }

    /**
     * @brief 裁剪Bitmap，自动回收原Bitmap
     */
    public static Bitmap cropBitmap(Bitmap src, int x, int y, int width, int height) {
        return cropBitmap(src, x, y, width, height, true);
    }

    /**
     * @param src       源Bitmap
     * @param x         开始x坐标
     * @param y         开始y坐标
     * @param width     截取宽度
     * @param height    截取高度
     * @param isRecycle 是否回收原图像
     * @return Bitmap
     * @brief 裁剪Bitmap
     */
    public static Bitmap cropBitmap(Bitmap src, int x, int y, int width, int height, boolean isRecycle) {
        if (x == 0 && y == 0 && width == src.getWidth() && height == src.getHeight()) {
            return src;
        }
        Bitmap dst = Bitmap.createBitmap(src, x, y, width, height);
        if (isRecycle && dst != src) {
            src.recycle();
        }
        return dst;
    }

    /**
     * 裁剪图片 按照给定的宽高从源图片的中点开始裁剪图片，返回一个Bitmap对象
     *
     * @param bitmap
     * @param tagWidth
     * @param tagHeight
     * @return
     */
    public static Bitmap cutBitmap(Bitmap bitmap, int tagWidth, int tagHeight) {
        if (null == bitmap) {
            return null;
        }
        Bitmap tmpBmp = Bitmap.createScaledBitmap(bitmap, tagWidth, tagHeight, false);
        return Bitmap.createBitmap(tmpBmp, 0, 0, tagWidth, tagHeight);
    }

    //    public static Bitmap cutBitmap

    /**
     * 裁剪图片 按照给定的宽高从源图片的中点开始裁剪图片，返回一个Bitmap对象
     *
     * @param bitmap
     * @param size
     * @return
     */
    public static Bitmap cutBitmap(Bitmap bitmap, int size) {
        return cutBitmap(bitmap, size, size);
    }

    /**
     * 裁剪图片成正方形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap cutBitmapToSquare(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /**
     * 将源图片转换成圆角图片
     *
     * @param src
     * @param radius
     * @return
     */
    public static Bitmap conversionBitmapToFillet(Bitmap src, float radius) {
        if (null == src) {
            return null;
        }
        if (radius == 0) {
            return src;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        //        src.recycle();
        return newBitmap;
    }

    /**
     * 合并两张bitmap为一张(一张在上面，一张紧接着上面一张）
     *
     * @param first
     * @param second
     * @return Bitmap
     */
    public static Bitmap combineBitmap(Bitmap first, Bitmap second) {
        if (first == null) {
            return null;
        }
        int fWidth = first.getWidth();
        int fHeight = first.getHeight();
        int sWidth = second.getWidth();
        int sHeight = second.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(fWidth, fHeight + sHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, fHeight, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBmp;
    }

    /**
     * 计算图片的缩放值 当一个BitmapFactory.Options有了输出的图片大小，然后根据这个大小和传入的宽高计算出缩放的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 把源Bitmap缩放到目标的宽高 先把源图片缩放到目标大小，然后再裁剪宽或高多余的部分；该操作至少保证了宽或高其中一个和源图片是等比例缩放
     *
     * @return
     */
    public static Bitmap scaleBitmapToSize(Bitmap bitmap, int tagWidth, int tagHeight) {
        if (null == bitmap || tagWidth < 0 || tagHeight < 0) {
            return bitmap;
        }
        final int WIDTH_T = 1; // 需要保持不变的属性
        final int HEIGHT_T = 2;
        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        float ratio;// 缩放的比例
        // 第一步：计算缩放值
        int property;
        float ratioWidth = (1.0f) * tagWidth / srcWidth;
        float ratioHeight = (1.0f) * tagHeight / srcHeight;
        ratio = ratioWidth < ratioHeight ? ratioHeight : ratioWidth;
        property = ratioWidth < ratioHeight ? HEIGHT_T : WIDTH_T;
        // 裁剪后的宽高
        float outWidth = srcWidth * ratio;
        float outHeight = srcHeight * ratio;
        // 第二步:新建一个缩放后的图片
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) Math.ceil(outWidth), (int) Math.ceil(outHeight), true);
        // 第三步:裁剪图片
        Bitmap newBmp;
        if (property == WIDTH_T) {// 裁剪高
            newBmp = Bitmap.createBitmap(scaledBitmap, 0, (int) ((scaledBitmap.getHeight() - tagHeight) * 0.5f), tagWidth, tagHeight);
        } else {// 裁剪宽
            newBmp = Bitmap.createBitmap(scaledBitmap, (int) ((scaledBitmap.getWidth() - tagWidth) * 0.5f), 0, tagWidth, tagHeight);
        }
        return newBmp;
    }

    /**
     * 释放图片资源
     *
     * @param bitmap
     */
    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 释放图片资源
     *
     * @param bitmaps
     */
    public static void recycle(ArrayList<Bitmap> bitmaps) {
        for (Bitmap bmp : bitmaps) {
            recycle(bmp);
        }
    }

    /**
     * 获取图片流
     *
     * @param uri 图片地址
     * @return
     * @throws MalformedURLException
     */
    public static InputStream getImageByUrl(String uri) throws MalformedURLException {
        URL url = new URL(uri);
        URLConnection conn;
        InputStream is;
        try {
            conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            // 或者用如下方法
            // is=(InputStream)url.getContent();
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Bitmap
     *
     * @param uri 图片地址
     * @return
     */
    public static Bitmap getBitmapByUrl(String uri) {
        if (android.text.TextUtils.isEmpty(uri)) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream is;
        try {
            is = getImageByUrl(uri);
            if (null == is) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;// 宽度和高度设置为原来的1/2
            // options.outWidth = SysToolUtils.pxToDip(240, 360);
            // options.outHeight = SysToolUtils.pxToDip(240, 240);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            is.close();
            // bitmap = GinUBitmap.scaleBitmapToSize(bitmap, 480, 800);
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // try
            // {
            // if(null != bitmap)
            // {
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // int tagWidth = 320;
            // if(bitmap.getWidth() > tagWidth)
            // {
            // int height = bitmap.getHeight() * tagWidth / bitmap.getWidth();
            // bitmap = GinUBitmap.cutBitmap(bitmap, tagWidth, height);
            // }
            // }
            // } finally
            // {
            // try
            // {
            // if(baos != null)
            // {
            // baos.close();
            // }
            // } catch(IOException e)
            // {
            // e.printStackTrace();
            // }
            // }
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 高斯模糊图片
     *
     * @param bmp
     * @return
     */
    public static Drawable boxBlurFilter(Bitmap bmp) {
        /** 水平方向模糊度 */
        float hRadius = 2;
        /** 竖直方向模糊度 */
        float vRadius = 2;
        /** 模糊迭代度 */
        int iterations = 5;
        return GinBitmapHelper.boxBlurFilter(bmp, hRadius, vRadius, iterations);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blurBitmap(Context context, Bitmap bitmap) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //Set the radius of the blur
        blurScript.setRadius(25.f);
        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        //recycle the original bitmap
        bitmap.recycle();
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Context ac, Uri uri) throws FileNotFoundException, IOException {

        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }


    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 通过Uri获取文件
     *
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if (uri.getScheme().toString().compareTo("content") == 0) {
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0) {
            return new File(uri.toString().replace("file://", ""));
        }
        return null;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
