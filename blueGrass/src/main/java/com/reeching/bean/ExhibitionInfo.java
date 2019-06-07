package com.reeching.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/11/26.
 * auther:lenovo
 * Date：2018/11/26
 */
public class ExhibitionInfo implements Serializable {
    /**
     * result : 1
     * msg : 获取成功！
     * infos : {"id":"b639c208e27b4346b9b270f4f49eeb7e","isNewRecord":false,"remarks":"","createDate":"2018-11-26 10:32:47","updateDate":"2018-11-26 15:10:32","galleryId":"24b9715c0b074aa3914ee771a5632007","theme":"罢了","userId":"1","status":"2","dateBegin":"2018-11-28","dateEnd":"2018-11-30","careLevel":"2","photo":"|/message/front/exhibition/2018-11-26/1543212642806/2504d3e121b04fd8a755abbe8727db72.jpg","photoFalse":"|/message/front/exhibition/2018-11-26/1543212642818/e120f54716c4474688ecdc779b915cc9.jpg|/message/front/exhibition/2018-11-26/1543213692371/b7d84b752dd64cd0b0216b79088cbd00.jpg|/message/front/exhibition/2018-11-26/1543213692416/6306f5b6990c459a8b0d353334b55d5e.jpg|/message/front/exhibition/2018-11-26/1543216114062/6afa0b8058f64aa79c988d666707ea28.jpg|/message/front/exhibition/2018-11-26/1543216229916/9a869a7ee9ab432c9428ca655f3505a6.jpg","smallPhoto":"|/message/front/exhibition/2018-11-26/1543212642806/2504d3e121b04fd8a755abbe8727db72_small.jpg","smallPhotoFalse":"|/message/front/exhibition/2018-11-26/1543212642818/e120f54716c4474688ecdc779b915cc9_small.jpg|/message/front/exhibition/2018-11-26/1543213692371/b7d84b752dd64cd0b0216b79088cbd00_small.jpg|/message/front/exhibition/2018-11-26/1543213692416/6306f5b6990c459a8b0d353334b55d5e_small.jpg|/message/front/exhibition/2018-11-26/1543216114062/6afa0b8058f64aa79c988d666707ea28_small.jpg|/message/front/exhibition/2018-11-26/1543216229916/9a869a7ee9ab432c9428ca655f3505a6_small.jpg","author":"","authorIntroduction":"","manager":"","managerIntroduction":"","galleryName":"山艺术-北京林正艺术空间","userName":"系统管理员","mapLng":"116.497423","mapLat":"39.992213","address":"北京市朝阳区酒仙桥路二号","area":"0","exhibitionIntroduction":"","questionRemarks":"","videoUrl":"|/message/front/exhibition/2018-11-26/1543212642795/8d43ece74d5e4f5c8bec3ed1e17cb816.mp4|/message/front/exhibition/2018-11-26/1543212642831/a6e223b7e60d4b5fae25e045ced658bd.mp4","videoUrlfalse":"|/message/front/exhibition/2018-11-26/1543213692397/2cb9a8bf97eb49cc82fed7398d7907cd.mp4|/message/front/exhibition/2018-11-26/1543213692424/1e13c2529ac74ba68779af3358ba4d70.mp4|/message/front/exhibition/2018-11-26/1543214023185/1234d42d71a14ce4acd6ecaee6ed9d43.mp4|/message/front/exhibition/2018-11-26/1543216114076/b2093d8a49fd4784b765cd5449f02606.mp4","worksCount":"","videoCount":"","sculptureCount":"","deviceCount":"","otherDesc":""}
     */

    private String result;
    private String msg;
    private InfosBean infos;

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

    public InfosBean getInfos() {
        return infos;
    }

    public void setInfos(InfosBean infos) {
        this.infos = infos;
    }

    public static class InfosBean extends ExhibitionBean.InfosBean {
    }
}
