package com.reeching.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */

public class AllHuaLangHistory implements Serializable {

    private String result;

    private String msg;

    private List<AllHuaLangHistory.Infos> infos;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setInfos(List<AllHuaLangHistory.Infos> infos) {
        this.infos = infos;
    }

    public List<AllHuaLangHistory.Infos> getInfos() {
        return this.infos;
    }

    public class Infos implements Serializable {
        String galleryId;        // 画廊id
        String theme;        // 展览主题
        String status;        // 状态  1:已完成  0：进行中
        Date dateBegin;        // 开始时间
        Date dateEnd;        // 结束时间
        String careLevel;        //关注等级  0:低 1：中 2：高
        String photo;        // 作品介绍
        String smallPhoto;
        String remarks;
        String photoFalse;
        String author;        // 作者
        String authorIntroduction;        // 作者简介
        String manager;        // 策展人
        String managerIntroduction;        // 策展人简介
        String galleryName;   //画廊名称
        String userId;   //上报人id
        String userName;    //上报人姓名
        String mapLng;    //经度
        String mapLat;    //纬度
        String address;   //地址
        String checkStatus;  //0：检查 1：核查
        String id;
        private String worksCount;
        private String videoCount;
        private String sculptureCount;
        private String deviceCount;
        private String otherDesc;

        public String getWorksCount() {
            return worksCount;
        }

        public void setWorksCount(String worksCount) {
            this.worksCount = worksCount;
        }

        public String getVideoCount() {
            return videoCount;
        }

        public void setVideoCount(String videoCount) {
            this.videoCount = videoCount;
        }

        public String getSculptureCount() {
            return sculptureCount;
        }

        public void setSculptureCount(String sculptureCount) {
            this.sculptureCount = sculptureCount;
        }

        public String getDeviceCount() {
            return deviceCount;
        }

        public void setDeviceCount(String deviceCount) {
            this.deviceCount = deviceCount;
        }

        public String getOtherDesc() {
            return otherDesc;
        }

        public void setOtherDesc(String otherDesc) {
            this.otherDesc = otherDesc;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        String videoUrl;

        public String getGalleryId() {
            return galleryId;
        }

        public void setGalleryId(String galleryId) {
            this.galleryId = galleryId;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Date getDateBegin() {
            return dateBegin;
        }

        public void setDateBegin(Date dateBegin) {
            this.dateBegin = dateBegin;
        }

        public Date getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(Date dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getCareLevel() {
            return careLevel;
        }

        public void setCareLevel(String careLevel) {
            this.careLevel = careLevel;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getAuthor() {
            return author;
        }

        public String getPhotoFalse() {
            return photoFalse;
        }

        public String getSmallPhoto() {
            return smallPhoto;
        }

        public void setSmallPhoto(String smallPhoto) {
            this.smallPhoto = smallPhoto;
        }

        public void setPhotoFalse(String photoFalse) {
            this.photoFalse = photoFalse;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorIntroduction() {
            return authorIntroduction;
        }

        public void setAuthorIntroduction(String authorIntroduction) {
            this.authorIntroduction = authorIntroduction;
        }

        public String getManager() {
            return manager;
        }

        public void setManager(String manager) {
            this.manager = manager;
        }

        public String getManagerIntroduction() {
            return managerIntroduction;
        }

        public void setManagerIntroduction(String managerIntroduction) {
            this.managerIntroduction = managerIntroduction;
        }

        public String getGalleryName() {
            return galleryName;
        }

        public void setGalleryName(String galleryName) {
            this.galleryName = galleryName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMapLng() {
            return mapLng;
        }

        public void setMapLng(String mapLng) {
            this.mapLng = mapLng;
        }

        public String getMapLat() {
            return mapLat;
        }

        public void setMapLat(String mapLat) {
            this.mapLat = mapLat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(String checkStatus) {
            this.checkStatus = checkStatus;
        }
    }
}
