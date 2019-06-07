package com.reeching.bean;

import java.io.Serializable;
import java.util.List;

public class HechaInfobean implements Serializable {
    private String result;

    private String msg;

    private List<Infos> infos;

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

    public void setInfos(List<Infos> infos) {
        this.infos = infos;
    }

    public List<Infos> getInfos() {
        return this.infos;
    }

    public class Infos implements Serializable {
        private Exhibition exhibition;
        private CheckInfo checkInfo;

        public CheckInfo getCheckInfo() {
            return checkInfo;
        }

        public void setCheckInfo(CheckInfo checkInfo) {
            this.checkInfo = checkInfo;
        }

        public void setExhibition(Exhibition exhibition) {
            this.exhibition = exhibition;
        }

        public Exhibition getExhibition() {
            return this.exhibition;
        }

        public class CheckInfo implements Serializable {
            String exhibitionId;
            String checkPhoto;
            String checkDescription;
            String checkStatus;
            String checkBegin;
            String checkEnd;
            String id;

            public String getQuestionRemarks() {
                return questionRemarks;
            }

            public void setQuestionRemarks(String questionRemarks) {
                this.questionRemarks = questionRemarks;
            }

            String questionRemarks;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getExhibitionId() {
                return exhibitionId;
            }

            public void setExhibitionId(String exhibitionId) {
                this.exhibitionId = exhibitionId;
            }

            public String getCheckPhoto() {
                return checkPhoto;
            }

            public void setCheckPhoto(String checkPhoto) {
                this.checkPhoto = checkPhoto;
            }

            public String getCheckDescription() {
                return checkDescription;
            }

            public void setCheckDescription(String checkDescription) {
                this.checkDescription = checkDescription;
            }

            public String getCheckStatus() {
                return checkStatus;
            }

            public void setCheckStatus(String checkStatus) {
                this.checkStatus = checkStatus;
            }

            public String getCheckBegin() {
                return checkBegin;
            }

            public void setCheckBegin(String checkBegin) {
                this.checkBegin = checkBegin;
            }

            public String getCheckEnd() {
                return checkEnd;
            }

            public void setCheckEnd(String checkEnd) {
                this.checkEnd = checkEnd;
            }
        }

        public class Exhibition implements Serializable {
            private String id;

            private boolean isNewRecord;

            private String remarks;

            private String createDate;

            private String updateDate;

            private String galleryId;

            private String theme;

            private String userId;

            private String status;

            private String dateBegin;

            private String dateEnd;

            private String careLevel;

            private String photo;

            private String author;

            private String authorIntroduction;

            private String manager;

            private String managerIntroduction;

            private String galleryName;

            private String userName;

            private String mapLng;

            private String mapLat;

            private String address;
            private String questionRemarks;

            public String getQuestionRemarks() {
                return questionRemarks;
            }

            public void setQuestionRemarks(String questionRemarks) {
                this.questionRemarks = questionRemarks;
            }

            private String checkStatus;
            private String area;
            private String exhibitionIntroduction;
            private String videoUrl;
            private int isFocus;//0无1有
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

            public int getIsFocus() {
                return isFocus;
            }

            public void setIsFocus(int isFocus) {
                this.isFocus = isFocus;
            }

            public String getFocusName() {
                return focusName;
            }

            public void setFocusName(String focusName) {
                this.focusName = focusName;
            }

            private String focusName;

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getExhibitionIntroduction() {
                return exhibitionIntroduction;
            }

            public void setExhibitionIntroduction(String exhibitionIntroduction) {
                this.exhibitionIntroduction = exhibitionIntroduction;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public boolean getIsNewRecord() {
                return this.isNewRecord;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public String getRemarks() {
                return this.remarks;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getCreateDate() {
                return this.createDate;
            }

            public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
            }

            public String getUpdateDate() {
                return this.updateDate;
            }

            public void setGalleryId(String galleryId) {
                this.galleryId = galleryId;
            }

            public String getGalleryId() {
                return this.galleryId;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public String getTheme() {
                return this.theme;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserId() {
                return this.userId;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setDateBegin(String dateBegin) {
                this.dateBegin = dateBegin;
            }

            public String getDateBegin() {
                return this.dateBegin;
            }

            public void setDateEnd(String dateEnd) {
                this.dateEnd = dateEnd;
            }

            public String getDateEnd() {
                return this.dateEnd;
            }

            public void setCareLevel(String careLevel) {
                this.careLevel = careLevel;
            }

            public String getCareLevel() {
                return this.careLevel;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public String getPhoto() {
                return this.photo;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getAuthor() {
                return this.author;
            }

            public void setAuthorIntroduction(String authorIntroduction) {
                this.authorIntroduction = authorIntroduction;
            }

            public String getAuthorIntroduction() {
                return this.authorIntroduction;
            }

            public void setManager(String manager) {
                this.manager = manager;
            }

            public String getManager() {
                return this.manager;
            }

            public void setManagerIntroduction(String managerIntroduction) {
                this.managerIntroduction = managerIntroduction;
            }

            public String getManagerIntroduction() {
                return this.managerIntroduction;
            }

            public void setGalleryName(String galleryName) {
                this.galleryName = galleryName;
            }

            public String getGalleryName() {
                return this.galleryName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserName() {
                return this.userName;
            }

            public void setMapLng(String mapLng) {
                this.mapLng = mapLng;
            }

            public String getMapLng() {
                return this.mapLng;
            }

            public void setMapLat(String mapLat) {
                this.mapLat = mapLat;
            }

            public String getMapLat() {
                return this.mapLat;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAddress() {
                return this.address;
            }

            public void setCheckStatus(String checkStatus) {
                this.checkStatus = checkStatus;
            }

            public String getCheckStatus() {
                return this.checkStatus;
            }

        }

    }
}
