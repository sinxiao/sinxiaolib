package ae.sinxiao.android.selectimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 图片处理工具类
 *
 * @author zhang.zheng
 * @version 2018-05-26
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    // 上传图片最大质量（KB）
    private static final int MAX_IMAGE_SIZE = 100;

    /**
     * 图片压缩
     */
    public static Bitmap compressImageSize(String imgPath) throws Exception {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(imgPath)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(imgPath)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /**
     * 读取本地图片位图
     * 该方式比较节省内存，可以防止内存溢出，在读取较大图片（图片裁剪）时非常有用。
     *
     * @param imgPath 图片路径
     * @param maxSize 压缩临界值（KB）
     */
    public static Bitmap decodeLocalImage(String imgPath, int maxSize) {
        try {
            // 1-解析图片位图
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inTempStorage = new byte[300 * 1024];// 设置300K的缓存
            options.inPreferredConfig = Config.RGB_565;// 颜色格式
            options.inPurgeable = true;// 内存不足时允许清除
            options.inInputShareable = true;
            if (new File(imgPath).length() <= maxSize * 1024) {
                options.inSampleSize = 1;// 文件大小小于临界值，不缩放
            } else {
                options.inSampleSize = 2;// 文件大小大于临界值，要缩放
            }
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imgPath), null, options);

            // 2-判断是否旋转（可解决部分手机拍照图片被旋转的问题）
            int degree = readImageRotateDegree(imgPath);
            if (degree != 0) {
                return rotateBitmap(bitmap, degree);
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析图片旋转角度
     */
    private static int readImageRotateDegree(String imgPath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imgPath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
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
            return degree;
        } catch (Exception e) {
            e.printStackTrace();
            return degree;
        }
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度（负值为逆时针旋转，正值为顺时针旋转）
     * @param bitmap 图片位图
     */
    private static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * 绘制圆角图片
     *
     * @param bitmap 位图
     * @param radius 半径
     */
    public static Bitmap drawCornerBitmap(Bitmap bitmap, float radius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);// 相当于清屏
        // 创建画笔
        final Paint paint = new Paint();
        paint.setAntiAlias(true);// 防止锯齿
        paint.setColor(Color.RED);
        // 创建与图像相同大小的区域(由构造的四个值决定区域的位置以及大小)
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        // 开始绘制圆角图形(第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角)
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // 最后把原来的bitmap画到现在的画布上
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 图片缩放（等比缩放）
     *
     * @param bmp   位图
     * @param scale 缩放比例（值小于1则为缩小，否则为放大）
     */
    public static Bitmap bitmapZoom(Bitmap bmp, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    /**
     * 图片缩放（固定比例）
     *
     * @param bmp 位图
     * @param w   缩放后宽
     * @param h   缩放后高
     */
    public static Bitmap bitmapZoom(Bitmap bmp, int w, int h) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
    }


    /**
     * 保存位图到本地
     */
//    public static String saveBitmapToLocal(Context context, Bitmap bitmap) {
//        String savePath = FileUtils.getImageCacheDir(context) + File.separator + System.currentTimeMillis() + ".jpg";
//        try {
//            FileOutputStream fos = new FileOutputStream(new File(savePath));
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//            fos.flush();
//            fos.close();
//            return savePath;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    /**
     * 转换上传图片
     * 此处是为解决因图片太大上传较慢或者失败的问题
     *
     * @param context  上下文
     * @param imgPath  图片路径
     * @param isDelete 转换成功后是否删除原图
     */
//    public static String convertUploadImage(Context context, String imgPath, boolean isDelete) {
//        File file = new File(imgPath);
//        Log.d(TAG, "原图片大小：" + file.length() / 1024 + "KB");
//        try {
//            if (file.length() / 1024 > MAX_IMAGE_SIZE) {
//                // 压缩后重新保存图片
//                Bitmap bitmap = compressImageSize(imgPath);
//                String newPath = FileUtils.getImageCacheDir(context) + File.separator + System.currentTimeMillis() + ".jpg";
//                if (saveBitmap2SD(bitmap, newPath)) {
//                    Log.d(TAG, "压缩后大小：" + new File(newPath).length() / 1024 + "KB");
//                    if (isDelete) {
//                        deleteImage(imgPath);
//                    }
//                    return newPath;
//                }
//            }
//            return imgPath;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return imgPath;
//        }
//    }

    /**
     * 保存位图到SD卡
     */
    private static boolean saveBitmap2SD(Bitmap bitmap, String savedPath) {
        File file = new File(savedPath);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除图片
     */
    public static boolean deleteImage(String imgPath) {
        File file = new File(imgPath);
        return file.delete();
    }


    /**
     * 压缩读取图片
     * 1、当图片宽度大于1000像素值会进行压缩处理
     *
     * @param path 图片路径
     * @return Bitmap
     */
    public static Bitmap readAndCompressImage(String path) {
        Bitmap bitmap;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1000)
                        && (options.outHeight >> i <= 1000)) {
                    in = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 压缩读取图片
     * 1、该方式比较节省内存，可以防止内存溢出，在读取较大图片（图片裁剪）时非常有用
     * 2、压缩临界值（单位KB）控制当图片大于多少时进行压缩处理
     *
     * @param path    图片路径
     * @param maxSize 压缩临界
     * @return Bitmap
     */
    public static Bitmap readAndCompressImage(String path, int maxSize) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            File file = new File(path);
            if (file.exists()) {
                long size = file.length() / 1024;
                if (size <= maxSize) {
                    // 读取原图
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } else {
                    // 图片压缩
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // 设置300K缓存
                    options.inTempStorage = new byte[300 * 1024];
                    // 图片格式
                    options.inPreferredConfig = Config.ARGB_8888;
                    // 内存不足时允许清除
                    options.inPurgeable = true;
                    options.inInputShareable = true;
                    // 压缩比
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                }
                // 2.判断是否旋转
//                int degree = readRotateDegree(path);
//                if (degree != 0) {
//                    // 可以解决部分手机拍照图片被旋转的问题
//                    return rotateBitmap(degree, bitmap);
//                }
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存位图到SD卡
     *
     * @param context 上下文
     * @param bitmap  位图图片
     * @return String 保存路径
     */
//    public static String saveBitmapToSD(Context context, Bitmap bitmap) {
//        // 图片默认保存路径
//        String imgSavedPath = FileUtils.getCacheDirectory(context,null) + File.separator + System.currentTimeMillis() + ".jpg";
//        FileOutputStream fos;
//        try {
//            File file = new File(imgSavedPath);
//            fos = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//            fos.flush();
//            fos.close();
//            Log.e(TAG, "头像路径：" + imgSavedPath);
//            return imgSavedPath;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

}
