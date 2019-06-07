package com.reeching;

/**
 * Created by 绍轩 on 2017/8/18.
 */

public class a {

    /**
     * result : 1
     * msg : 获取成功
     * info : {"id":"1","isNewRecord":false,"remarks":"最高管理员","createDate":"2013-05-27 08:00:00","updateDate":"2017-02-07 13:43:18","loginName":"admin","no":"0001","name":"系统管理员","email":"thinkgem@163.com","phone":"867532","mobile":"86751521","userType":"1","loginIp":"203.86.55.29","loginDate":"2017-08-17 16:34:23","loginFlag":"1","photo":"","oldLoginIp":"203.86.55.29","oldLoginDate":"2017-08-17 16:34:23","mapLng":"116.407449","mapLat":"39.878133","manageArea":"北京市北京市东城区永定门外大街22号","roleNames":"","admin":true}
     */

    private String result;
    private String msg;
    private InfoBean info;

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

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 1
         * isNewRecord : false
         * remarks : 最高管理员
         * createDate : 2013-05-27 08:00:00
         * updateDate : 2017-02-07 13:43:18
         * loginName : admin
         * no : 0001
         * name : 系统管理员
         * email : thinkgem@163.com
         * phone : 867532
         * mobile : 86751521
         * userType : 1
         * loginIp : 203.86.55.29
         * loginDate : 2017-08-17 16:34:23
         * loginFlag : 1
         * photo :
         * oldLoginIp : 203.86.55.29
         * oldLoginDate : 2017-08-17 16:34:23
         * mapLng : 116.407449
         * mapLat : 39.878133
         * manageArea : 北京市北京市东城区永定门外大街22号
         * roleNames :
         * admin : true
         */

        private String id;
        private boolean isNewRecord;
        private String remarks;
        private String createDate;
        private String updateDate;
        private String loginName;
        private String no;
        private String name;
        private String email;
        private String phone;
        private String mobile;
        private String userType;
        private String loginIp;
        private String loginDate;
        private String loginFlag;
        private String photo;
        private String oldLoginIp;
        private String oldLoginDate;
        private String mapLng;
        private String mapLat;
        private String manageArea;
        private String roleNames;
        private boolean admin;

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

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getLoginIp() {
            return loginIp;
        }

        public void setLoginIp(String loginIp) {
            this.loginIp = loginIp;
        }

        public String getLoginDate() {
            return loginDate;
        }

        public void setLoginDate(String loginDate) {
            this.loginDate = loginDate;
        }

        public String getLoginFlag() {
            return loginFlag;
        }

        public void setLoginFlag(String loginFlag) {
            this.loginFlag = loginFlag;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getOldLoginIp() {
            return oldLoginIp;
        }

        public void setOldLoginIp(String oldLoginIp) {
            this.oldLoginIp = oldLoginIp;
        }

        public String getOldLoginDate() {
            return oldLoginDate;
        }

        public void setOldLoginDate(String oldLoginDate) {
            this.oldLoginDate = oldLoginDate;
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

        public String getManageArea() {
            return manageArea;
        }

        public void setManageArea(String manageArea) {
            this.manageArea = manageArea;
        }

        public String getRoleNames() {
            return roleNames;
        }

        public void setRoleNames(String roleNames) {
            this.roleNames = roleNames;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            this.admin = admin;
        }
    }
}
