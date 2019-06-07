package com.reeching.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageCacheUtil {

    private static final String tag = "ImageCacheUtil";

    // 缓存文件路径
    private static File cacheDir;

    // key:图片链接地址 value:图片对象
    private static LruCache<String, Bitmap> lruCache;


    public ImageCacheUtil(Context context) {

        // cache路径
        cacheDir = context.getCacheDir();

        // 运行内存大小
        long maxMemory = Runtime.getRuntime().maxMemory();

        // lru缓存大小为运行内存大小的8分之1
        int cacheSize = (int) (maxMemory / 8);
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // value.getRowBytes()获取一行中图片对应占用的字节数
                // value.getHeight()，获取行号
                return value.getRowBytes() * value.getHeight();

                // 图片可以去指明占用多少位
                // 16,32
                // ARGB_4444 占用16位，每一个像素点占用16位表示2个字节
                // 800*480*16/8/1024---> kb
            }
        };
    }

    // 提供一个获取bitmap的方法
    public Bitmap getBitmap(String imgUrl) {
        // 1.内存
        Bitmap bitmap = lruCache.get(imgUrl);
        Log.i(tag, imgUrl);
        if (bitmap != null) {
            Log.i(tag, "从内存中取得的图片");
            return bitmap;
        }
        // 2.文件
        bitmap = getBitmapFromFile(imgUrl);
        if (bitmap != null) {
            Log.i(tag, "从文件中取得的图片");
            return bitmap;
        }
        // 3.网络
        // 请求图片的链接地址
        getBitmapFromNet(imgUrl);
        Log.i(tag, "从网络中取得的图片");
        return null;// 可能是为空的，调用要判断
    }

    private void getBitmapFromNet(String imgUrl) {
        new Video_Img_Async().execute(imgUrl);
    }

    static class Video_Img_Async extends AsyncTask<String, Void, Bitmap> {
        private String url;

        @Override
        protected Bitmap doInBackground(String... strings) {
            url = strings[0];
            return BitmapUtils.getVideoBitmap2(strings[0]);
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // 将下载的文件存到文件
            writeToFile(url, bitmap);
            Log.i(tag, "将图片存入文件成功！");

            // 将下载的文件存到内存(LRU算法)
            lruCache.put(url, bitmap);
            Log.i(tag, "将图片存入内存成功！");
        }
    }

    /**
     * 将图片写到文件
     */
    static void writeToFile(String imgUrl, Bitmap bitmap) {
        // imgUrl地址对图片文件进行存储操作，cache文件夹

        try {
            String fileName = Utils.md5(imgUrl).substring(10);
            if (!new File(fileName).exists()) {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(new File(cacheDir, fileName)));
                // 将图片存储到指定文件中
                bitmap.compress(CompressFormat.JPEG, 100, bos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * 将图片从文件中取出
     */
    private Bitmap getBitmapFromFile(String imgUrl) {
        String fileName = Utils.md5(imgUrl).substring(10);
        File file = new File(cacheDir, fileName);

        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

            // 在文件中获取到了图片以后还需要将图片放到内存中
            if (imgUrl != null && bitmap != null) {
                lruCache.put(imgUrl, bitmap);
                Log.i(tag, "文件中存入内存");
            }
            return bitmap;
        }
        return null;
    }
}
