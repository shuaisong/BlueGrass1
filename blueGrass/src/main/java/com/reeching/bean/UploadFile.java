package com.reeching.bean;

import com.yancy.imageselector.CameraImage;

/**
 * Created by lenovo on 2018/10/11.
 * auther:lenovo
 * Date：2018/10/11
 */
public class UploadFile {
    private String path;
    private int type;//0:图片；2：视频
    private VideoEntity mVideoEntity;

    public VideoEntity getVideoEntity() {
        return mVideoEntity;
    }

    public void setVideoEntity(VideoEntity videoEntity) {
        mVideoEntity = videoEntity;
    }

    public CameraImage getCameraImage() {
        return mCameraImage;
    }

    public void setCameraImage(CameraImage cameraImage) {
        mCameraImage = cameraImage;
    }

    private CameraImage mCameraImage;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
