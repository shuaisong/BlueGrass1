package com.reeching.bean;

import java.util.List;

/**
 * Created by lenovo on 2018/11/15.
 * auther:lenovo
 * Date：2018/11/15
 */
public class HeChaBean {

    /**
     * result : 1
     * msg : 获取成功！
     * infos : []
     */

    private String result;
    private String msg;
    private List<?> infos;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getInfos() {
        return infos;
    }

    public void setInfos(List<?> infos) {
        this.infos = infos;
    }
    public static class Info{

        /**
         * exhibition : {"id":"82d1668c7ad54c3e9fa369d878dcb626","isNewRecord":false,"remarks":"","createDate":"2018-11-15 15:35:07","updateDate":"2018-11-15 15:35:08","galleryId":"24b9715c0b074aa3914ee771a5632007","theme":"test","userId":"1","status":"2","dateBegin":"2018-11-16","dateEnd":"2018-11-17","careLevel":"2","photo":"","photoFalse":"","smallPhoto":"","smallPhotoFalse":"","author":"","authorIntroduction":"","manager":"","managerIntroduction":"","galleryName":"山艺术\u2022北京林正艺术空间","userName":"系统管理员","mapLng":"116.497423","mapLat":"39.992213","address":"北京市朝阳区酒仙桥路二号","area":"0","exhibitionIntroduction":"","questionRemarks":"","videoUrl":"|/message/front/exhibition/2018-11-15/1542267307033/6009d4e053644708b6d9dc8ea4a9aad7.mp4","videoUrlfalse":"","worksCount":"","videoCount":"","sculptureCount":"","deviceCount":"","otherDesc":""}
         */

        private ExhibitionBean exhibition;

        public ExhibitionBean getExhibition() {
            return exhibition;
        }

        public void setExhibition(ExhibitionBean exhibition) {
            this.exhibition = exhibition;
        }

        public static class ExhibitionBean {
            /**
             * id : 82d1668c7ad54c3e9fa369d878dcb626
             * isNewRecord : false
             * remarks :
             * createDate : 2018-11-15 15:35:07
             * updateDate : 2018-11-15 15:35:08
             * galleryId : 24b9715c0b074aa3914ee771a5632007
             * theme : test
             * userId : 1
             * status : 2
             * dateBegin : 2018-11-16
             * dateEnd : 2018-11-17
             * careLevel : 2
             * photo :
             * photoFalse :
             * smallPhoto :
             * smallPhotoFalse :
             * author :
             * authorIntroduction :
             * manager :
             * managerIntroduction :
             * galleryName : 山艺术•北京林正艺术空间
             * userName : 系统管理员
             * mapLng : 116.497423
             * mapLat : 39.992213
             * address : 北京市朝阳区酒仙桥路二号
             * area : 0
             * exhibitionIntroduction :
             * questionRemarks :
             * videoUrl : |/message/front/exhibition/2018-11-15/1542267307033/6009d4e053644708b6d9dc8ea4a9aad7.mp4
             * videoUrlfalse :
             * worksCount :
             * videoCount :
             * sculptureCount :
             * deviceCount :
             * otherDesc :
             */

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
            private String photoFalse;
            private String smallPhoto;
            private String smallPhotoFalse;
            private String author;
            private String authorIntroduction;
            private String manager;
            private String managerIntroduction;
            private String galleryName;
            private String userName;
            private String mapLng;
            private String mapLat;
            private String address;
            private String area;
            private String exhibitionIntroduction;
            private String questionRemarks;
            private String videoUrl;
            private String videoUrlfalse;
            private String worksCount;
            private String videoCount;
            private String sculptureCount;
            private String deviceCount;
            private String otherDesc;

            public String getFocusName() {
                return focusName;
            }

            public void setFocusName(String focusName) {
                this.focusName = focusName;
            }

            private String focusName;

            public int getIsFocus() {
                return isFocus;
            }

            public void setIsFocus(int isFocus) {
                this.isFocus = isFocus;
            }

            private int isFocus;



            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
            }

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

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDateBegin() {
                return dateBegin;
            }

            public void setDateBegin(String dateBegin) {
                this.dateBegin = dateBegin;
            }

            public String getDateEnd() {
                return dateEnd;
            }

            public void setDateEnd(String dateEnd) {
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

            public String getPhotoFalse() {
                return photoFalse;
            }

            public void setPhotoFalse(String photoFalse) {
                this.photoFalse = photoFalse;
            }

            public String getSmallPhoto() {
                return smallPhoto;
            }

            public void setSmallPhoto(String smallPhoto) {
                this.smallPhoto = smallPhoto;
            }

            public String getSmallPhotoFalse() {
                return smallPhotoFalse;
            }

            public void setSmallPhotoFalse(String smallPhotoFalse) {
                this.smallPhotoFalse = smallPhotoFalse;
            }

            public String getAuthor() {
                return author;
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

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getExhibitionIntroduction() {
                return exhibitionIntroduction;
            }

            public void setExhibitionIntroduction(String exhibitionIntroduction) {
                this.exhibitionIntroduction = exhibitionIntroduction;
            }

            public String getQuestionRemarks() {
                return questionRemarks;
            }

            public void setQuestionRemarks(String questionRemarks) {
                this.questionRemarks = questionRemarks;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getVideoUrlfalse() {
                return videoUrlfalse;
            }

            public void setVideoUrlfalse(String videoUrlfalse) {
                this.videoUrlfalse = videoUrlfalse;
            }

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
        }
    }
}
