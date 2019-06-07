package com.reeching.core;

import com.lidroid.xutils.util.LogUtils;
import com.reeching.BaseApplication;
import com.reeching.bean.VideoEntity;
import com.reeching.utils.FileUtils;
import com.reeching.utils.MediaControl;
import com.reeching.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by lenovo on 2018/11/8.
 * auther:lenovo
 * Date：2018/11/8
 */
public class CompressVideoThread implements Runnable {
    private VideoEntity videoEntity;

    public CompressVideoThread(VideoEntity videoEntity) {
        this.videoEntity = videoEntity;
    }

    @Override
    public void run() {
        try {
            LogUtils.i("currentThread:" + Thread.currentThread().getName());
            LogUtils.i("videoEntity.filePath:" + videoEntity.filePath);
            if (Utils.ReadVideoSize(videoEntity.filePath) <= 5 * 1024 * 1024 ) {
                videoEntity.compress = true;
                videoEntity.size = Utils.ReadVideoSize(videoEntity.filePath);
                EventBus.getDefault().post(videoEntity);
                return;
            }
            MediaControl mediaControl = new MediaControl();
            int width;
            int hight;
            if (videoEntity.type == 1) {
                width = 1080;
                hight = 960;
            } else {
                width = 960;
                hight = 720;
            }
            boolean isconverted = mediaControl.convertVideo(videoEntity.filePath,
                    new File(FileUtils.getFilePath(BaseApplication.getInstance()
                            , "video" + File.separator + "compress")),
                    width, hight, 1000000);
            if (isconverted) {
                LogUtils.d(videoEntity.filePath);
                LogUtils.d(mediaControl.cachedFile.getPath());
                videoEntity.compress = true;
                videoEntity.filePath = mediaControl.cachedFile.getPath();
                videoEntity.size = Utils.ReadVideoSize(videoEntity.filePath);
                EventBus.getDefault().post(videoEntity);
            }

        } catch (Exception e) {
            videoEntity.compress = false;
            EventBus.getDefault().post(videoEntity);
            LogUtils.e(e.getMessage());
        }
    }
}
