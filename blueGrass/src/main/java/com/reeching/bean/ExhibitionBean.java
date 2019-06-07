package com.reeching.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/11/14.
 * auther:lenovo
 * Date：2018/11/14
 */
public class ExhibitionBean implements Serializable {

    /**
     * result : 1
     * msg : 获取成功！
     */

    private String result;
    private String msg;
    private List<InfosBean> infos;

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

    public List<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(List<InfosBean> infos) {
        this.infos = infos;
    }

    public static class InfosBean implements Serializable{
        /**
         * id : 04caeaeed067404f9f469b2d07a8024c
         * isNewRecord : false
         * remarks : 23架上
         * createDate : 2018-10-12 19:43:31
         * updateDate : 2018-10-14 11:46:56
         * galleryId : 24b9715c0b074aa3914ee771a5632014
         * theme : 动物喜剧，乐园之旅——郑成俊个展
         * userId : 2ea0918cbf464cbc9d00e7e0deec885e
         * status : 0
         * dateBegin : 2018-10-13
         * dateEnd : 2018-11-15
         * careLevel : 0
         * photo : |/message/front/exhibition/2018-10-12/1539344611626/4ccdf20814d04f3484d95fcc86a01ce2.jpg|/message/front/exhibition/2018-10-12/1539344611632/87dd6c4acd1e417ca255cd75b6c6b3c3.jpg|/message/front/exhibition/2018-10-12/1539344611635/d812edc48a9449f6852ac460860e2cd6.jpg|/message/front/exhibition/2018-10-12/1539344611701/33f9a57ec9a94c5c902383b6e14c90cd.jpg|/message/front/exhibition/2018-10-12/1539344611706/9a5120bb24064b38b32492da755e8a64.jpg|/message/front/exhibition/2018-10-12/1539344611711/307494aaeb894d9583a5344c9fab08d0.jpg|/message/front/exhibition/2018-10-12/1539344611716/f3578d1d00554bcf94974b3911ad9380.jpg|/message/front/exhibition/2018-10-12/1539344611722/bbdc878d867e49e7afbb9857c6970570.jpg|/message/front/exhibition/2018-10-12/1539344611727/e655c080ea5148858c3730f624d78bea.jpg|/message/front/exhibition/2018-10-14/1539488813722/5551eae77a354fbeae0080a9c0e4e4c3.jpg|/message/front/exhibition/2018-10-14/1539488813729/90c4210bd1a840efa3ed09609d6ea52b.jpg|/message/front/exhibition/2018-10-14/1539488813734/e639f4f069af4ecb99a49ffbae5c26f5.jpg|/message/front/exhibition/2018-10-14/1539488813739/5b092c1f763947d6acbc63a3ed26b1b3.jpg|/message/front/exhibition/2018-10-14/1539488813755/2ec5a69f2ddb42008cec8a084698a5ca.jpg|/message/front/exhibition/2018-10-14/1539488813760/57ff0358f4f84281a9bef9f30eea34ec.jpg|/message/front/exhibition/2018-10-14/1539488813765/6c9c841675ee44108b7a574c792c6f86.jpg|/message/front/exhibition/2018-10-14/1539488813770/394090b9f3f641b6b024accfa54028c5.jpg|/message/front/exhibition/2018-10-14/1539488813775/52588dc855194dffa8a70610051b11df.jpg
         * photoFalse :
         * smallPhoto : |/message/front/exhibition/2018-10-12/1539344611626/4ccdf20814d04f3484d95fcc86a01ce2_small.jpg|/message/front/exhibition/2018-10-12/1539344611632/87dd6c4acd1e417ca255cd75b6c6b3c3_small.jpg|/message/front/exhibition/2018-10-12/1539344611635/d812edc48a9449f6852ac460860e2cd6_small.jpg|/message/front/exhibition/2018-10-12/1539344611701/33f9a57ec9a94c5c902383b6e14c90cd_small.jpg|/message/front/exhibition/2018-10-12/1539344611706/9a5120bb24064b38b32492da755e8a64_small.jpg|/message/front/exhibition/2018-10-12/1539344611711/307494aaeb894d9583a5344c9fab08d0_small.jpg|/message/front/exhibition/2018-10-12/1539344611716/f3578d1d00554bcf94974b3911ad9380_small.jpg|/message/front/exhibition/2018-10-12/1539344611722/bbdc878d867e49e7afbb9857c6970570_small.jpg|/message/front/exhibition/2018-10-12/1539344611727/e655c080ea5148858c3730f624d78bea_small.jpg|/message/front/exhibition/2018-10-14/1539488813722/5551eae77a354fbeae0080a9c0e4e4c3_small.jpg|/message/front/exhibition/2018-10-14/1539488813729/90c4210bd1a840efa3ed09609d6ea52b_small.jpg|/message/front/exhibition/2018-10-14/1539488813734/e639f4f069af4ecb99a49ffbae5c26f5_small.jpg|/message/front/exhibition/2018-10-14/1539488813739/5b092c1f763947d6acbc63a3ed26b1b3_small.jpg|/message/front/exhibition/2018-10-14/1539488813755/2ec5a69f2ddb42008cec8a084698a5ca_small.jpg|/message/front/exhibition/2018-10-14/1539488813760/57ff0358f4f84281a9bef9f30eea34ec_small.jpg|/message/front/exhibition/2018-10-14/1539488813765/6c9c841675ee44108b7a574c792c6f86_small.jpg|/message/front/exhibition/2018-10-14/1539488813770/394090b9f3f641b6b024accfa54028c5_small.jpg|/message/front/exhibition/2018-10-14/1539488813775/52588dc855194dffa8a70610051b11df_small.jpg
         * smallPhotoFalse :
         * author : 郑成俊
         * authorIntroduction :
         * manager : 金炳宪
         * managerIntroduction :
         * galleryName : 作者画廊
         * userName : 作者画廊
         * mapLng : 116.497416
         * mapLat : 39.993303
         * address : 北京市朝阳区酒仙桥路2号798艺术区797中街01商务楼北楼壹层侧玻璃屋
         * area : 0
         * checkStatus :
         * exhibitionIntroduction :
         * questionRemarks :
         * videoUrl :
         * videoUrlfalse :
         * worksCount : '0'
         * videoCount : '0'
         * sculptureCount : '0'
         * deviceCount : '0'
         * otherDesc :
         * authorids :
         * managerids :
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
        private String checkStatus;
        private String exhibitionIntroduction;
        private String questionRemarks;
        private String videoUrl;
        private String videoUrlfalse;
        private String worksCount;
        private String videoCount;
        private String sculptureCount;
        private String deviceCount;
        private String otherDesc;
        private String authorids;
        private String managerids;

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

        public String getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(String checkStatus) {
            this.checkStatus = checkStatus;
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

        public String getAuthorids() {
            return authorids;
        }

        public void setAuthorids(String authorids) {
            this.authorids = authorids;
        }

        public String getManagerids() {
            return managerids;
        }

        public void setManagerids(String managerids) {
            this.managerids = managerids;
        }
    }
}
