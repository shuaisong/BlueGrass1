package com.reeching.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.bean.VideoEntity;
import com.reeching.core.CompressVideoThread;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by Administrator on 2017/3/10.
 */

public class FileUtils {
    private static String FILE_NAME = "userIcon.jpg";
    public static String PATH_PHOTOGRAPH = "/LXT/";

    public static String getFilePath(Context context, String dir) {
        String directoryPath;
        //判断SD卡是否可用 
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
        } else {
            //没内存卡就存机身内存  
            directoryPath = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在  
            file.mkdirs();
        }
        LogUtils.i("filePath====>" + directoryPath);
        return directoryPath;
    }

    public static void compressVideos(Context context, List<VideoEntity> list) {
        LogUtils.d(list.size() + "FileUtils.compressVideos");
        int cpu_count = Runtime.getRuntime().availableProcessors();
        int corePoolSize = Math.max(2, Math.min(cpu_count - 1, 4));
        int maximumPoolSize = 5;
        LogUtils.d("cpu_count:" + cpu_count);
        BlockingQueue<Runnable> workQueue = new SynchronousQueue<>();
        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "compressTask：" + mCount.getAndIncrement());
            }
        };
        RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0, TimeUnit.SECONDS, workQueue, factory, policy);
        for (VideoEntity v :
                list) {
            poolExecutor.execute(new CompressVideoThread(v));
        }
    }

    /**
     * 设置压缩质量
     *
     * @param position
     * @return
     */
    private static int getEncodingBitrateLevel(int position) {
        return ENCODING_BITRATE_LEVEL_ARRAY[position];
    }

    /**
     * 选的越高文件质量越大，质量越好
     */
    public static final int[] ENCODING_BITRATE_LEVEL_ARRAY = {
            320 * 240,
            500 * 1000,
            800 * 1000,
            1000 * 1000,
            1200 * 1000,
            1600 * 1000,
            2000 * 1000,
            2500 * 1000,//出错
            4000 * 1000,
            8000 * 1000,
    };

    public static String toTime(int time) {//毫秒
        time = time / 1000;
        int h = time / 3600;
        int m = (time - h * 3600) / 60;
        int s = (time - h * 3600) % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 0L ? "error" : (var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F)) + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F)) + "MB" : var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F)) + "GB")));
    }

    public static void deleteCompresses() {
        final String compressPath = getFilePath(BaseApplication.getInstance(), "video" + File.separator +
                "compress");
        File file = new File(compressPath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (file.length() <= 0) return;
            for (File f :
                    files) {
                boolean delete = f.delete();
                if (!delete) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 删除三天之前的图片缓存
     */
    public static void deleteImgCache() {
        final String compressPath =
                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "imageSelect" + File.separator +
                        "ImgCache";
        File file = new File(compressPath);
        LogUtils.d("deleteImgCache");
        if (file.exists() && file.isDirectory()) {
            LogUtils.d("deleteImgCache:isDirectory");
            File[] files = file.listFiles();
            if (file.length() <= 0) return;
            for (File f :
                    files) {
                try {
                    boolean delete = f.delete();
                    if (!delete) {
                        delete = f.delete();
                    }
                    LogUtils.d(delete + "delete");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
