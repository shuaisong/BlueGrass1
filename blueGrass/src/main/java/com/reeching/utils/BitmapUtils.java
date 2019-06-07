
package com.reeching.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;

import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.bluegrass.R;
import com.reeching.core.SDCardStoragePath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * <p>
 * 图片压缩类
 * Created by 王健(Jarek) on 2016/9/12.
 */
public class BitmapUtils {

    private static File sPhotoFile;

    /**
     * <li> 压缩图片 </li>
     */
    public static File commpressImage(String filePath) {
        try {
            Bitmap bitmap = getSmallBitmap(filePath);
            File file = new File(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH + "/" + UUID.randomUUID().toString()
                    + ".jpg");

            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                out.flush();
                out.close();
            }
            file.length();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File commpressImage2(String filePath) {
        try {
            Bitmap bitmap = getSmallBitmap2(filePath);
//            File file = new File(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH + "/" + UUID.randomUUID().toString() + ".jpg");
            File file = new File(SDCardStoragePath.DEFAULT_IMAGE_CACHE_PATH + "/" + filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."))
                    + ".jpg");
//            File file = new File("shuaishuai"+ "/" + UUID.randomUUID().toString()
//                    + ".jpeg");
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists())
                file.createNewFile();
            else return file;
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)) {
                out.flush();
                out.close();
            }
            file.length();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    public static Bitmap getVideoBitmap(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            return retriever.getFrameAtTime();
        } catch (Exception e) {
            return null;
        } finally {
            retriever.release();
        }
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static Bitmap getVideoBitmap1(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
    }

    public static Bitmap getVideoBitmap2(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //获取网络视频
            retriever.setDataSource(path, new HashMap<String, String>());
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
            return BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.drawable.error);
        }
        return retriever.getFrameAtTime();
    }

    /**
     * <li> 计算图片的缩放值 </li>
     *
     * @param options   options
     * @param reqWidth  宽
     * @param reqHeight 高
     * @return inSampleSize
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
     * <li> 根据路径获得图片并压缩，返回bitmap </li>
     *
     * @param filePath filePath
     * @return Bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 300, 300);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap getSmallBitmap2(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 900, 900);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {

        int w = bitmap.getWidth();

        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidth = ((float) width / w);

        float scaleHeight = ((float) height / h);

        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

        return newbmp;
    }


    public static File savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {

        if (android.os.Environment.getExternalStorageState().equals(

                android.os.Environment.MEDIA_MOUNTED)) {

            File dir = new File(path);

            if (!dir.exists()) {

                dir.mkdirs();

            }

            //在指定路径下创建文件
            sPhotoFile = new File(path, photoName);

            FileOutputStream fileOutputStream = null;

            try {

                fileOutputStream = new FileOutputStream(sPhotoFile);

                if (photoBitmap != null) {

                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,

                            fileOutputStream)) {

                        fileOutputStream.flush();

                    }

                }

            } catch (FileNotFoundException e) {

                sPhotoFile.delete();

                e.printStackTrace();

            } catch (IOException e) {

                sPhotoFile.delete();

                e.printStackTrace();

            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sPhotoFile;
    }
}
