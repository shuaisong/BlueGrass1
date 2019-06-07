package com.reeching.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by 绍轩 on 2018/4/10.
 */

public class PathUtil {
    public static final String videoPathName = "/video/";
    private static PathUtil instance = null;
    private File videoPath = null;

    private PathUtil() {
    }

    public static PathUtil getInstance() {
        if(instance == null) {
            instance = new PathUtil();
        }
        return instance;
    }

    public void initDirs() {
        this.videoPath = generateVideoPath();
        if(!this.videoPath.exists()) {
            this.videoPath.mkdirs();
        }

    }
    public File getVideoPath() {
        return this.videoPath;
    }
    private static File generateVideoPath() {
        String var2=null;
        File var1 = Environment.getExternalStorageDirectory();
        var2=var1+videoPathName;

        return new File(var2);
    }

}
