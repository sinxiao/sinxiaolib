
package ae.sinxiao.android.selectimage;

import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        if (file == null || file.length == 0) {
            throw new FileNotFoundException("Source file not found." + sourceDir);
        }
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            } else if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    // 递归删除文件及文件夹
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static long caculateFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isFile()) {
            return file.length();
        }

        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return 0;
        }
        long size = 0;
        for (int i = 0; i < listFiles.length; i++) {
            size += caculateFileSize(listFiles[i]);
        }
        return size;
    }

//    public static byte[] file2bytes(String filePath) {
//        if (TextUtils.isEmpty(filePath)) {
//            return null;
//        }
//
//        File file = new File(filePath);
//        return file2bytes(file);
//    }

//    public static byte[] file2bytes(File file) {
//        byte[] buffer = null;
//        if (file == null || !file.exists() || file.isDirectory()) {
//            LOG.logI("file don't exist or is dic!");
//            return null;
//        }
//        FileInputStream fis = null;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            fis = new FileInputStream(file);
//            byte[] b = new byte[1024];
//            int n;
//            while ((n = fis.read(b)) != -1) {
//                bos.write(b, 0, n);
//            }
//            buffer = bos.toByteArray();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            Utilities.silentlyClose(fis);
//            Utilities.silentlyClose(bos);
//        }
//        return buffer;
//    }

//    public static boolean bytes2file(String filePath, byte[] bytes, boolean append) {
//        if (TextUtils.isEmpty(filePath)) {
//            return false;
//        }
//        return bytes2file(new File(filePath), bytes, append);
//    }

//    public static boolean bytes2file(File file, byte[] bytes, boolean append) {
//        if (file == null) {
//            return false;
//        }
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file, append);
//            fos.write(bytes);
//            fos.flush();
//            return true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            Utilities.silentlyClose(fos);
//        }
//        return false;
//    }

    /**
     * 判断文件是否是图片
     *
     * @param filePath 文件路径
     * @return 是否是图片
     */
    public static boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return options.outWidth != -1;
    }
}
